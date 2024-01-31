public class OrderLine {

    private int productID;
    private int orderID;
    private double quantity;
    private double cost;

    public double getQuantity() {
        return quantity;
    }
// function for the quantity
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }
// function for the cost
    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getOrderID() {
        return orderID;
    }
// function for Order ID
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }
// function for the ID of the product
    public void setProductID(int productID) {
        this.productID = productID;
    }
}
