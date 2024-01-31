public class Product {
    private int productID;
    private String name;
    private double price;
    private double quantity;

    public double getPrice() {
        return price;
    }
// function for the price of the product
    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductID() {
        return productID;
    }
// function for the products ID
    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }
// function for the name of the product
    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }
// function for the number of the specific product
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
