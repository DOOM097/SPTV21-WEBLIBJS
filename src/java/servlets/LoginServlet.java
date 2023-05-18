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
import javax.servlet.http.HttpSession;
import session.UserFacade;
import tools.PasswordEncrypt;

/**
 *
 * @author user
 */
@WebServlet(name = "LoginServlet",loadOnStartup = 1, urlPatterns = {
    "/login",
    "/logout",
    
})

public class LoginServlet extends HttpServlet {
    @EJB private UserFacade userFacade; 
    
    private PasswordEncrypt pe = new PasswordEncrypt();

       
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
        String path = request.getServletPath();
        switch (path) {
            case "/login":
                JsonReader jsonReader = Json.createReader(request.getReader());
                JsonObject jsonObject = jsonReader.readObject();
                String login = jsonObject.getString("login");
                String password = jsonObject.getString("password");
                if(login == null || login.isEmpty()
                        || password == null || password.isEmpty()){
                    job.add("info", "Нет такого пользователя или неправильный пароль");
                    job.add("status", false);
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
                    break;
                }
                User user = userFacade.findByLogin(login);
                if(user == null){
                    job.add("info", "Нет такого пользователя или неправильный пароль");
                    job.add("status", false);
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
                    break;
                }
                if(!user.getPassword().equals(pe.getProtectedPassword(password, user.getSalt()))){
                    job.add("info", "Нет такого пользователя или неправильный пароль");
                    job.add("status", false);
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
                    break;
                }
                HttpSession session = request.getSession(true);
                session.setAttribute("authUser", user);
                job.add("status", true);
                job.add("info", "Вы вошли как "+user.getLogin());
                job.add("user", new ConvertorToJson().getJsonObjectUser(user));
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/logout":
                session = request.getSession(false);
                if(session != null){
                    session.invalidate();
                    job.add("info", "Вы вышли");
                    job.add("status", true);
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
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
