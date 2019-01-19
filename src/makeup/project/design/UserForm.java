package makeup.project.design;

import makeup.project.software.*;
import makeup.project.software.Dao.*;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserForm extends JFrame {
    private JPanel panelUser;
    public JLabel greetUser;
    private JPanel pnlOp;
    private JLabel labelOp;
    private JTabbedPane paneOp;
    private JPanel panelUpd;
    private JCheckBox chkSub;
    private JLabel labelGreet;
    private JTable tableUpd;
    private JScrollPane paneUpd;
    private JLabel lblInfo;
    private JLabel labelTitle;
    private JTable tableBrow;
    private JPanel paneBrowse;
    private JScrollPane paneBrow;
    private JButton btnCheck;
    private JButton btnRateBook;
    private JLabel lblRateCheck;
    private JPanel paneProcess;
    private JTable tableProc;
    private JPanel pnlReq;
    private JLabel lblName;
    private JTextField txtName;
    private JLabel lblAuthor;
    private JTextField txtAut;
    private JLabel lblType;
    private JComboBox boxType;
    private JLabel lblComment;
    private JTextArea comArea;
    private JButton btnReq;
    private JLabel lblReqInfo;
    private JButton btnCancel;
    private JLabel lblCancelInfo;
    private JTable tableMail;
    private JPanel panelMail;
    private JScrollPane paneMail;
    private JButton btnDel;

    private User user;
    private Request req;
    private BookDao bDao;
    private BookLog uLog;
    private UserDao uDao;
    private BLogDao bLog;
    private HBookDao hDao;
    private HiredBook hBook;
    private ReqDao rDao;
    private UMailDao umDao;

    private Calendar cal;
    private java.util.Date retDate;

    UserForm(Connection connection, int u_id) {
        add(panelUser);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setTitle("User Panel");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        unsubSit();

        cal = Calendar.getInstance();
        retDate = new java.util.Date();

        lblInfo.setVisible(false);
        lblReqInfo.setVisible(false);
        lblRateCheck.setVisible(false);

        bDao = new BookDaoImpl();
        uDao = new UserDaoImpl();
        bLog = new BLogDaoImpl();
        hDao = new HBookDaoImpl();
        rDao = new ReqDaoImpl();
        umDao = new UMailDaoImpl();

        user = uDao.getUserById(u_id, connection);
        uLog = bLog.getLog(user, connection);

        if (checkUserSub(connection, u_id)) {
            subSit();
            callTableUpd(u_id, connection);
        }

        chkSub.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!chkSub.isSelected()) {
                    bLog.unsub2Upd(u_id, connection);
                    unsubSit();
                    inform("Unsubscribed from the Updates", false);
                } else {
                    inform("Subscribed to the Updates", true);

                    if (!uLog.isSubscribed())
                        bLog.subUser2Upd(u_id, connection);
                    subSit();
                    callTableUpd(u_id, connection);
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
                lblRateCheck.setText("");
                lblCancelInfo.setText("");
                callTableBrow(connection); // Browse-pane
                callTableProc(u_id, connection); // Process-pane
                callTableEmail(u_id, connection); // Mail
                fillItemBox(); // Request-pane
            }
        });
        btnRateBook.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = tableBrow.getSelectedRow();

                if (selRow != -1) {
                    String isbn = tableBrow.getModel().getValueAt(selRow, 3).toString();
                    Book b = bDao.getSingleBookByISBN(isbn, connection);

                    String qua = tableBrow.getModel().getValueAt(selRow, 4).toString();
                    String userQua = JOptionPane.showInputDialog("Quality: ");
                    int newQua = (Integer.valueOf(qua) + Integer.valueOf(userQua)) / 2;
                    JOptionPane.showMessageDialog(new JFrame(), "The new Quality value based on all rankings "
                            + newQua, "Dialog", JOptionPane.INFORMATION_MESSAGE);

                    b.setQuality(String.valueOf(newQua));
                    bDao.rateBook(b, connection);

                    callTableBrow(connection);

                    lblRateCheck.setVisible(true);
                    lblRateCheck.setForeground(Color.GREEN);
                    lblRateCheck.setText("Thank you for rating the book");
                } else {
                    lblRateCheck.setVisible(true);
                    lblRateCheck.setForeground(Color.RED);
                    lblRateCheck.setText("Please select the book from the table");
                }
            }
        });
        btnCheck.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = tableBrow.getSelectedRow();

                if (selRow != -1) {
                    String isbn = tableBrow.getModel().getValueAt(selRow, 3).toString();
                    Book b = bDao.getSingleBookByISBN(isbn, connection);
                    Date curDate = getDate();
                    Date retDate = returnDate(curDate);
                    int timeLeft = calcLeftTime(curDate, retDate);
                    String pen = calcPen(timeLeft);

                    hBook = new HiredBook();

                    hBook.setU_id(u_id);
                    hBook.setB_id(b.getId());
                    hBook.setH_date(curDate); // hired-date
                    hBook.setH_return(retDate); // return-date
                    hBook.setTimeLeft(Math.abs(timeLeft));
                    hBook.setPenalty(pen);
                    hBook.setAction_confirm("N"); // Not confirmed

                    hDao.checkBook(hBook, connection);
                } else {
                    lblRateCheck.setVisible(true);
                    lblRateCheck.setForeground(Color.RED);
                    lblRateCheck.setText("Please select the book from the table");
                }
                lblRateCheck.setVisible(true);
                lblRateCheck.setForeground(Color.GREEN);
                lblRateCheck.setText("Check-in completed. Now check-in operation must be approved by the manager.");
            }
        });
        btnReq.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                req = new Request();

                boolean isFieldNull = txtName.getText() == null | txtAut.getText() == null;

                if (!isFieldNull) {

                    req.setU_id(u_id);
                    req.setI_name(txtName.getText());
                    req.setAuthor(txtAut.getText());
                    req.setI_type((String) boxType.getSelectedItem());
                    req.setU_comment(comArea.getText());

                    rDao.sendReq(req, connection);

                    lblReqInfo.setVisible(true);
                    lblReqInfo.setForeground(Color.GREEN);
                    lblReqInfo.setText("The request successfully send to the manager.");
                } else {
                    lblReqInfo.setVisible(true);
                    lblReqInfo.setForeground(Color.RED);
                    lblReqInfo.setText("Please fill name and author fields.");
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = tableProc.getSelectedRow();

                if (selRow != -1) {
                    String isbn = tableProc.getModel().getValueAt(selRow, 3).toString();
                    Book b = bDao.getSingleBookByISBN(isbn, connection);
                    HiredBook hBook = hDao.getCheckinDetail(u_id, b.getId(), connection);
                    if (hBook.getAction_confirm().equals("Y")) {
                        lblCancelInfo.setForeground(Color.RED);
                        lblCancelInfo.setText("Can't remove the process, manager already confirmed the action.");
                    } else {
                        hDao.remCheck(u_id, b.getId(), connection);
                        lblCancelInfo.setForeground(Color.GREEN);
                        lblCancelInfo.setText("Process successfully removed.");

                        callTableProc(u_id, connection);
                    }
                } else {
                    lblCancelInfo.setForeground(Color.RED);
                    lblCancelInfo.setText("Please select the item from the table.");
                }
            }
        });
        btnDel.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int selRow = tableMail.getSelectedRow();

                if (selRow != -1) {
                    UserMail uMail = new UserMail();

                    uMail.setUser_id(u_id);
                    umDao.delEmail(uMail, connection);
                    callTableEmail(u_id, connection);
                }
            }
        });
    }

    private boolean checkUserSub(Connection con, int u_id) {
        User u = uDao.getUserById(u_id, con);

        if (u.getUserId() != 0) {
            BookLog bl = bLog.getLog(u, con);
            chkSub.setSelected(bl.isSubscribed());
            return bl.isSubscribed();
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Can't get user from the db", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Unsubscribe situation
     */
    private void unsubSit() {
        lblInfo.setText("");
        lblInfo.setVisible(false);
        tableUpd.setVisible(false);
        labelTitle.setVisible(false);
    }

    /**
     * Subscribe situation
     */
    private void subSit() {
        lblInfo.setText("");
        lblInfo.setVisible(true);
        tableUpd.setVisible(true);
        labelTitle.setVisible(true);
    }

    /**
     * Update information
     *
     * @param info: current information
     * @param pos:  positive
     */
    private void inform(String info, boolean pos) {
        lblInfo.setVisible(true);
        if (pos) {
            tableUpd.setVisible(true);
            lblInfo.setForeground(Color.GREEN);
        } else {
            tableUpd.setVisible(false);
            lblInfo.setForeground(Color.RED);
        }
        lblInfo.setText(info);
    }

    private void callTableBrow(Connection connection) {
        tableBrow.setModel(DbUtils.resultSetToTableModel(bDao.getAllBooksRS(connection)));
    }

    private void callTableProc(int u_id, Connection connection) {
        tableProc.setModel(DbUtils.resultSetToTableModel(hDao.getUserCheckRS(u_id, connection)));
    }

    private void callTableUpd(int u_id, Connection connection) {
        tableUpd.setModel(DbUtils.resultSetToTableModel(bLog.getLogRS(u_id, connection)));
    }

    private void callTableEmail(int u_id, Connection connection) {
        tableMail.setModel(DbUtils.resultSetToTableModel(umDao.getEmail(u_id, connection)));
    }

    private Date getDate() {
        java.util.Date dObject = new java.util.Date();
        return new Date(dObject.getTime());
    }

    private Date returnDate(Date inDate) {
        Date dObj = Date.valueOf(inDate.toLocalDate());
        int[] dArr = getDayMonthYear(dObj);
        // give 1-month time
        dArr[1] += 1;
        // turn new d-m-y to sql.date
        String rDate = dArr[2] + "-" + dArr[1] + "-" + dArr[0];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            retDate = format.parse(rDate);
        } catch (ParseException e) {
            lblRateCheck.setVisible(true);
            lblRateCheck.setForeground(Color.RED);
            lblRateCheck.setText("Problem in return date calculation.");
        }

        return new Date(retDate.getTime());
    }

    private int calcLeftTime(Date inDate, Date outDate) {
        Date in = Date.valueOf(inDate.toLocalDate());
        Date out = Date.valueOf(outDate.toLocalDate());

        int[] inArr = getDayMonthYear(in);
        int[] outArr = getDayMonthYear(out);

        int t_left = (outArr[1] - inArr[1]) * 30 + outArr[0] - (inArr[0] - 30);

        return t_left;
    }

    private int[] getDayMonthYear(Date dObj) {
        cal.setTime(dObj);

        int[] A = new int[3];
        // extract day-month-year
        A[0] = cal.get(Calendar.DAY_OF_MONTH);
        A[1] = cal.get(Calendar.MONTH) + 1; // month ordered from 0-11, where 0:Jan 11: Feb
        A[2] = cal.get(Calendar.YEAR);

        return A;
    }

    private String calcPen(int time) {
        int pen = 0; // penalty

        if (time < 0) {
            pen = Math.abs(time);
            return String.valueOf(pen) + " £";
        } else
            return "0 £";
    }

    private void fillItemBox() {
        boxType.setModel(new DefaultComboBoxModel(getReqItems()));
    }

    private String[] getReqItems() {
        return new String[]{"Book", "Journal", "Comic"};
    }
}
