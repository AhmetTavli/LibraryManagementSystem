package makeup.project.design;

import makeup.project.software.*;
import makeup.project.software.Dao.UserDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

public class RecoveryForm extends JFrame {
    private JLabel labelEmail;
    private JTextField textEmail;
    private JButton buttonProceed;
    private JPanel panelRec;
    private JLabel labelMsg;
    private JComboBox boxDay;
    private JComboBox boxMonth;
    private JComboBox boxYear;
    private JLabel labelBirthday;
    private JRadioButton femaleRadioButton;
    private JRadioButton maleRadioButton;
    private JLabel labelNewPwd;
    private JLabel labelGender;
    private JButton buttonUpdate;
    private JPasswordField textPwd;
    private JLabel labelNew;

    private UserDao userDao;
    private User userObject;

    public RecoveryForm(Connection connection) {
        add(panelRec);
        setTitle("Recover Your Account");

        setSize(600, 300);

        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - 500) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - 200) / 2;

        this.setLocation(x, y);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        userDao = new UserDaoImpl();
        // manDao = new ManagerDaoImpl();

        labelMsg.setText("");

        fillComboBox();

        labelNew.setVisible(false);
        labelNewPwd.setVisible(false);
        textPwd.setVisible(false);
        buttonUpdate.setVisible(false);

        buttonProceed.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                labelMsg.setText("");
                userObject = userDao.getSingleUserByEmail(textEmail.getText(), connection);

                if (userObject.getEmail() == null) {
                    labelMsg.setForeground(Color.RED);
                    labelMsg.setText("User is not existed in the database.");
                } else {

                    // set birthday
                    String day = (String) boxDay.getSelectedItem();
                    String month = (String) boxMonth.getSelectedItem();
                    String year = (String) boxYear.getSelectedItem();
                    String birthday = day + "/" + month + "/" + year;

                    String gen = getGender();

                    boolean isEml = userObject.getEmail().equals(textEmail.getText());
                    boolean isBir = userObject.getBirthday().equals(birthday);
                    boolean isGen = userObject.getGender().equals(gen);

                    if (isEml && isBir && isGen) {
                        labelNew.setVisible(true);
                        labelNewPwd.setVisible(true);
                        textPwd.setVisible(true);
                        buttonUpdate.setVisible(true);
                    } else {
                        labelMsg.setForeground(Color.RED);
                        labelMsg.setText("Please check your credentials.");
                    }

                }

            }
        });
        buttonUpdate.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                userObject.setUserPassword(new String(textPwd.getPassword()));

                userDao.updateUserPwd(userObject, connection);
                labelMsg.setForeground(Color.GREEN);
                labelMsg.setText("Password successfully updated");
            }
        });
    }

    private void fillComboBox() {
        boxDay.setModel(new DefaultComboBoxModel(getDayArray().toArray()));
        boxMonth.setModel(new DefaultComboBoxModel(getMonthArray()));
        boxYear.setModel(new DefaultComboBoxModel(getYearArray().toArray()));
    }

    private ArrayList<String> getDayArray() {
        ArrayList<String> dayList = new ArrayList<>();

        for (int i = 0; i < 31; i++)
            dayList.add(String.valueOf(i + 1));

        return dayList;
    }

    private String[] getMonthArray() {
        return new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
    }

    private ArrayList<String> getYearArray() {
        ArrayList<String> yearList = new ArrayList<>();

        for (int i = 1900; i < 2010; i++)
            yearList.add(String.valueOf(i));

        return yearList;
    }

    private String getGender() {
        return femaleRadioButton.isSelected() ? "F" : "M";
    }
}
