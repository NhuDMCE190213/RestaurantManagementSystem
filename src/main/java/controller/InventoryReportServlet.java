package controller;

import static constant.CommonFunction.removePopup;
import static constant.CommonFunction.setPopup;
import static constant.CommonFunction.validateInteger;
import dao.IngredientDAO;
import dao.InventoryReportDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DailyUsageSummary;
import model.Employee;
import model.Ingredient;
import model.IngredientUsageReport;

/**
 * Handles the inventory usage report flow, allowing end-of-day deductions based on completed orders.
 */
@WebServlet(name = "InventoryReportServlet", urlPatterns = {"/inventory-report"})
public class InventoryReportServlet extends HttpServlet {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String SESSION_PENDING_MANUAL = "pendingManualUsage";

    private final InventoryReportDAO reportDAO = new InventoryReportDAO();
    private final IngredientDAO ingredientDAO = new IngredientDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ensureSession(request);

        prepareDetailPage(request);

        request.getRequestDispatcher("/WEB-INF/report/usage.jsp").forward(request, response);
        removePopup(request);
    }

    private void prepareDetailPage(HttpServletRequest request) {
        LocalDate reportDate = parseDate(request.getParameter("date"));
        if (reportDate == null) {
            reportDate = LocalDate.now();
        }

        boolean alreadyProcessed = reportDAO.hasUsageReportForDate(reportDate);

        HttpSession session = request.getSession(false);

        List<IngredientUsageReport> usageList;
        if (alreadyProcessed) {
            usageList = reportDAO.getRecordedUsage(reportDate);
            clearPendingManualEntries(session, reportDate);
        } else {
            usageList = reportDAO.calculateDailyUsage(reportDate);
            applyCurrentStockSnapshot(usageList);

            List<IngredientUsageReport> pendingManual = getPendingManualEntries(session, reportDate);
            if (!pendingManual.isEmpty()) {
                usageList.addAll(pendingManual);
            }
        }

        DailyUsageSummary summary = reportDAO.getDailySummary(reportDate);
        double totalUsage = 0;
        LocalDateTime processedAt = null;
        String processedByName = null;

        for (IngredientUsageReport usageReport : usageList) {
            totalUsage += usageReport.getQuantityUsed();

            if (processedAt == null && usageReport.getProcessedAt() != null) {
                processedAt = usageReport.getProcessedAt();
            }

            if (processedByName == null && usageReport.getProcessedByName() != null) {
                processedByName = usageReport.getProcessedByName();
            }
        }

        String processedAtDisplay = null;
        if (processedAt != null) {
            processedAtDisplay = processedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        request.setAttribute("reportDate", reportDate);
        request.setAttribute("usageList", usageList);
        request.setAttribute("alreadyProcessed", alreadyProcessed);
        request.setAttribute("summary", summary);
        request.setAttribute("totalUsage", totalUsage);
        request.setAttribute("processedAt", processedAtDisplay);
        request.setAttribute("processedByName", processedByName);
        request.setAttribute("hasData", !usageList.isEmpty());
        request.setAttribute("ingredientList", ingredientDAO.getAll());

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ensureSession(request);
        String action = request.getParameter("action");
        LocalDate reportDate = parseDate(request.getParameter("date"));
        if (reportDate == null) {
            reportDate = LocalDate.now();
        }

        if ("manual".equalsIgnoreCase(action)) {
            handleManualAdjustment(request, reportDate);
        } else if ("confirm".equalsIgnoreCase(action)) {
            handleConfirmReport(request, reportDate);
        }

        response.sendRedirect(request.getContextPath() + "/inventory-report?date=" + reportDate);
    }

    private void handleManualAdjustment(HttpServletRequest request, LocalDate reportDate) {
        int ingredientId;
        try {
            ingredientId = Integer.parseInt(request.getParameter("ingredientId"));
        } catch (NumberFormatException ex) {
            ingredientId = -1;
        }

        double remainingQuantity;
        String remainingQuantityRaw = request.getParameter("remainingQuantity");
        try {
            remainingQuantity = Double.parseDouble(remainingQuantityRaw);
        } catch (NumberFormatException | NullPointerException ex) {
            remainingQuantity = -1;
        }

        if (!validateInteger(ingredientId, false, false, true) || remainingQuantity < 0) {
            setPopup(request, false, "Please select an ingredient and provide a valid end-of-day quantity.");
            return;
        }

        Ingredient ingredient = ingredientDAO.getElementByID(ingredientId);
        if (ingredient == null) {
            setPopup(request, false, "Unable to find the ingredient for adjustment.");
            return;
        }

        double stockBefore = ingredient.getTotalQuantity();
        if (stockBefore < 0) {
            stockBefore = 0;
        }

        if (remainingQuantity > stockBefore) {
            setPopup(request, false, "Remaining quantity exceeds current stock (" + stockBefore + "). Please double-check your numbers.");
            return;
        }

        double quantityUsed = stockBefore - remainingQuantity;
        if (quantityUsed <= 0) {
            setPopup(request, false, "No usage could be derived from the current measurement.");
            return;
        }

        String note = request.getParameter("note");
        if (note != null) {
            note = note.trim();
        }
        if (note != null && note.length() > 255) {
            setPopup(request, false, "Notes are limited to 255 characters.");
            return;
        }
        if (note != null && note.isEmpty()) {
            note = null;
        }

        double stockAfter = remainingQuantity;

        HttpSession session = request.getSession(true);

        IngredientUsageReport manualEntry = new IngredientUsageReport(
                ingredientId,
                ingredient.getIngredientName(),
                ingredient.getUnit(),
                quantityUsed
        );
        manualEntry.setStockBefore(stockBefore);
        manualEntry.setStockAfter(stockAfter);
        manualEntry.setNote(note);

        addPendingManualEntry(session, reportDate, manualEntry);

        StringBuilder employeeMessage = new StringBuilder()
            .append(ingredient.getIngredientName())
            .append(": recorded usage of ")
            .append(quantityUsed)
            .append(" ")
            .append(ingredient.getUnit())
            .append(". Stock will be deducted once you confirm.");
        if (note != null && !note.isBlank()) {
            employeeMessage.append(" Note: ").append(note);
        }
        setPopup(request, true, employeeMessage.toString());
    }

    private void handleConfirmReport(HttpServletRequest request, LocalDate reportDate) {
        finalizeAutomaticUsageReport(request, reportDate);
    }

    private void finalizeAutomaticUsageReport(HttpServletRequest request, LocalDate reportDate) {
        HttpSession session = request.getSession(false);
        Employee employee = session != null ? (Employee) session.getAttribute("employeeSession") : null;
        Integer employeeId = employee != null ? employee.getEmpId() : null;

        if (reportDAO.hasUsageReportForDate(reportDate)) {
            setPopup(request, true, "The report for " + reportDate + " was already confirmed.");
            return;
        }

        List<IngredientUsageReport> usageList = reportDAO.calculateDailyUsage(reportDate);
        applyCurrentStockSnapshot(usageList);

        List<IngredientUsageReport> pendingManual = getPendingManualEntries(session, reportDate);
        if (!pendingManual.isEmpty()) {
            usageList.addAll(pendingManual);
        }

        if (usageList.isEmpty()) {
            setPopup(request, false, "There is no data to confirm for " + reportDate + ".");
            return;
        }

        try {
            reportDAO.saveUsageReport(reportDate, employeeId, usageList);
            String successMessage = "Confirmed and deducted inventory for " + usageList.size() + " ingredients on " + reportDate + ".";
            setPopup(request, true, successMessage);
            clearPendingManualEntries(session, reportDate);
        } catch (SQLException ex) {
            String errorMessage = "Unable to confirm report: " + ex.getMessage();
            setPopup(request, false, errorMessage);
        }
    }

    @SuppressWarnings("unchecked")
    private void addPendingManualEntry(HttpSession session, LocalDate reportDate, IngredientUsageReport entry) {
        if (session == null || reportDate == null || entry == null) {
            return;
        }

        Map<String, List<IngredientUsageReport>> pendingMap = (Map<String, List<IngredientUsageReport>>) session.getAttribute(SESSION_PENDING_MANUAL);
        if (pendingMap == null) {
            pendingMap = new HashMap<>();
            session.setAttribute(SESSION_PENDING_MANUAL, pendingMap);
        }

        String key = reportDate.toString();
        List<IngredientUsageReport> entries = pendingMap.computeIfAbsent(key, unused -> new ArrayList<>());
        entries.add(entry);
    }

    @SuppressWarnings("unchecked")
    private List<IngredientUsageReport> getPendingManualEntries(HttpSession session, LocalDate reportDate) {
        if (session == null || reportDate == null) {
            return new ArrayList<>();
        }

        Map<String, List<IngredientUsageReport>> pendingMap = (Map<String, List<IngredientUsageReport>>) session.getAttribute(SESSION_PENDING_MANUAL);
        if (pendingMap == null) {
            return new ArrayList<>();
        }

        List<IngredientUsageReport> entries = pendingMap.get(reportDate.toString());
        if (entries == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(entries);
    }

    @SuppressWarnings("unchecked")
    private void clearPendingManualEntries(HttpSession session, LocalDate reportDate) {
        if (session == null || reportDate == null) {
            return;
        }

        Map<String, List<IngredientUsageReport>> pendingMap = (Map<String, List<IngredientUsageReport>>) session.getAttribute(SESSION_PENDING_MANUAL);
        if (pendingMap == null) {
            return;
        }

        pendingMap.remove(reportDate.toString());
    }

    private void applyCurrentStockSnapshot(List<IngredientUsageReport> usageList) {
        if (usageList == null || usageList.isEmpty()) {
            return;
        }

        List<Ingredient> ingredients = ingredientDAO.getAll();

        for (IngredientUsageReport report : usageList) {
            double stockBefore = 0;
            for (Ingredient ingredient : ingredients) {
                if (ingredient.getIngredientId() == report.getIngredientId()) {
                    stockBefore = ingredient.getTotalQuantity();
                    break;
                }
            }

            report.setStockBefore(stockBefore);
            report.setStockAfter(stockBefore - report.getQuantityUsed());
        }
    }

    private LocalDate parseDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(raw.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }


    private void ensureSession(HttpServletRequest request) {
        if (request.getSession(false) == null) {
            request.getSession(true);
        }
    }
}
