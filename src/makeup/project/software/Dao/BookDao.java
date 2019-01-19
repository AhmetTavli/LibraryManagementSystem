package makeup.project.software.Dao;

import makeup.project.software.Book;

import java.sql.Connection;
import java.sql.ResultSet;

public interface BookDao {
    void addBook(Book book, Connection connection);

    void remBook(Book book, Connection connection);

    void rateBook(Book book, Connection connection);

    Book getSingleBookByISBN(String isbn, Connection connection);

    ResultSet getAllBooksRS(Connection connection);
}
