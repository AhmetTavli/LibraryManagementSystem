package makeup.project.design;

import makeup.project.software.*;
import makeup.project.software.Dao.ManDao;
import makeup.project.software.Dao.UserDao;
import makeup.project.software.ManDaoImpl;
import makeup.project.software.Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

public class Home extends JFrame {
    private JPanel panelMain;
    private JPanel panelCreate;

    private JLabel labelEmail;
    private JLabel labelPassword;
    private JLabel labelLibrary;
    private JLabel labelCreate;
    private JLabel labelBirthday;
    private JLabel labelName;
    private JLabel labelSurname;
    private JLabel labelEmailAddress;
    private JLabel labelUserPassword;
    private JLabel labelGender;
    private JLabel labelRole;
    private JLabel labelNewAccount;
    private JLabel labelAccount;

    private JButton buttonLogin;
    private JButton buttonForgot;
    private JButton buttonSignUp;

    private JPasswordField textPassword;
    private JPasswordField passwordField;

    private JTextField textEmail;
    private JTextField textName;
    private JTextField textSurname;
    private JTextField textNewEmail;
    private JTextField textNewPassword;

    private JComboBox boxDay;
    private JComboBox boxMonth;
    private JComboBox boxYear;

    private JRadioButton radioFemale;
    private JRadioButton radioMale;
    private JRadioButton radioManager;
    private JRadioButton radioUser;

    private String day;
    private String month;
    private String year;
    private String birthday;
    private String gender;
    private String role;

    private User user;

    private UserDao userDao;
    private ManDao managerDao;

