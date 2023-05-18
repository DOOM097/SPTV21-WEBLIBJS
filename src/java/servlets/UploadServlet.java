/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Cover;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import session.CoverFacade;

/**
 *
 * @author user
 */
@WebServlet(name = "UploadServlet", urlPatterns = {
    "/addCover", 
    "/uploadCover"
})
@MultipartConfig
public class UploadServlet extends HttpServlet {
    @EJB CoverFacade coverFacade;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        JsonObjectBuilder job = Json.createObjectBuilder();
        HttpSession session = request.getSession(false);
        if(session == null){
            job.add("info", "У вас нет прав, авторизуйтесь!");
            try (PrintWriter out = response.getWriter()) {
                out.println(job.build().toString());
            }
            return;
        }
        User authUser = (User) session.getAttribute("authUser");
        if(authUser == null || !authUser.getRoles().contains(UserServlet.role.MANAGER.toString())){
            job.add("info", "У вас нет прав, авторизуйтесь!");
            try (PrintWriter out = response.getWriter()) {
                out.println(job.build().toString());
            }
            return;
        }
        String uploadFolder = "C:\\Users\\user\\UploadDir\\SPTV21WebLibraryJS";
        String path = request.getServletPath();
        switch (path) {
            case "/uploadCover":
                List<Part> fileParts = request.getParts().stream().filter(
                        part -> "file".equals(part.getName()))
                        .collect(Collectors.toList());
                StringBuilder sb = new StringBuilder();
                for (Part filePart : fileParts) {
                    sb.append(uploadFolder + File.separator + getFileName(filePart));
                    File file = new File(sb.toString());
                    file.mkdirs();
                    try(InputStream fileContent = filePart.getInputStream()){
                        Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    Cover cover = new Cover();
                    cover.setDescription(request.getParameter("description"));
                    cover.setUrl(sb.toString());
                    coverFacade.create(cover);
                }
                job.add("info", "Обложка для книги успешно загружена");
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            
        }   
        
        
    }
    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")){
            if(content.trim().startsWith("filename")){
                return content
                        .substring(content.indexOf('=')+1)
                        .trim()
                        .replace("\"",""); 
            }
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
