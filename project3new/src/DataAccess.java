import java.util.List;
public interface DataAccess {
    void connect(String s);

    void disconnect();

    boolean saveProduct(Product product);

    Product loadProduct(int productID);

    List<Product> loadAllProducts();

    boolean saveOrder(Order order);

    Order loadOrder(int id);

    boolean addUser(User user);

    //overloaded for multiple implementations
    User loadUser(String username, String password);
    User loadUser(String username);
    //User loadUser(User user);

    /**
     * server creates new ClientHandler for every client that connects
     *
     * DataAdapter is for serverside DB access
     *  methods get called by client handler based on requests from client read w RDA
     *  connects to store.db and does the heavy lifting
     *      eg, looking thru DB or adding new entries
     *
     * RemoteDataAdapter is for clientside DB access
     *  used in application, which is the client application
     *  contains the functions the controllers call
     *  connects to the server socket
     *  uses gson and request/response models
     *      sends requests to the server for the specific CH to handle

     //*/
}
