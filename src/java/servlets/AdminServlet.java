/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import converters.ConvertorToJson;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import session.UserFacade;

/**
 *
 * @author user
 */
@WebServlet(name = "AdminServlet", urlPatterns = {
    "/changeRolesData",
    "/addRole",
    "/removeRole",
})
public class AdminServlet extends HttpServlet {
    @EJB private UserFacade userFacade;

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
        JsonObjectBuilder job = Json.createObjectBuilder();
        
        String path = request.getServletPath();
        switch (path) {
            case "/changeRolesData":
                try {
                    List<User> listUsers = userFacade.findAll();
                    List<String> listRoles = new ArrayList<>();
                    for(int i = 0;i<UserServlet.role.values().length;i++){
                       listRoles.add(UserServlet.role.values()[i].toString()); 
                    }
                    job.add("users", new ConvertorToJson().getJsonArrayUsers(listUsers));
                    job.add("roles", new ConvertorToJson().getJsonArrayRoles(listRoles));
                    job.add("status", true);
                } catch (Exception e) {
                    job.add("users", "");
                    job.add("roles", "");
                    job.add("status", false);
                    job.add("info", e.toString());
                }
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/addRole":
                JsonReader jsonReader = Json.createReader(request.getReader());
                JsonObject jsonObject = jsonReader.readObject();
                String userId = jsonObject.getString("userId");
                String role = jsonObject.getString("role");
                User user = userFacade.find(Long.parseLong(userId));
                if(user == null){
                    job.add("info", "Нет такого пользователя");
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
                    break;
                }
                if(!user.getRoles().contains(role)){
                    user.getRoles().add(role);
                    job.add("info", "Роль изменена");
                    userFacade.edit(user);
                }else{
                    job.add("info", "Такая роль у пользователя уже есть");
                }
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/removeRole":
                jsonReader = Json.createReader(request.getReader());
                jsonObject = jsonReader.readObject();
                userId = jsonObject.getString("userId");
                role = jsonObject.getString("role");
                user = userFacade.find(Long.parseLong(userId));
                if(user == null){
                    job.add("info", "Нет такого пользователя");
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
                    break;
                }
                if(user.getRoles().contains(role)){
                    user.getRoles().remove(role);
                    userFacade.edit(user);
                    job.add("info", "Роль изменена");
                }else{
                    job.add("info", "Такой роли у пользователя нет");
                    
                }
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
        }
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
