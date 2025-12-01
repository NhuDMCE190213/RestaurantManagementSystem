package dao;

import db.DBContext;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.DailyUsageSummary;
import model.IngredientUsageReport;

/**
 * Provides data access helpers for daily kitchen usage reports.
 */
public class InventoryReportDAO extends DBContext {

    public List<IngredientUsageReport> calculateDailyUsage(LocalDate reportDate) {
        List<IngredientUsageReport> results = new ArrayList<>();

        if (reportDate == null) {
            return results;
        }

        try {
            String query = "SELECT ri.ingredient_id, ing.ingredient_name, ing.unit, "
                    + "SUM(CAST(oi.quantity AS DECIMAL(18, 2)) * ri.quantity) AS total_usage "
                    + "FROM [order] AS o "
                    + "JOIN order_item AS oi ON o.order_id = oi.order_id "
                    + "JOIN menu_item AS mi ON oi.menu_item_id = mi.menu_item_id "
                    + "JOIN recipe AS r ON mi.recipe_id = r.recipe_id "
                    + "JOIN recipe_item AS ri ON r.recipe_id = ri.recipe_id "
                    + "JOIN ingredient AS ing ON ri.ingredient_id = ing.ingredient_id "
                    + "WHERE CAST(o.order_date AS DATE) = ? "
                    + "AND LOWER(o.status) = LOWER(N'Completed') "
                    + "AND (r.status IS NULL OR LOWER(r.status) <> LOWER(N'Deleted')) "
                    + "AND (ri.status IS NULL OR LOWER(ri.status) <> LOWER(N'Deleted')) "
                    + "GROUP BY ri.ingredient_id, ing.ingredient_name, ing.unit "
                    + "ORDER BY ing.ingredient_name";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{Date.valueOf(reportDate)});

            while (rs.next()) {
                IngredientUsageReport report = new IngredientUsageReport(
                        rs.getInt("ingredient_id"),
                        rs.getNString("ingredient_name"),
                        rs.getString("unit"),
                        rs.getDouble("total_usage")
                );
                results.add(report);
            }
        } catch (SQLException ex) {
        }

        return results;
    }

    public DailyUsageSummary getDailySummary(LocalDate reportDate) {
        DailyUsageSummary summary = new DailyUsageSummary();

        if (reportDate == null) {
            return summary;
        }

        try {
            String query = "SELECT COUNT(DISTINCT o.order_id) AS order_count, "
                    + "COALESCE(SUM(CASE WHEN oi.quantity IS NULL THEN 0 ELSE oi.quantity END), 0) AS item_count "
                    + "FROM [order] AS o "
                    + "LEFT JOIN order_item AS oi ON o.order_id = oi.order_id "
                    + "WHERE CAST(o.order_date AS DATE) = ? "
                    + "AND LOWER(o.status) = LOWER(N'Completed')";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{Date.valueOf(reportDate)});
            if (rs.next()) {
                summary.setReportDate(reportDate);
                summary.setCompletedOrders(rs.getInt("order_count"));
                summary.setItemsPrepared(rs.getInt("item_count"));
            }
        } catch (SQLException ex) {
        }

        return summary;
    }

    public boolean hasUsageReportForDate(LocalDate reportDate) {
        if (reportDate == null) {
            return false;
        }

        try {
            String query = "SELECT COUNT(usage_id) AS total FROM ingredient_usage WHERE usage_date = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{Date.valueOf(reportDate)});
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException ex) {
        }

        return false;
    }

    public List<IngredientUsageReport> getRecordedUsage(LocalDate reportDate) {
        List<IngredientUsageReport> results = new ArrayList<>();

        if (reportDate == null) {
            return results;
        }

        try {
            String query = "SELECT iu.ingredient_id, ing.ingredient_name, ing.unit, iu.quantity_used, "
                    + "iu.stock_before, iu.stock_after, iu.note, iu.created_by, iu.created_at, e.emp_name "
                    + "FROM ingredient_usage AS iu "
                    + "JOIN ingredient AS ing ON iu.ingredient_id = ing.ingredient_id "
                    + "LEFT JOIN employee AS e ON iu.created_by = e.emp_id "
                    + "WHERE iu.usage_date = ? "
                    + "ORDER BY ing.ingredient_name";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{Date.valueOf(reportDate)});

            while (rs.next()) {
                IngredientUsageReport report = new IngredientUsageReport();
                report.setIngredientId(rs.getInt("ingredient_id"));
                report.setIngredientName(rs.getNString("ingredient_name"));
                report.setUnit(rs.getString("unit"));
                report.setQuantityUsed(rs.getDouble("quantity_used"));
                report.setStockBefore(rs.getDouble("stock_before"));
                report.setStockAfter(rs.getDouble("stock_after"));
                report.setNote(rs.getNString("note"));

                int createdBy = rs.getInt("created_by");
                if (!rs.wasNull()) {
                    report.setProcessedBy(createdBy);
                }

                String createdByName = rs.getNString("emp_name");
                if (createdByName != null) {
                    report.setProcessedByName(createdByName);
                }

                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    report.setProcessedAt(createdAt.toLocalDateTime());
                }

                results.add(report);
            }
        } catch (SQLException ex) {
        }

        return results;
    }

    public int saveUsageReport(LocalDate reportDate, Integer createdBy, List<IngredientUsageReport> items) throws SQLException {
        if (reportDate == null || items == null || items.isEmpty()) {
            return 0;
        }

        String sql = "INSERT INTO ingredient_usage (usage_date, ingredient_id, quantity_used, stock_before, stock_after, note, created_by) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int inserted = 0;
        for (IngredientUsageReport item : items) {
            Object noteParam = item.getNote();
            if (noteParam instanceof String && ((String) noteParam).isBlank()) {
                noteParam = null;
            }

            Object createdByParam = (createdBy != null && createdBy > 0) ? createdBy : null;

            Object[] params = new Object[]{
                Date.valueOf(reportDate),
                item.getIngredientId(),
                BigDecimal.valueOf(item.getQuantityUsed()),
                BigDecimal.valueOf(item.getStockBefore()),
                BigDecimal.valueOf(item.getStockAfter()),
                noteParam,
                createdByParam
            };

            inserted += this.executeQuery(sql, params);
        }

        return inserted;
    }
}
