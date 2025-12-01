package controller;

import static constant.CommonFunction.*;
import dao.TypeDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TypeServlet", urlPatterns = {"/type"})
public class TypeServlet extends HttpServlet {

    private final TypeDAO typeDAO = new TypeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = request.getParameter("view");
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }

        String namepage;
        if (!validateString(view, -1) || "list".equalsIgnoreCase(view)) {
            namepage = "list";
        } else if ("add".equalsIgnoreCase(view)) {
            namepage = "add";
        } else if ("edit".equalsIgnoreCase(view)) {
            namepage = "edit";

            int id;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }

            request.setAttribute("currentType", typeDAO.getElementByID(id));
        } else if ("delete".equalsIgnoreCase(view)) {
            namepage = "delete";
        } else {
            namepage = "list";
        }

        int page;
        int totalPages = getTotalPages(typeDAO.countItem());

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword);
        request.setAttribute("typesList", typeDAO.getAll(page, keyword));

        request.getRequestDispatcher("/WEB-INF/type/" + namepage + ".jsp").forward(request, response);
        removePopup(request);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        boolean popupStatus = true;
        String popupMessage = "";

        if (action != null && !action.isEmpty()) {

            if ("add".equalsIgnoreCase(action)) {
                String typeName = request.getParameter("typeName");
                String description = request.getParameter("description");

                if (!validateString(typeName, -1)) {
                    popupStatus = false;
                    popupMessage = "The add action is NOT successfull. The input has some error.";
                } else {
                    popupMessage = "The object with name=" + typeName + " added successfull.";
                }

                if (popupStatus) {
                    int checkError = typeDAO.add(typeName, description);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The add action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }

            } else if ("edit".equalsIgnoreCase(action)) {
                int id;
                String typeName = request.getParameter("typeName");
                String description = request.getParameter("description");

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

                if (!validateString(typeName, -1)
                        || !validateInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The edit action is NOT successfull. The input has some error.";
                } else {
                    popupMessage = "The object with id=" + id + " edited successfull.";
                }

                if (popupStatus) {
                    int checkError = typeDAO.edit(id, typeName, description);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The edit action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            } else if ("delete".equalsIgnoreCase(action)) {

                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

                if (!validateInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The delete action is NOT successfull.";
                } else {
                    popupMessage = "The object with id=" + id + " deleted successfull.";
                }

                if (popupStatus) {
                    int checkError = typeDAO.delete(id);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The delete action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            }
        }

        setPopup(request, popupStatus, popupMessage);
        response.sendRedirect(request.getContextPath() + "/type");
    }

    @Override
    public String getServletInfo() {
        return "Type management servlet";
    }
}
