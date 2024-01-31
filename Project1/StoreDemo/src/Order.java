import java.sql.Date;
import java.util.List;
import java.util.ArrayList;


public class Order {
    private int orderID;
    private String customerName;
    private double totalCost;
    private double totalTax;
    private Date date;

    private List<OrderLine> lines;

    public Order() {
        lines = new ArrayList<>();
    }

    public Date getDate() {
        return date;
    }
//function for the date
    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalCost() {
        return totalCost;
    }
//function for the cost
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalTax() {
        return totalTax;
    }
//function for the total tax
    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public int getOrderID() {
        return orderID;
    }
//function for the order ID
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getCustomerName() {
        return customerName;
    }
//function for the customer name
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void addLine(OrderLine line) {
        lines.add(line);
    }

    public void removeLine(OrderLine line) {
        lines.remove(line);
    }

    public List<OrderLine> getLines() {
        return lines;
    }
}
