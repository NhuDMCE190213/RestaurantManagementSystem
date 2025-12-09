package controller;

import dao.*;
import model.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path; // Import Path
import java.nio.file.Paths;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.util.List;

/**
 *
 * @author Huynh Thai Duy Phuong - CE190603
 */
@WebServlet(name = "MenuItemServlet", urlPatterns = {"/menuitem"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class MenuItemServlet extends HttpServlet {

    private final int MAX_ELEMENTS_PER_PAGE = 10;
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    // Must match ImageServingServlet
    private static final String UPLOAD_DIRECTORY_NAME = "menu_images_storage";
    private static final String UPLOAD_URL_PREFIX = "images/";

    /**
     * Helper method to determine the image storage path based on Tomcat/User
     * home and the predefined directory name.
     *
     * @return The absolute Path for the image upload directory.
     */
    private Path getUploadDirectoryPath() {
        String applicationBaseDir = System.getProperty("catalina.base", System.getProperty("user.home"));
        return new File(applicationBaseDir, UPLOAD_DIRECTORY_NAME).toPath();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String view = request.getParameter("view");
        String namepage = "";

        if (view == null || view.isBlank() || view.equalsIgnoreCase("list")) {
            namepage = "emp-list";
        } else if (view.equalsIgnoreCase("add")) {
            namepage = "add";
            loadFormData(request);
        } else if (view.equalsIgnoreCase("edit")) {
            namepage = "edit";

            int id;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }
            request.setAttribute("menuItem", menuItemDAO.getElementByID(id));
            loadFormData(request);
        }

        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }
//search
        String keyword = request.getParameter("keyword");
        int totalItems;
        List<MenuItem> menuItemsList;

        if (keyword != null && !keyword.trim().isEmpty()) {
            menuItemsList = menuItemDAO.searchAll(keyword, page, MAX_ELEMENTS_PER_PAGE);
            totalItems = menuItemDAO.countItem(keyword);
            request.setAttribute("keyword", keyword);
        } else {
            menuItemsList = menuItemDAO.getAll(page, MAX_ELEMENTS_PER_PAGE);
            totalItems = menuItemDAO.countItem();
        }

        int totalPages = getTotalPages(totalItems);

        request.setAttribute("menuItemsList", menuItemsList);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);

        request.getRequestDispatcher("/WEB-INF/menu/" + namepage + ".jsp").forward(request, response);
        removePopup(request);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        boolean popupStatus = true;
        String popupMessage = "";
        if (action != null && !action.isEmpty()) {
            if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("edit")) {
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

                String itemName = request.getParameter("itemName");
                String priceStr = request.getParameter("price");
                String description = request.getParameter("description");
                String status = request.getParameter("status");
                String existingImageUrl = request.getParameter("existingImageUrl");

                int categoryId = parseIntOrDefault(request.getParameter("categoryId"), -1);

                if (itemName == null || itemName.isBlank() || itemName.matches(".*\\d.*")) {
                    popupStatus = false;
                    popupMessage = "Invalid item name.";
                } else if (menuItemDAO.checkItemNameExist(itemName, id)) {
                    popupStatus = false;
                    popupMessage = "Item name already exists. Choose another.";
                } else if (priceStr == null || !priceStr.matches("\\d+")) {
                    popupStatus = false;
                    popupMessage = "Price must be a valid number.";
                } else {
                    int price;
                    try {
                        price = Integer.parseInt(priceStr);
                    } catch (NumberFormatException e) {
                        popupStatus = false;
                        popupMessage = "Price must be between 5.000 and 5.000.000 VND.";
                        setPopup(request, popupStatus, popupMessage);

                        response.sendRedirect(request.getContextPath() + "/menuitem");
                        return;

                    }
                    if (price < 5000 || price > 5000000) {
                        popupStatus = false;
                        popupMessage = "Price must be between 5.000 and 5.000.000 VND.";
                    } else {

                        Path uploadDirPath = getUploadDirectoryPath();
                        String uploadPath = uploadDirPath.toString();

                        String newImageUrl = existingImageUrl;
                        Part filePart = request.getPart("imageFile");
                        String fileName = (filePart != null && filePart.getSubmittedFileName() != null)
                                ? Paths.get(filePart.getSubmittedFileName()).getFileName().toString()
                                : "";
                        if (!fileName.isEmpty()) {

                            File uploadDir = uploadDirPath.toFile();
                            if (!uploadDir.exists()) {
                                if (!uploadDir.mkdirs()) {
                                    throw new IOException("Failed to create directory: " + uploadPath + ". Check permissions.");
                                }
                            }
                            String uniqueFileName = fileName;
                            String fullFilePath = uploadPath + File.separator + uniqueFileName;

                            filePart.write(fullFilePath);

                            newImageUrl = UPLOAD_URL_PREFIX + uniqueFileName;
                        }

                        Category category = categoryDAO.getElementByID(categoryId);

                        if (category == null) {
                            popupStatus = false;
                            popupMessage = "Invalid category or recipe.";
                        } else {
                            if (status == null || status.isBlank()) {
                                status = "Active";
                            }
                            MenuItem item = new MenuItem(id, category, itemName, newImageUrl, price, description, status);

                            int result;

                            if (action.equalsIgnoreCase("add")) {
                                result = menuItemDAO.add(item);
                                popupMessage = (result >= 1) ? "Item added successfully." : "Add failed.";
                            } else if (action.equalsIgnoreCase("edit")) {
                                result = menuItemDAO.edit(item);
                                popupMessage = (result >= 1) ? "Item updated successfully." : "Edit failed.";
                            } else {
                                result = -1;
                                popupMessage = "Invalid action.";
                            }
                            popupStatus = result >= 1;
                        }
                    }
                }
            } else if (action.equalsIgnoreCase("delete")) {
                int id = parseIntOrDefault(request.getParameter("id"), -1);
                if (id <= 0) {
                    popupStatus = false;
                    popupMessage = "Invalid item ID.";
                } else {
                    int check = menuItemDAO.delete(id);
                    popupStatus = check >= 1;
                    popupMessage = popupStatus ? "Item deleted successfully." : "Delete failed.";
                }
            }
        }

        setPopup(request, popupStatus, popupMessage);
        response.sendRedirect(request.getContextPath() + "/menuitem");
    }

    private void loadFormData(HttpServletRequest request) {
        request.setAttribute("categories", categoryDAO.getAll());
    }

    private int parseIntOrDefault(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private int getTotalPages(int countItems) {
        return (int) Math.ceil((double) countItems / MAX_ELEMENTS_PER_PAGE);
    }

    private void setPopup(HttpServletRequest request, boolean status, String message) {
        HttpSession session = request.getSession(false);
        session.setAttribute("popupStatus", status);
        session.setAttribute("popupMessage", message);
    }

    private void removePopup(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("popupStatus");
            session.removeAttribute("popupMessage");
        }
    }

    @Override
    public String getServletInfo() {
        return "";
    }
}
