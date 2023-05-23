

package converters;

import entity.Author;
import entity.Book;
import entity.Cover;
import entity.User;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public class ConvertorToJson {
    
    public JsonObject getJsonObjectUser(User user){
        JsonObjectBuilder jobUser = Json.createObjectBuilder();
        jobUser.add("id", user.getId());
        jobUser.add("firstname", user.getFirstname());
        jobUser.add("lastname", user.getLastname());
        jobUser.add("phone", user.getPhone());
        jobUser.add("login", user.getLogin());
        jobUser.add("roles", getJsonArrayRoles(user.getRoles()));
        return jobUser.build();
    }
    public JsonArray getJsonArrayRoles(List<String> roles){
        JsonArrayBuilder jabRoles = Json.createArrayBuilder();
        for (int i = 0; i < roles.size(); i++) {
            String role = roles.get(i);
            jabRoles.add(role);
        }
        return jabRoles.build();
    }
   
    public JsonArray getJsonArrayUsers(List<User>listUsers){
        JsonArrayBuilder jabUsers = Json.createArrayBuilder();
        for (int i = 0; i < listUsers.size(); i++) {
            User user = listUsers.get(i);
            jabUsers.add(getJsonObjectUser(user));
        }
        return jabUsers.build();
    }
    
    public JsonObject getJOAuthor(Author author){
        JsonArrayBuilder jabAuthors = Json.createArrayBuilder();
        JsonObjectBuilder jobAuthor = Json.createObjectBuilder();
        jobAuthor.add("id", author.getId());
        jobAuthor.add("firstname", author.getFirstname());
        jobAuthor.add("lastname", author.getLastname());
        jobAuthor.add("birthYear", author.getBirthYear());
        List<Book> authorBooks = author.getBooks();
        jobAuthor.add("books", getJABooksWithoutAuthors(authorBooks));
        return jobAuthor.build();
    }
    public JsonArray getJABooksWithoutAuthors(List<Book> listBooks){
        JsonArrayBuilder jabBooksWithoutAuthors = Json.createArrayBuilder();
        JsonObjectBuilder jobBook = Json.createObjectBuilder();
        for (int i = 0; i < listBooks.size(); i++) {
            Book book = listBooks.get(i);
            jobBook.add("id", book.getId());
            jobBook.add("bookName", book.getBookName());
            jobBook.add("PublishedYear", book.getPublishedYear());
            jobBook.add("quantity", book.getQuantity());
            jabBooksWithoutAuthors.add(jobBook.build());
        }
        return jabBooksWithoutAuthors.build();
    }
    public JsonArray getJAAuthors(List<Author> authors){
        JsonArrayBuilder jabAuthors = Json.createArrayBuilder();
        JsonObjectBuilder jobAuthor = Json.createObjectBuilder();
        for (int i = 0; i < authors.size(); i++) {
            jobAuthor.add("id", authors.get(i).getId());
            jobAuthor.add("firstname", authors.get(i).getFirstname());
            jobAuthor.add("lastname", authors.get(i).getLastname());
            jobAuthor.add("birthYear", authors.get(i).getBirthYear());
            jobAuthor.add("books", getJABooksWithoutAuthors(authors.get(i).getBooks()));
            jabAuthors.add(jobAuthor.build());
        }
        return jabAuthors.build();
    }
    public JsonObject getJOBook(Book book) {
        JsonObjectBuilder jobBook = Json.createObjectBuilder();
        jobBook.add("id", book.getId());
        jobBook.add("bookName", book.getBookName());
        jobBook.add("PublishedYear", book.getPublishedYear());
        jobBook.add("quantity", book.getQuantity());
        jobBook.add("cover", getJOCover(book.getCover()));
        jobBook.add("authors", getJAAuthorsWithoutBooks(book.getAuthors()));
        return jobBook.build();
    }
    public JsonArray getJABooks(List<Book> listBooks) {
        JsonArrayBuilder jabBooks = Json.createArrayBuilder();
        JsonObjectBuilder jobBook = Json.createObjectBuilder();
        for (int i = 0; i < listBooks.size(); i++) {
            Book book = listBooks.get(i);
            jobBook.add("id", book.getId());
            jobBook.add("bookName", book.getBookName());
            jobBook.add("PublishedYear", book.getPublishedYear());
            jobBook.add("quantity", book.getQuantity());
            jobBook.add("authors", getJAAuthorsWithoutBooks(book.getAuthors()));
            jabBooks.add(jobBook.build());
        }
        return jabBooks.build();
    }
    public JsonArray getJAAuthorsWithoutBooks(List<Author> authors){
        JsonArrayBuilder jabAuthorsWithoutBooks = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        for (int i = 0; i < authors.size(); i++) {
            job.add("id", authors.get(i).getId());
            job.add("firstname", authors.get(i).getFirstname());
            job.add("lastname", authors.get(i).getLastname());
            job.add("birthYear", authors.get(i).getBirthYear());
            jabAuthorsWithoutBooks.add(job.build());
        }
        return jabAuthorsWithoutBooks.build();
    }
    public JsonObject getJOCover(Cover cover){
        JsonObjectBuilder jobCover = Json.createObjectBuilder();
        jobCover.add("id", cover.getId());
        jobCover.add("description", cover.getDescription());
        jobCover.add("url", cover.getUrl());
        return jobCover.build();
    }
    public JsonArray getJACovers(List<Cover>listCovers){
        JsonArrayBuilder jabCovers = Json.createArrayBuilder();
        JsonObjectBuilder jobCover = Json.createObjectBuilder();
        for(int i = 0; i<listCovers.size();i++){
            jabCovers.add(getJOCover(listCovers.get(i)));
        }
        return jabCovers.build();
    }
}
