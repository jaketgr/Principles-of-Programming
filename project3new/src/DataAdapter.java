import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DataAdapter is for serverside DB access
 *  used by client handler
 *  connects to store.db
 *  uses gson and does the heavy lifting
 *      eg, looking through DB or adding new entries
 */
public class DataAdapter implements DataAccess {
    private Connection connection;
    //public DataAdapter(Connection connection) { this.connection = connection; }

    @Override
    public void connect(String url) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);

            if (connection == null) {
                System.out.println("Cannot make the connection!!!");
            } else {
                System.out.println("The connection object is " + connection);
            }
            System.out.println("Connection to SQLite has been established.");

        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void disconnect() {
        try {
            connection.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    } //*/

    @Override
    public Product loadProduct(int id) {
        try {
            String query = "SELECT * FROM Product WHERE ProductID = " + id;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                Product product = new Product();
                product.setProductID(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setPrice(resultSet.getDouble(3));
                product.setQuantity(resultSet.getDouble(4));
                resultSet.close();
                statement.close();

                return product;
            }

        } catch (SQLException e) {
            System.out.println("Product database access error!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> loadAllProducts() {
        List<Product> list = new ArrayList<>();
        Product item = null;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product ");
            while (rs.next()) {
                item = new Product();
                item.setProductID(rs.getInt(1));
                item.setName(rs.getString(2));
                item.setPrice(rs.getDouble(3));
                item.setQuantity(rs.getDouble(4));
                list.add(item);
            }

        } catch (Exception ex) {
            System.out.println("Error retrieving all products!");
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean saveProduct(Product product) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Product WHERE ProductID = ?");
            statement.setInt(1, product.getProductID());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // this product exists, update its fields
                statement = connection.prepareStatement("UPDATE Product SET Name = ?, Price = ?, Quantity = ? WHERE ProductID = ?");
                statement.setString(1, product.getName());
                statement.setDouble(2, product.getPrice());
                statement.setDouble(3, product.getQuantity());
                statement.setInt(4, product.getProductID());
            }
            else { // this product does not exist, use insert into
                statement = connection.prepareStatement("INSERT INTO Product VALUES (?, ?, ?, ?)");
                statement.setString(2, product.getName());
                statement.setDouble(3, product.getPrice());
                statement.setDouble(4, product.getQuantity());
                statement.setInt(1, product.getProductID());
            }
            statement.execute();
            resultSet.close();
            statement.close();
            return true;        // save successfully

        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }

    @Override
    public Order loadOrder(int id) {
        try {
            Order order = null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM \"Order\" WHERE OrderID = " + id);

            if (resultSet.next()) {
                order = new Order();
                order.setOrderID(resultSet.getInt("OrderID"));
                order.setCustomerName(resultSet.getString("Customer"));
                order.setTotalCost(resultSet.getDouble("TotalCost"));
                order.setTotalTax(resultSet.getDouble("TotalTax"));
                order.setDate(resultSet.getDate("OrderDate"));
                resultSet.close();
                statement.close();
            }

            // loading the order lines for this order
            resultSet = statement.executeQuery("SELECT * FROM OrderLine WHERE OrderID = " + id);

            while (resultSet.next()) {
                OrderLine line = new OrderLine();
                line.setOrderID(resultSet.getInt(1));
                line.setProductID(resultSet.getInt(2));
                line.setQuantity(resultSet.getDouble(3));
                line.setCost(resultSet.getDouble(4));
                order.addLine(line);
            }

            return order;

        } catch (SQLException e) {
            System.out.println("Order database access error!");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean saveOrder(Order order)
    {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Order\" VALUES ($next_id, ?, ?, ?, ?)");
            statement.setInt(1, order.getOrderID());
            statement.setDate(2, order.getDate());
            statement.setString(3, order.getCustomerName());
            statement.setDouble(4, order.getTotalCost());
            statement.setDouble(5, order.getTotalTax());

            statement.execute();    // commit to the database;
            statement.close();

            //gets latest ID for properly adding new order lines
            Statement statement2 = connection.createStatement();
            ResultSet resultSet = statement2.executeQuery("SELECT * FROM \"Order\" ORDER BY rowid DESC LIMIT 1");
            if (resultSet.next()) {
                order.setOrderID(resultSet.getInt("OrderID"));
                resultSet.close();
                statement2.close();
            }

            statement = connection.prepareStatement("INSERT INTO OrderLine VALUES (?, ?, ?, ?)");
            for (OrderLine line: order.getLines()) { // store for each order line!
                statement.setInt(1, order.getOrderID());
                statement.setInt(2, line.getProductID());
                statement.setDouble(3, line.getQuantity());
                statement.setDouble(4, line.getCost());

                statement.execute();    // commit to the database;
            }
            statement.close();
            return true; // save successfully!
        }
        catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User loadUser(String username, String password) {
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM User WHERE UserName = ? AND Password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setUserID(resultSet.getInt("UserID"));
                user.setUserName(resultSet.getString("UserName"));
                user.setPassword(resultSet.getString("Password"));
                user.setDisplayName(resultSet.getString("DisplayName"));
                user.setManager(resultSet.getBoolean("IsManager"));
                resultSet.close();
                statement.close();

                return user;
            }

        } catch (SQLException e) {
            System.out.println("User database access error!");
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public User loadUser(String username) {
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM User WHERE UserName = ?");
            statement.setString(1, username);
            //statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setUserID(resultSet.getInt("UserID"));
                user.setUserName(resultSet.getString("UserName"));
                user.setPassword(resultSet.getString("Password"));
                user.setDisplayName(resultSet.getString("DisplayName"));
                user.setManager(resultSet.getBoolean("IsManager"));
                resultSet.close();
                statement.close();

                return user;
            }

        } catch (SQLException e) {
            System.out.println("User database access error!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addUser(User u) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM User WHERE UserName = ?");
            statement.setString(1, u.getUserName());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("This username is already taken!");
                return false;
            }
            else {
                statement = connection.prepareStatement("INSERT INTO \"User\" VALUES ($next_id, ?, ?, ?, ?)");
                //statement.setInt(1, u.getUserID());
                statement.setString(2, u.getUserName());
                statement.setString(3, u.getPassword());
                statement.setString(4, u.getDisplayName());
                statement.setBoolean(5, u.isManager());

                statement.execute();    // commit to the database;
                statement.close();
                return true; // save successfully
            }
        } catch (SQLException e) {
            System.out.println("User database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }
}
