import javax.swing.*;
import java.awt.*;

public class ProductView extends JFrame {

    public JTextField txtProductID = new JTextField(35);
    public JTextField txtProductName = new JTextField(35);
    public JTextField txtProductPrice = new JTextField(35);
    public JTextField txtProductQuantity = new JTextField(35);

    public JButton btnLoad = new JButton("Load");
    public JButton btnSave = new JButton("Save");

    public ProductView() {

        this.setTitle("Product View");
        this.setSize(new Dimension(425, 450));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));    // make this window with box layout

        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel idLabel = new JLabel("Product ID:");
        line1.add(idLabel);
        line1.add(txtProductID);
        line1.setBackground(new java.awt.Color(2, 250, 11));
        this.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Product Name:");
        line2.add(nameLabel);
        line2.add(txtProductName);
        line2.setBackground(new java.awt.Color(2, 250, 11));
        this.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel priceLabel = new JLabel("Price:");
        line3.add(priceLabel);
        line3.add(txtProductPrice);
        line3.setBackground(new java.awt.Color(2, 250, 11));
        this.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel quantityLabel = new JLabel("Quantity:");
        line4.add(quantityLabel);
        line4.add(txtProductQuantity);
        line4.setBackground(new java.awt.Color(2, 250, 11));
        this.getContentPane().add(line4);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new java.awt.Color(2, 250, 11));
        buttonPanel.add(btnLoad);
        buttonPanel.add(btnSave);

        this.getContentPane().add(buttonPanel);
    }

}
