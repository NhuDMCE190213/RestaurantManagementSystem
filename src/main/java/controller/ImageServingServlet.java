package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;


@WebServlet("/images/*") 
public class ImageServingServlet extends HttpServlet {

    // must match MenuItemServlet
    private static final String UPLOAD_DIRECTORY_NAME = "menu_images_storage"; 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestedFile = request.getPathInfo();

        if (requestedFile == null || requestedFile.isEmpty() || requestedFile.equals("/")) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND); 
            return;
        }
        String fileName = requestedFile.substring(1); 
        String applicationBaseDir = System.getProperty("catalina.base", System.getProperty("user.home"));
        Path externalPath = new File(new File(applicationBaseDir, UPLOAD_DIRECTORY_NAME), fileName).toPath();

        if (!Files.exists(externalPath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); 
            return;
        }
        String contentType = getServletContext().getMimeType(fileName);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        response.setContentType(contentType);
        response.setContentLengthLong(Files.size(externalPath));
        try (InputStream input = Files.newInputStream(externalPath);
             OutputStream output = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.err.println("Error streaming image: " + e.getMessage());
        }
    }
}