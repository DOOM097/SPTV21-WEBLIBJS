/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import converters.ConvertorJsonToJava;
import converters.ConvertorToJson;
import entity.Author;
import entity.Book;
import entity.Cover;
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
import session.AuthorFacade;
import session.BookFacade;
import session.CoverFacade;
import session.UserFacade;
import tools.PasswordEncrypt;

/**
 *
 * @author user
 */
@WebServlet(name = "ManagerServlet",loadOnStartup = 1, urlPatterns = {
    "/createBook",    
    "/getListCovers",
})
public class ManagerServlet extends HttpServlet {
    @EJB private AuthorFacade authorFacade; 
    @EJB private BookFacade bookFacade; 
    @EJB private CoverFacade coverFacade; 
    public static enum role {ADMINISTRATOR, MANAGER, USER};
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
        if(!authUser.getRoles().contains(UserServlet.role.MANAGER.toString())){
            job.add("info", "У вас нет права. Авторизуйтесь как менеджер.");
            job.add("status", false);
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                return;
        }
        
        String path = request.getServletPath();
        switch (path) {
            case "/createBook":
                JsonReader jsonReader = Json.createReader(request.getReader());
                JsonObject jsonObject = jsonReader.readObject();
                job = Json.createObjectBuilder();
                Book book = new ConvertorJsonToJava().getBook(jsonObject, authorFacade,coverFacade);
                if(book == null){
                    job.add("info", "Не все поля заполнены");
                    job.add("status", false);
                    try (PrintWriter out = response.getWriter()) {
                        out.println(job.build().toString());
                    }
                    break;
                }
                try {
                    bookFacade.create(book);
                    for(int i = 0; i < book.getAuthors().size(); i++){
                        Author author = authorFacade.find(book.getAuthors().get(i).getId());
                        author.getBooks().add(book);
                        authorFacade.edit(author);
                    }
                    job.add("info", "Книга успешно создана");
                    job.add("status", true);
                } catch (Exception e) {
                    job.add("info", "Книгу создать не удолось");
                    job.add("status", false);
                }
                try (PrintWriter out = response.getWriter()) {
                    out.println(job.build().toString());
                }
                break;
            case "/getListCovers":
                List<Cover> listCovers = coverFacade.findAll();
                job.add("covers", new ConvertorToJson().getJACovers(listCovers));
                job.add("status", true);
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
