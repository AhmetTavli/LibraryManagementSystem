package makeup.project.design;

import makeup.project.software.*;
import makeup.project.software.Dao.*;
import makeup.project.software.Book;
import makeup.project.software.HiredBook;
import makeup.project.software.UserMail;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Date;

public class ManagerForm extends JFrame {
    private JPanel pnlMan;
    private JPanel pnlOp;
    public JLabel greetManager;
    private JTabbedPane paneOp;
    private JLabel labelOp;
    private JPanel panelAdd;
    private JPanel panelRem;
    private JPanel panelRecall;
    private JPanel panelPen;
    private JPanel panelTran;
    private JLabel lblBookName;
    private JTextField txtBookName;
    private JLabel lblAuthor;
    private JTextField txtAuthor;
    private JLabel lblYear;
    private JTextField txtYear;
    private JLabel lblIsbn;
    private JTextField txtIsbn;
    private JLabel lblInfo;
    private JButton buttonAdd;
    private JScrollPane remScroll;
    private JTable tableRem;
    private JButton btnRem;
    private JTable tableReCall;
    private JTable tablePen;
    private JScrollPane panePen;
    private JTable tableReq;
    private JScrollPane paneReq;
    private JButton btnReCall;
    private JLabel lblReCallInfo;
    private JTable tableTA;
    private JButton btnTA;
    private JPanel panelTA;
    private JScrollPane paneTA;

    private Book book;
    private BookDao bookDao;
    private BLogDao logDao;
    private HBookDao hDao;
    private ReqDao rDao;
    private UserDao uDao;
    private UMailDao uMailDao;

    ManagerForm(Connection connection) {
        add(pnlMan);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setTitle("Manager Panel");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        lblInfo.setText("");
        lblReCallInfo.setVisible(false);

        bookDao = new BookDaoImpl();
        logDao = new BLogDaoImpl();
        hDao = new HBookDaoImpl();
        rDao = new ReqDaoImpl();
        uDao = new UserDaoImpl();
        uMailDao = new UMailDaoImpl();

        buttonAdd.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e : ActionEvent
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean fieldName = isFieldEmpty(txtBookName.getText());
                boolean fieldAuth = isFieldEmpty(txtAuthor.getText());
                boolean fieldYear = isFieldEmpty(txtYear.getText());
                boolean fieldISBN = isFieldEmpty(txtIsbn.getText());

                boolean isSingleFieldEmpty = fieldName | fieldAuth | fieldYear | fieldISBN;

                boolean isBookAvail = isBookInDb(txtIsbn.getText(), connection);

                if (isBookAvail) {
                    lblInfo.setForeground(Color.RED);
                    lblInfo.setText("Current book is available.");
                } else if (isSingleFieldEmpty) {
                    lblInfo.setForeground(Color.RED);
                    lblInfo.setText("Please fill all book information.");
                } else {
                    book = new Book();

                    book.setName(txtBookName.getText());
                    book.setAuthor(txtAuthor.getText());
                    book.setYear(Integer.valueOf(txtYear.getText()));
                    book.setIsbn(txtIsbn.getText());
                    book.setQuality("0");

                    bookDao.addBook(book, connection);

                    Date bookAdd = getDate();
                    Book getBook = bookDao.getSingleBookByISBN(txtIsbn.getText(), connection);
                    logDao.addLog(getBook.getId(), bookAdd, connection);

                    lblInfo.setForeground(Color.GREEN);
                    lblInfo.setText("Successfully Added to the database.");
                }
            }
        });

        paneOp.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                callTableRem(connection);
                callTableRecall(connection);
                callTablePen(connection);
                callTableReq(connection);
                callTableTA(connection);
                refresh();
            }
        });

        btnRem.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = tableRem.getSelectedRow();

                if (selRow != -1) {
                    String isbn = tableRem.getModel().getValueAt(selRow, 3).toString();
                    Book b = bookDao.getSingleBookByISBN(isbn, connection);
                    bookDao.remBook(b, connection);

                    callTableRem(connection);
                }
            }
        });
        btnReCall.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = tableReCall.getSelectedRow();

                if (selRow != -1) {
                    String isbn = tableReCall.getModel().getValueAt(selRow, 3).toString();
                    Book b = bookDao.getSingleBookByISBN(isbn, connection);
                    HiredBook u = hDao.getCheckedBookUser(b.getId(), connection);
                    User user = uDao.getUserById(u.getU_id(), connection);

                    UserMail um = new UserMail();

                    String body = "Please return Book " + b.getName() + " ("
                            + b.getIsbn() + ") immediately!";

                    um.setBody(body);
                    um.setMail_from("Manager");
                    um.setUser_id(user.getUserId());

                    uMailDao.sendEmail(um, connection);

                    lblReCallInfo.setVisible(true);
                    lblReCallInfo.setForeground(Color.GREEN);
                    lblReCallInfo.setText("User successfully informed.");
                } else {
                    lblReCallInfo.setVisible(true);
                    lblReCallInfo.setForeground(Color.RED);
                    lblReCallInfo.setText("Please select the book for recalling");
                }
            }
        });
        btnTA.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = tableTA.getSelectedRow();

                if (selRow != -1) {
                    String email = tableTA.getModel().getValueAt(selRow, 2).toString();
                    String isbn = tableTA.getModel().getValueAt(selRow, 5).toString();
                    User user = uDao.getSingleUserByEmail(email, connection);
                    Book book = bookDao.getSingleBookByISBN(isbn, connection);

                    HiredBook hiredBook = new HiredBook();

                    hiredBook.setU_id(user.getUserId());
                    hiredBook.setB_id(book.getId());
                    hiredBook.setAction_confirm("Y");

                    hDao.confirmBook(hiredBook, connection);

                    callTableTA(connection);
                }
            }
        });
    }

    private boolean isFieldEmpty(String text) {
        return text.equals("");
    }

    private boolean isBookInDb(String isbn, Connection con) {
        Book b = bookDao.getSingleBookByISBN(isbn, con);
        return b.getName() != null | b.getAuthor() != null | b.getYear() != 0 | b.getIsbn() != null;
    }

    private void refresh() {
        txtBookName.setText("");
        txtAuthor.setText("");
        txtYear.setText("");
        txtIsbn.setText("");
        lblInfo.setText("");
        lblReCallInfo.setText("");
    }

    private void callTableRem(Connection connection) {
        tableRem.setModel(DbUtils.resultSetToTableModel(bookDao.getAllBooksRS(connection)));
    }

    private void callTableRecall(Connection connection) {
        tableReCall.setModel(DbUtils.resultSetToTableModel(hDao.getLast5DayBooksRS(connection)));
    }

    private void callTablePen(Connection connection) {
        tablePen.setModel(DbUtils.resultSetToTableModel(hDao.getPenaltyRS(connection)));
    }

    private void callTableReq(Connection connection) {
        tableReq.setModel(DbUtils.resultSetToTableModel(rDao.displayReq(connection)));
    }

    /**
     * TA: TransAction synonym.
     *
     * @param connection: db connection
     */
    private void callTableTA(Connection connection) {
        tableTA.setModel(DbUtils.resultSetToTableModel(hDao.getUnConfirmBooksRS(connection)));
    }

    private Date getDate() {
        java.util.Date dObject = new java.util.Date();
        return new Date(dObject.getTime());
    }
}
