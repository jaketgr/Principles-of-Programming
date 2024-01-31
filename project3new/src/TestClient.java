import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class TestClient {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        int choice;
        //first ask for service from Registry
        Socket socket = new Socket("localhost", 5000);

        ServiceMessageModel req = new ServiceMessageModel();
        req.code = ServiceMessageModel.SERVICE_DISCOVER_REQUEST;

        do {
            System.out.print("Options:\n"
                    + "\t1: Product Info Service\n"
                    + "\t2: Order Info Service\n"
                    + "\t3: Product Price Update Service\n"
                    + "\t4: Product Quantity Update Service\n"
                    + "\t5: User Login Info Service\n"
                    + "\t6: User Order Permissions Service\n"
                    + "Enter a microservice to test: ");
            choice = Integer.parseInt(input.next().trim());
            switch (choice) {
                case ServiceInfoModel.PRODUCT_INFO_SERVICE:
                    req.data = String.valueOf(ServiceInfoModel.PRODUCT_INFO_SERVICE);
                    break;
                case ServiceInfoModel.ORDER_INFO_SERVICE:
                    req.data = String.valueOf(ServiceInfoModel.ORDER_INFO_SERVICE);
                    break;
                case ServiceInfoModel.PRODUCT_PRICE_UPDATE_SERVICE:
                    req.data = String.valueOf(ServiceInfoModel.PRODUCT_PRICE_UPDATE_SERVICE);
                    break;
                case ServiceInfoModel.PRODUCT_QUANTITY_UPDATE_SERVICE:
                    req.data = String.valueOf(ServiceInfoModel.PRODUCT_QUANTITY_UPDATE_SERVICE);
                    break;
                case ServiceInfoModel.AUTH_LOGIN_SERVICE:
                    req.data = String.valueOf(ServiceInfoModel.AUTH_LOGIN_SERVICE);
                    break;
                case ServiceInfoModel.USER_PERMS_SERVICE:
                    req.data = String.valueOf(ServiceInfoModel.USER_PERMS_SERVICE);
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (req.data == null);
        Gson gson = new Gson();

        DataOutputStream printer = new DataOutputStream(socket.getOutputStream());
        printer.writeUTF(gson.toJson(req));
        printer.flush();

        DataInputStream reader = new DataInputStream(socket.getInputStream());
        String msg = reader.readUTF();

        System.out.println("Message from server: " + msg);

        printer.close();
        reader.close();
        socket.close();

        ServiceMessageModel res = gson.fromJson(msg, ServiceMessageModel.class);

        if (res.code == ServiceMessageModel.SERVICE_DISCOVER_OK) {
            System.out.println(res.data);
        }
        else {
            System.out.println("Service not found");
            return;
        }

        ServiceInfoModel info = gson.fromJson(res.data, ServiceInfoModel.class);
        System.out.println("Address: " + info.serviceHostAddress + ", Port: " + info.serviceHostPort);

        Socket socket2 = new Socket(info.serviceHostAddress, info.serviceHostPort);
        DataOutputStream printer2 = new DataOutputStream(socket2.getOutputStream());
        DataInputStream reader2 = new DataInputStream(socket2.getInputStream());
        DecimalFormat df = new DecimalFormat("$#,##0.00");
        String modelmsg, username;
        int id;
        Product product;

        switch (info.serviceCode)
        {
            case ServiceInfoModel.PRODUCT_INFO_SERVICE:
                //now read user input for product ID
                System.out.print("Enter a Product ID to look up: ");
                id = Integer.parseInt(input.next().trim());
                modelmsg = gson.toJson(id);
                printer2.writeUTF(modelmsg);
                printer2.flush();

                modelmsg = reader2.readUTF();
                product = gson.fromJson(modelmsg, Product.class);
                if (product != null) {
                System.out.println("Product ID: " + product.getProductID()
                        + "\nName: " + product.getName()
                        + "\nPrice: " + product.getPrice()
                        + "\nQuantity: " + product.getQuantity());
                } else { System.out.println("A product with that ID was not found in the database."); }
                break;
            case ServiceInfoModel.ORDER_INFO_SERVICE:
                System.out.print("Enter an ID to look up: ");
                id = Integer.parseInt(input.next().trim());
                modelmsg = gson.toJson(id);
                printer2.writeUTF(modelmsg);
                printer2.flush();

                modelmsg = reader2.readUTF();
                Order order = gson.fromJson(modelmsg, Order.class);
                if (order != null) {
                System.out.println("Order ID: " + order.getOrderID()
                        + "\nCustomer Name: " + order.getCustomerName()
                        + "\nTotal Cost: " + df.format(order.getTotalTax() + order.getTotalCost())
                        + "\nItems in this Order: ");
                    for (OrderLine line : order.getLines()) {
                        System.out.println("\tProduct ID: " + line.getProductID()
                            + "\n\tAmount: " + line.getQuantity()
                            + "\n\tTotal Line Cost: " + df.format(line.getCost()));
                    }
                } else { System.out.println("An order with that ID was not found in the database."); }
                break;
            case ServiceInfoModel.PRODUCT_PRICE_UPDATE_SERVICE:
                System.out.print("Enter a Product ID to modify: ");
                id = Integer.parseInt(input.next().trim());
                modelmsg = gson.toJson(id);
                printer2.writeUTF(modelmsg);
                printer2.flush();

                modelmsg = reader2.readUTF();
                Product pchange = gson.fromJson(modelmsg, Product.class);
                //display current info
                if (pchange != null) {
                System.out.println("Product ID: " + pchange.getProductID()
                        + "\nName: " + pchange.getName()
                        + "\nPrice: " + df.format(pchange.getPrice())
                        + "\nQuantity: " + pchange.getQuantity());

                System.out.print("Enter the new price in decimal format: ");
                double price = Double.parseDouble(input.next().trim());
                pchange.setPrice(price);
                //send updated product info back
                modelmsg = gson.toJson(pchange, Product.class);
                printer2.writeUTF(modelmsg);
                printer2.flush();

                //receive confirmed change
                modelmsg = reader2.readUTF();
                product = gson.fromJson(modelmsg, Product.class);
                //display updated info
                System.out.println("Updated Information:\nProduct ID: " + product.getProductID()
                        + "\nName: " + product.getName()
                        + "\nPrice: " + df.format(product.getPrice())
                        + "\nQuantity: " + product.getQuantity());
                } else { System.out.println("A product with that ID was not found in the database."); }
                break;
            case ServiceInfoModel.PRODUCT_QUANTITY_UPDATE_SERVICE: //===============================================
                System.out.print("Enter a Product ID to modify: ");
                id = Integer.parseInt(input.next().trim());
                modelmsg = gson.toJson(id);
                printer2.writeUTF(modelmsg);
                printer2.flush();

                modelmsg = reader2.readUTF();
                Product qchange = gson.fromJson(modelmsg, Product.class);
                //display current info
                if (qchange != null) {
                System.out.println("Product ID: " + qchange.getProductID()
                        + "\nName: " + qchange.getName()
                        + "\nPrice: " + df.format(qchange.getPrice())
                        + "\nQuantity: " + qchange.getQuantity());

                System.out.print("Enter the new quantity in decimal format: ");
                double quant = Double.parseDouble(input.next().trim());
                qchange.setQuantity(quant);
                //send updated product info back
                modelmsg = gson.toJson(qchange, Product.class);
                printer2.writeUTF(modelmsg);
                printer2.flush();

                //receive confirmed change
                modelmsg = reader2.readUTF();
                product = gson.fromJson(modelmsg, Product.class);
                //display updated info
                System.out.println("Updated Information:\nProduct ID: " + product.getProductID()
                        + "\nName: " + product.getName()
                        + "\nPrice: " + df.format(product.getPrice())
                        + "\nQuantity: " + product.getQuantity());
                } else { System.out.println("A product with that ID was not found in the database."); }
                break;
            case ServiceInfoModel.AUTH_LOGIN_SERVICE:
                //check un and pw to see if registered user exists
                System.out.print("Enter a username to look up: ");
                username = input.next().trim();
                System.out.print("Enter a password to look up: ");
                String password = input.next().trim();
                User dummy = new User();
                dummy.setUserName(username);
                dummy.setPassword(password);
                modelmsg = gson.toJson(dummy, User.class);
                printer2.writeUTF(modelmsg);
                printer2.flush();

                modelmsg = reader2.readUTF();
                dummy = gson.fromJson(modelmsg, User.class);
                if (dummy != null) {
                    System.out.println("Found user with the stated information:\n" +
                            "User ID: " + dummy.getUserID()
                            + "\nDisplay name: " + dummy.getDisplayName()
                            + "\nUsername: " + dummy.getUserName()
                            + "\nPassword: " + dummy.getPassword());
                } else { System.out.println("A user with that information was not found in the database."); }
                break;
            case ServiceInfoModel.USER_PERMS_SERVICE:
                //given un and order id, check if order belongs to that user
                System.out.print("Enter an order ID to check: ");
                id = Integer.parseInt(input.next().trim());
                modelmsg = gson.toJson(id);
                printer2.writeUTF(modelmsg);
                printer2.flush();

                modelmsg = reader2.readUTF();
                Order dummyOrder = gson.fromJson(modelmsg, Order.class);

                if (dummyOrder != null) {
                    System.out.print("Order belongs to Customer: " + dummyOrder.getCustomerName()
                            + "\nEnter a username: ");
                    username = input.next().trim();
                    printer2.writeUTF(username);
                    printer2.flush();

                    modelmsg = reader2.readUTF();
                    dummy = gson.fromJson(modelmsg, User.class);
                    if (dummy != null) {
                        System.out.println("This user's display name is: " + dummy.getDisplayName() + ".");
                        //compare user display name with order's customer name
                        if (dummy.getDisplayName().equalsIgnoreCase(dummyOrder.getCustomerName())) {
                            System.out.println("This user is allowed to cancel Order "
                                    + dummyOrder.getOrderID() + ".");
                        } else { System.out.println("Order " + dummyOrder.getOrderID()
                                    + " does not belong to this user."); }
                    } else { System.out.println("A user with that information was not found in the database."); }
                } else { System.out.println("An order with that ID was not found in the database."); }
                break;
            default:
                System.out.println("Unrecognized Request!");
        }

        printer2.close();
        reader2.close();
        socket2.close();
    }
}
