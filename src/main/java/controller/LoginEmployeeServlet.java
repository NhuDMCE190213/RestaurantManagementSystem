package controller;

import dao.EmployeeDAO; 
import db.DBContext;
import model.Employee;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
/**
 * 
 * @author Huynh Thai Duy Phuong - CE190603 
 */

@WebServlet(name = "LoginEmployeeServlet", urlPatterns = {"/login_employee"})
public class LoginEmployeeServlet extends HttpServlet {

   
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private DBContext dbContext = new DBContext();

  
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
      
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EmployeeLoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EmployeeLoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

/**
 * 
 * @param request
 * @param response
 * @throws ServletException
 * @throws IOException 
 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/authentication/employee-login.jsp").forward(request, response);
    }

 /**
  * 
  * @param request
  * @param response
  * @throws ServletException
  * @throws IOException 
  */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

     
        String empAccount = request.getParameter("account");
        String password = request.getParameter("password");

        String errorMessage = "";

        if (empAccount == null || empAccount.isEmpty() || password == null || password.isEmpty()) {
            errorMessage = "Please enter valid Username and Password.";
        } else {
            
            
            String hashedPassword = dbContext.hashToMD5(password);
            
           
            Employee employee = employeeDAO.authenticate(empAccount, hashedPassword);

            if (employee != null) {
                
                   HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }

  session = request.getSession(true);
                session.setAttribute("employeeSession", employee);
                session.setMaxInactiveInterval(30 * 60); 
     
                response.sendRedirect(request.getContextPath() + "/order"); 
                return;
            } else {
                
                
                errorMessage = "Incorrect username or password. Your account might also be banned. Please try again.";
            }
        }

       
        request.setAttribute("error", errorMessage);
        request.setAttribute("account", empAccount);


        request.getRequestDispatcher("/WEB-INF/authentication/employee-login.jsp").forward(request, response);
    }

/*
    
    */
    @Override
    public String getServletInfo() {
        return "Handles employee login and authentication";
    }// </editor-fold>

}