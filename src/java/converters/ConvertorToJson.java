

package converters;

import entity.Author;
import entity.Book;
import entity.User;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public class ConvertorToJson {
    JsonArrayBuilder jab = Json.createArrayBuilder();
    JsonObjectBuilder job = Json.createObjectBuilder();
    
    public JsonObject getJsonObjectUser(User user){
        job.add("id", user.getId());
        job.add("firstname", user.getFirstname());
        job.add("lastname", user.getLastname());
        job.add("phone", user.getPhone());
        job.add("login", user.getLogin());
        job.add("roles", getJsonArrayRoles(user.getRoles()));
        return job.build();
    }
    public JsonArray getJsonArrayRoles(List<String> roles){
        for (int i = 0; i < roles.size(); i++) {
            String role = roles.get(i);
            jab.add(role);
        }
        return jab.build();
    }
   
    public JsonArray getJsonArrayUsers(List<User>listUsers){
        for (int i = 0; i < listUsers.size(); i++) {
            User user = listUsers.get(i);
            jab.add(getJsonObjectUser(user));
        }
        return jab.build();
    }
    
    public JsonObject getJOAuthor(Author author){
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", author.getId());
        job.add("firstname", author.getFirstname());
        job.add("lastname", author.getLastname());
        job.add("birthYear", author.getBirthYear());
        List<Book> authorBooks = author.getBooks();
        job.add("books", getJABooksWithoutAuthors(authorBooks));
        return job.build();
    }
    public JsonArray getJABooksWithoutAuthors(List<Book> listBooks){
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        for (int i = 0; i < listBooks.size(); i++) {
            Book book = listBooks.get(i);
            job.add("bookName", book.getBookName());
            job.add("PublishedYear", book.getPublishedYear());
            job.add("quantity", book.getQuantity());
            jab.add(job.build());
        }
        return jab.build();
    }
    public JsonArray getJAAuthors(List<Author> authors){
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        for (int i = 0; i < authors.size(); i++) {
            job.add("id", authors.get(i).getId());
            job.add("firstname", authors.get(i).getFirstname());
            job.add("lastname", authors.get(i).getLastname());
            job.add("birthYear", authors.get(i).getBirthYear());
            job.add("books", getJABooksWithoutAuthors(authors.get(i).getBooks()));
            jab.add(job.build());
        }
        return jab.build();
    }
    public JsonObject getJOBook(Book book) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("bookName", book.getBookName());
        job.add("PublishedYear", book.getPublishedYear());
        job.add("quantity", book.getQuantity());
        job.add("authors", getJAAuthorsWithoutBooks(book.getAuthors()));
        return job.build();
    }
    public JsonArray getJABooks(List<Book> listBooks) {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        for (int i = 0; i < listBooks.size(); i++) {
            Book book = listBooks.get(i);
            job.add("bookName", book.getBookName());
            job.add("PublishedYear", book.getPublishedYear());
            job.add("quantity", book.getQuantity());
            job.add("authors", getJAAuthorsWithoutBooks(book.getAuthors()));
            jab.add(job.build());
        }
        return jab.build();
    }
    public JsonArray getJAAuthorsWithoutBooks(List<Author> authors){
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        for (int i = 0; i < authors.size(); i++) {
            job.add("id", authors.get(i).getId());
            job.add("firstname", authors.get(i).getFirstname());
            job.add("lastname", authors.get(i).getLastname());
            job.add("birthYear", authors.get(i).getBirthYear());
            jab.add(job.build());
        }
        return jab.build();
    }
}
