import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    private JTextField txtUserName = new JTextField(30);
    private JPasswordField txtPassword = new JPasswordField(30);
    private JButton    btnLogin    = new JButton("Login");

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JTextField getTxtUserName() {
        return txtUserName;
    }

    public LoginScreen() {


        this.setSize(375, 300);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        JLabel title = new JLabel ("Jacob's Store Management System", SwingConstants.LEFT);
        title.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        this.getContentPane().add(title);

        JPanel panelUserName = new JPanel();
        panelUserName.add(new JLabel("Username:"));
        txtUserName.setBounds(100,20,165,25);
        panelUserName.add(txtUserName);
        panelUserName.setBackground(new java.awt.Color(22, 132, 243));
        this.getContentPane().add(panelUserName);

        JPanel panelPassword = new JPanel();
        panelPassword.add(new JLabel("Password:"));
        txtPassword.setBounds(10,50,80,25);
        panelPassword.add(txtPassword);
        panelPassword.setBackground(new java.awt.Color(246, 59, 6));
        this.getContentPane().add(panelPassword);

        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelLogin.add(btnLogin);
        panelLogin.setBackground(new java.awt.Color(22, 132, 243));
        this.getContentPane().add(panelLogin);

        getContentPane().setBackground(new java.awt.Color(246, 59, 6));
    }
}
