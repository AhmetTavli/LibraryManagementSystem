package makeup.project.software;

import makeup.project.software.Book;
import makeup.project.software.Dao.BookDao;

import javax.swing.*;
import java.sql.*;

public class BookDaoImpl implements BookDao {
    public void addBook(Book book, Connection connection) {
        String que = "insert into book (b_name, author, b_year, isbn, quality) " +
                "values(?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(que);
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.setInt(3, book.getYear());
            statement.setString(4, book.getIsbn());
            statement.setString(5, book.getQuality());
            statement.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't add book. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void remBook(Book book, Connection connection) {
        String que = "delete from book where book_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(que);
            statement.setInt(1, book.getId());
            statement.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't remove book. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void rateBook(Book book, Connection connection) {
        String que = "update book set quality = ? where book_id = ?";
        try {
            PreparedStatement stat = connection.prepareStatement(que);
            stat.setString(1, book.getQuality());
            stat.setInt(2, book.getId());
            stat.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't rate book. ",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public Book getSingleBookByISBN(String isbn, Connection connection) {
        Book b = new Book();

        String que = "select * from book where isbn = '" + isbn + "'";

        try {
            Statement stat = connection.createStatement();
            ResultSet rSet = stat.executeQuery(que);

            while (rSet.next()) {
                b = getBookValFromSet(rSet);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't get book.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return b;
    }

    @Override
    public ResultSet getAllBooksRS(Connection connection) {
        String que = "select b_name as 'Book Name', author as 'Author', b_year as 'Year of Book Published'," +
                "isbn as 'ISBN', quality as 'User Rating on Book Quality' from book";

        ResultSet rSet = null;

        try {
            Statement stat = connection.createStatement();
            rSet = stat.executeQuery(que);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error." +
                            "\nCan't get books result-set.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return rSet;
    }

    private Book getBookValFromSet(ResultSet set) {
        Book b = new Book();

        try {
            int book_id = set.getInt("book_id");
            String b_name = set.getString("b_name");
            String author = set.getString("author");
            int b_year = set.getInt("b_year");
            String isbn = set.getString("isbn");
            String quality = set.getString("quality");

            b.setId(book_id);
            b.setName(b_name);
            b.setAuthor(author);
            b.setYear(b_year);
            b.setIsbn(isbn);
            b.setQuality(quality);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "\nSql Statement Error. \nCan't get book object.",
                    "Dialog", JOptionPane.ERROR_MESSAGE);
        }

        return b;
    }
}
