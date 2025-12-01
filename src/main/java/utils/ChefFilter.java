/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Employee;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
@WebFilter(filterName = "ChefFilter", urlPatterns = {"/ingredient", "/recipe", "/import"})
public class ChefFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("AdminFilter called");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        
        if (session == null || session.getAttribute("employeeSession") == null
                || (((Employee) session.getAttribute("employeeSession")).getRole().getId() != 1 // admin
                && ((Employee) session.getAttribute("employeeSession")).getRole().getId() != 2  //manager
                && ((Employee) session.getAttribute("employeeSession")).getRole().getId() != 4)) {  //chef 
            resp.sendRedirect(req.getContextPath() + "/login_employee");
            return;
        }

        chain.doFilter(request, response);
    }

}
