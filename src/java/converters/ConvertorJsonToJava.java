

package converters;

import entity.Author;
import entity.Book;
import entity.User;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import servlets.UserServlet;
import session.AuthorFacade;
import session.BookFacade;
import session.CoverFacade;
import session.UserFacade;
import tools.PasswordEncrypt;


public class ConvertorJsonToJava {
    private PasswordEncrypt pe = new PasswordEncrypt();

    public User getUser(JsonObject jsonObject){
        JsonObjectBuilder job = Json.createObjectBuilder();
                String firstname = jsonObject.getString("firstname");
                String lastname = jsonObject.getString("lastname");
                String phone = jsonObject.getString("phone");
                String login = jsonObject.getString("login");
                String password = jsonObject.getString("password");
                if(firstname == null || firstname.isEmpty() 
                        || lastname == null || lastname.isEmpty() 
                        || phone == null || phone.isEmpty()
                        || login == null || login.isEmpty()
                        || password == null || password.isEmpty()){
                    return null;
                }
                User user = new User();
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setPhone(phone);
                user.setLogin(login);
                user.setSalt(pe.getSalt());
                password = pe.getProtectedPassword(password, user.getSalt());
                user.setPassword(password);
                user.getRoles().add(UserServlet.role.USER.toString());
                return user;
    }
    public User getUser(JsonObject jsonObject,UserFacade userFacade){
        String userId = jsonObject.getString("userId");
        String firstname = jsonObject.getString("firstname");
        String lastname = jsonObject.getString("lastname");
        String phone = jsonObject.getString("phone");
        String login = jsonObject.getString("login");
        String password = jsonObject.getString("password");
        if(firstname == null || firstname.isEmpty() 
                || lastname == null || lastname.isEmpty() 
                || phone == null || phone.isEmpty()
                || login == null || login.isEmpty()
                || userId == null || userId.isEmpty()){
            return null;
        }
        // Находим по идентификатору пользователя в базе и инициируем новыми значениями
        User user = userFacade.find(Long.parseLong(userId));
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPhone(phone);
        user.setLogin(login);
        if(password != null && !password.isEmpty()){
            //если пароль заполнен, то меняем на указанный
            password = pe.getProtectedPassword(password, user.getSalt());
            user.setPassword(password);
        }
        return user;
    }

    public Author getAuthor(JsonObject jsonObject, BookFacade bookFacade) {
        
        String firstname = jsonObject.getString("firstname");
        String lastname = jsonObject.getString("lastname");
        String birthYear = jsonObject.getString("birthYear");
        Author author = new Author();
        author.setBirthYear(Integer.parseInt(birthYear));
        author.setFirstname(firstname);
        author.setLastname(lastname);
        JsonArray jsonArrayBooksId = null;
        try {
            jsonArrayBooksId = jsonObject.getJsonArray("selectedBooks");
        } catch (Exception e) {
           // jsonArrayBooksId = [];
        }
        if(jsonArrayBooksId != null){
            for (int i = 0; i < jsonArrayBooksId.size(); i++) {
                 author.getBooks().add(bookFacade.find(Long.parseLong(jsonArrayBooksId.get(i).toString())));
            }
        }
        return author;
        
    }

   

    public Book getBook(JsonObject jsonObject, AuthorFacade authorFacade, CoverFacade coverFacade) {
        String bookName = jsonObject.getString("bookName","");
        String publishedYear = jsonObject.getString("publishedYear","");
        String quantity = jsonObject.getString("quantity","");
        String cover = jsonObject.getString("cover","");
        
        Book book = new Book();
        book.setBookName(bookName);
        book.setPublishedYear(Integer.parseInt(publishedYear));
        book.setQuantity(Integer.parseInt(quantity));
        book.setCover(coverFacade.find(Long.parseLong(cover)));
        JsonArray selectedAuthors = null;
        selectedAuthors = jsonObject.getJsonArray("authors");
        if(selectedAuthors != null){
            for (int i = 0; i < selectedAuthors.size(); i++) {
                JsonString key = selectedAuthors.getJsonString(i);
                String strKey = key.getString();
                 book.getAuthors().add(authorFacade.find(Long.parseLong(strKey)));
            }
        }
        
        return book;
    }
    
}
