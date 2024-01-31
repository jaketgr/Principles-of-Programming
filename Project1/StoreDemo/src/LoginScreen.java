import javax.swing.*;

public class LoginScreen extends JFrame {
    private JTextField txtUserName = new JTextField(10);
    private JTextField txtPassword = new JTextField(10);
    private JButton    btnLogin    = new JButton("Login");

    public JButton getBtnLogin() {
        return btnLogin;
    }
// to login
    public JTextField getTxtPassword() {
        return txtPassword;
    }
//where to enter password
    public JTextField getTxtUserName() {
        return txtUserName;
    }
// where to enter unsername
    public LoginScreen() {


        this.setSize(300, 400);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        this.getContentPane().add(new JLabel ("Store Management System"));

        JPanel panelUserName = new JPanel();
        panelUserName.add(new JLabel("Username:"));
        panelUserName.add(txtUserName);
//space for username
        this.getContentPane().add(panelUserName);
        // space for password
        JPanel panelPassword = new JPanel();
        panelPassword.add(new JLabel("Password:"));
        panelPassword.add(txtPassword);

        this.getContentPane().add(panelPassword);

        this.getContentPane().add(btnLogin);
    }
}
