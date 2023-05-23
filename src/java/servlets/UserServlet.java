/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import converters.ConvertorJsonToJava;
import converters.ConvertorToJson;
import entity.Book;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.servlet.http.HttpSession;
import session.BookFacade;
import session.UserFacade;
import tools.PasswordEncrypt;

/**
 *
 * @author user
 */
@WebServlet(name = "UserServlet",loadOnStartup = 1, urlPatterns = {
    
    "/changeUserProfile",
    "/getBook",
    
    
})

public class UserServlet extends HttpServlet {
    @EJB private UserFacade userFacade; 
    @EJB private BookFacade bookFacade; 
    public static enum role {ADMINISTRATOR, MANAGER, USER};
    private PasswordEncrypt pe = new PasswordEncrypt();

    @Override
    public void init() throws ServletException {
        super.init(); 
        if(userFacade.count() > 0) return;
        User user = new User();
        user.setFirstname("Juri");
        user.setLastname("Melnikov");
        user.setPhone("5659394949");
        user.setLogin("Administrator");
        user.setSalt(pe.getSalt());
        user.setPassword(pe.getProtectedPassword("12345", user.getSalt()));
        user.getRoles().add(UserServlet.role.ADMINISTRATOR.toString());
        user.getRoles().add(UserServlet.role.MANAGER.toString());
        user.getRoles().add(UserServlet.role.USER.toString());
        userFacade.create(user);
    }
    
    public static boolean isRole(String role){
        for(int i=0;i<UserServlet.role.values().length;i++){
            if(UserServlet.role.values()[i].toString().equals(role)){
                return true;
            }
        }
        return false;
    }
    
    
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
            job.add("info", "Вы не аторизованы.");
            job.add("status", false);
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                return;
        }
        User authUser = (User) session.getAttribute("authUser");
        if(authUser == null){
            job.add("info", "Вы не аторизованы.");
            job.add("status", false);
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                return;
        }
        if(!authUser.getRoles().contains(UserServlet.role.USER.toString())){
            job.add("info", "У вас нет права. Авторизуйтесь как Администратор.");
            job.add("status", false);
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                return;
        }
        String path = request.getServletPath();
        switch (path) {
            case "/changeUserProfile":
                JsonReader jsonReader = Json.createReader(request.getReader());
                JsonObject jsonObject = jsonReader.readObject();
                job = Json.createObjectBuilder();
                User user = new ConvertorJsonToJava().getUser(jsonObject, userFacade);
                if(user == null){
                    job.add("info", "Не все поля заполнены");
                    job.add("status", false);
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
                    break;
                }
                try {
                    userFacade.edit(user);
                    //Состояние пользователя изменилось -> запоминаем его в сессии
                    session = request.getSession(false);
                    if(session.getAttribute("authUser")!= null ){
                        session.setAttribute("authUser", user);
                    }
                    job.add("info", "Профиль пользователя успешно изменнен");
                    job.add("user", new ConvertorToJson().getJsonObjectUser(user));
                    job.add("status", true);
                } catch (Exception e) {
                    job.add("info", "Профиль пользователя изменить не удолось");
                    job.add("status", false);
                }
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/getBook":
                String bookId = request.getParameter("bookId");
                Book book = bookFacade.find(Long.parseLong(bookId));
                job.add("status", true);
                job.add("book", new ConvertorToJson().getJOBook(book));
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