    private static final Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    public Home(Connection connection) {
        add(panelMain);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setTitle("Welcome to the Library Management System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // debug purpose
        // -manager
        // textEmail.setText("ahmet.tavli89@gmail.com");
        // textPassword.setText("123");
        // -user
        //textEmail.setText("taner@yilmaz.com");
        //textPassword.setText("tanercik");

        //label
        labelNewAccount.setText("");
        labelAccount.setText("");

        fillComboBox();

        initBirthday();

        userDao = new UserDaoImpl();
        managerDao = new ManDaoImpl();

        buttonSignUp.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * There are 3 constraints for sign-up.
             * Constraint #1: No field is empty
             * Constraint #2: Valid email-address
             * Constraint #3: Role
             *
             * If current users email address is available in the manager_accept database,
             * then current user can select Manager role.
             *
             * @param e : exception
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // constraint satisfied => true
                boolean constraint1 = checkConstraint1();
                boolean constraint2 = checkConstraint2(connection);
                boolean constraint3 = checkConstraint3(connection);

                if (constraint1 && constraint2 && constraint3) {
                    user = new User();

                    day = (String) boxDay.getSelectedItem();
                    month = (String) boxMonth.getSelectedItem();
                    year = (String) boxYear.getSelectedItem();
                    birthday = day + "/" + month + "/" + year;

                    gender = getGender();

                    user.setFirstName(textName.getText());
                    user.setSurname(textSurname.getText());
                    user.setEmail(textNewEmail.getText());
                    user.setUserPassword(new String(passwordField.getPassword()));
                    user.setGender(gender);
                    user.setBirthday(birthday);
                    user.setRole(role);

                    userDao.addUser(user, connection);

                    labelNewAccount.setForeground(Color.GREEN);
                    labelNewAccount.setText("The new account has been successfully created.");

                    refresh();
                }
            }
        });

        buttonLogin.addActionListener(new ActionListener() {
            /**
             *
             * There are 2 constraint validations for log-in.
             * Constraint #1: Email
             * Constraint #2: Password
             *
             * @param e : exception
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!nullEmail()) {
                    char[] u_pwd = textPassword.getPassword();

                    User user = userDao.getSingleUserByEmail(textEmail.getText(), new String(u_pwd), connection);
                    Manager man = managerDao.getManagerByEmail(textEmail.getText(), connection);

                    boolean isUser = user.getEmail() != null && man.getEmail() == null;
                    boolean isMan = user.getEmail() != null && man.getEmail() != null;

                    if (isUser) {
                        UserForm userForm = new UserForm(connection, user.getUserId());
                        userForm.greetUser.setText(greetingText(user));
                        userForm.setVisible(true);
                    } else if (isMan) {
                        ManagerForm managerForm = new ManagerForm(connection);
                        managerForm.greetManager.setText(greetingText(user));
                        managerForm.setVisible(true);
                    } else {
                        // Who is this guy?
                        labelAccount.setForeground(Color.RED);
                        labelAccount.setText("Can't find account on the database.");
                    }
                } else {
                    labelAccount.setForeground(Color.RED);
                    labelAccount.setText("Both email and passwords fields cannot be empty.");
                }
            }
        });
        textPassword.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                textPassword.setText("");
            }
        });
        buttonForgot.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();

                RecoveryForm recoveryForm = new RecoveryForm(connection);
                recoveryForm.setAlwaysOnTop(true);
                recoveryForm.setVisible(true);
            }
        });
        textEmail.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                labelAccount.setText("");
            }
        });
        textPassword.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                labelAccount.setText("");
            }
        });
    }

    private ArrayList<String> getDayArray() {
        ArrayList<String> dayList = new ArrayList<>();

        for (int i = 0; i < 30; i++)
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
        return radioFemale.isSelected() ? "F" : "M";
    }

    private String getRole() {
        return radioManager.isSelected() ? "M" : "U";
    }

    private boolean checkConstraint1() {
        boolean result = false;

        if (textName.getText().isEmpty()) {
            labelNewAccount.setForeground(Color.RED);
            labelNewAccount.setText("Name field can't be empty.");
        } else if (textSurname.getText().isEmpty()) {
            labelNewAccount.setForeground(Color.RED);
            labelNewAccount.setText("Surname field can't be empty.");
        } else if (textNewEmail.getText().isEmpty()) {
            labelNewAccount.setForeground(Color.RED);
            labelNewAccount.setText("Email field can't be empty.");
        } else
            result = true;

        return result;
    }

    /**
     * @return if valid email address true, else false.
     */
    private boolean checkConstraint2(Connection connection) {
        boolean emailConstraint = true;

        Matcher matcher = emailPattern.matcher(textNewEmail.getText());

        // check if user already signed up.
        User user = userDao.getSingleUserByEmail(textNewEmail.getText(), connection);

        labelNewAccount.setForeground(Color.RED);
        if (user.getEmail() != null) {
            labelNewAccount.setText("The user is existed in the database." +
                    " You can click on Forgot Password button for recovering your account.");

            emailConstraint = false;
        } else if (!matcher.find()) {
            labelNewAccount.setText("Email address is not valid.");

            emailConstraint = false;
        }

        if (radioManager.isSelected()) {
            // check if user email is valid in the manager db
            Manager man = managerDao.getManagerByEmail(textNewEmail.getText(), connection);

            if (man.getEmail() == null) {
                labelNewAccount.setText("User role cannot be manager. The email address was not found in database");

                emailConstraint = false;
            }
        }

        return emailConstraint;
    }

    private boolean checkConstraint3(Connection connection) {
        boolean result = false;

        role = getRole();

        if (role.equals("M")) {
            ArrayList<Manager> managerEmails = managerDao.getAllManagers(connection);

            // search current users email in the managers email
            for (Manager managerObject : managerEmails)
                if (managerObject.getEmail().equals(textNewEmail.getText()))
                    result = true;

            if (!result) {
                labelNewAccount.setForeground(Color.RED);
                labelNewAccount.setText("Current user role can't be Manager.");
            }
        } else if (role.equals("U"))
            result = true;

        return result;
    }

    private void fillComboBox() {
        boxDay.setModel(new DefaultComboBoxModel(getDayArray().toArray()));
        boxMonth.setModel(new DefaultComboBoxModel(getMonthArray()));
        boxYear.setModel(new DefaultComboBoxModel(getYearArray().toArray()));
    }

    private void initBirthday() {
        boxDay.setSelectedItem(0);
        boxMonth.setSelectedItem(0);
        boxYear.setSelectedItem(0);
    }

    private void refresh() {
        textName.setText("");
        textSurname.setText("");
        textNewEmail.setText("");
        passwordField.setText("");

        initBirthday();
    }

    private String greetingText(User user) {
        labelAccount.setText("");

        String greet = "Hello, ";

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        greet += user.getGender().equals("M") ? "Mr. " : "Ms. ";
        greet += user.getSurname() + "  ";
        greet += simpleDateFormat.format(date);

        return greet;
    }

    private boolean nullEmail() {
        return textEmail.getText().equals("");
    }
}
