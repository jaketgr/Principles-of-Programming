import com.hp.gagawa.java.elements.*;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
public class WebServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
        HttpContext root = server.createContext("/");
        root.setHandler(WebServer::handleRequest);

        HttpContext users = server.createContext("/users");
        users.setHandler(WebServer::handleRequestUser);

        HttpContext product = server.createContext("/products");
        product.setHandler(WebServer::handleRequestOneProduct);

        HttpContext allproducts = server.createContext("/products/all");
        allproducts.setHandler(WebServer::handleRequestAllProducts);

        HttpContext order = server.createContext("/order");
        order.setHandler(WebServer::handleRequestOrder);

        server.start();
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {

        Html html = new Html();
        Head head = new Head();
        html.appendChild(head);

        Title title = new Title();
        title.appendChild(new Text("Online shopping web server"));
        head.appendChild(title);

        Body body = new Body();

        H1 h1 = new H1();
        h1.appendChild(new Text("Online store homepage"));
        body.appendChild(h1);
        P para = new P();
        para.appendChild(new Text("The server time is " + LocalDateTime.now()));
        body.appendChild(para);

        para = new P();
        A link = new A("/products/all", "/products/all");
        link.appendText("Product list");
        para.appendChild(link);
        body.appendChild(para);

        para = new P();
        link = new A("/users", "/users");
        link.appendText("Users list");
        para.appendChild(link);
        body.appendChild(para);

        para = new P();
        link = new A("/order/", "/order/");
        link.appendText("Order list");
        para.appendChild(link);
        body.appendChild(para);

        html.appendChild( body );
        String response = html.write();
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestUser(HttpExchange exchange) throws IOException {
        String uri =  exchange.getRequestURI().toString();
        String username = uri.substring(uri.lastIndexOf('/')+1);
        Html html = new Html();
        Head head = new Head();
        html.appendChild(head);
        Title title = new Title();
        title.appendChild(new Text("Users"));
        head.appendChild(title);
        Body body = new Body();
        html.appendChild(body);


         if (username.equalsIgnoreCase("") || username.equalsIgnoreCase("users")) {
         H1 h1 = new H1();
         h1.appendChild(new Text("Users homepage"));
         body.appendChild(h1);
         P para = new P();
         para.appendChild(new Text("The server time is " + LocalDateTime.now()));
         body.appendChild(para);

         para = new P();
         A link = new A("/users/admin", "/users/admin");
         link.appendText("admin");
         para.appendChild(link);
         body.appendChild(para);

         para = new P();
         link = new A("/users/user", "/users/user");
         link.appendText("user");
         para.appendChild(link);
         body.appendChild(para);

         para = new P();
         link = new A("/users/jacob", "/users/jacob");
         link.appendText("jacob");
         para.appendChild(link);
         body.appendChild(para);
         }
         else {

            System.out.println(username);

            String url = "jdbc:sqlite:store.db";
            DataAdapter dao = new DataAdapter();
            dao.connect(url);

            H1 h1 = new H1();
            h1.appendChild(new Text("Page of User " + username));
            body.appendChild(h1);

            P para = new P();
            para.appendChild(new Text("The server time is " + LocalDateTime.now()));
            body.appendChild(para);

            User user = dao.loadUser(username);

            if (user != null) {
                para = new P();
                para.appendText("User ID: " + user.getUserID());
                html.appendChild(para);
                para = new P();
                para.appendText("Username: " + user.getUserName());
                html.appendChild(para);
                para = new P();
                para.appendText("Display name: " + user.getDisplayName());
                html.appendChild(para);
                para = new P();
                para.appendText("Password: " + user.getPassword());
                html.appendChild(para);
                para = new P();
                para.appendText("Manager Status: " + (user.isManager() ? "Yes" : "No"));
                html.appendChild(para);
            }

                else {
                 para = new P();
                 para.appendText("User not found");
                 html.appendChild(para);
                 }

        }

        String response = html.write();
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestOrder(HttpExchange exchange) throws IOException {
        DecimalFormat df = new DecimalFormat("$#,##0.00");
        String uri =  exchange.getRequestURI().toString();
        System.out.println(uri);

        String url = "jdbc:sqlite:store.db";
        DataAdapter dao = new DataAdapter();
        dao.connect(url);


        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("Order") );
        head.appendChild( title );

        Body body = new Body();
        html.appendChild( body );

        if (uri.substring(uri.lastIndexOf('/')+1).equals("") || uri.substring(uri.lastIndexOf('/')+1).equals("order")) {
            H1 h1 = new H1();
            h1.appendChild(new Text("Order homepage"));
            body.appendChild(h1);
            P para = new P();
            para.appendChild(new Text("The server time is " + LocalDateTime.now()));
            body.appendChild(para);

            para = new P();
            A link = new A("/order/30", "/order/30");
            link.appendText("30");
            para.appendChild(link);
            body.appendChild(para);

            para = new P();
            link = new A("/order/31", "/order/31");
            link.appendText("31");
            para.appendChild(link);
            body.appendChild(para);
        }
        else {

            int id = Integer.parseInt(uri.substring(uri.lastIndexOf('/')+1));

            H1 h1 = new H1();
            h1.appendChild(new Text("Order " + id));
            body.appendChild(h1);

            P para = new P();
            para.appendChild(new Text("The server time is " + LocalDateTime.now()));
            body.appendChild(para);

            Order order = dao.loadOrder(id);

            if (order != null) {
                para = new P();
                para.appendText("Order ID: " + order.getOrderID());
                html.appendChild(para);
                para = new P();
                para.appendText("Ordered by: " + order.getCustomerName());
                html.appendChild(para);
                para = new P();
                para.appendText("Subtotal: " + df.format(order.getTotalCost()));
                html.appendChild(para);
                para = new P();
                para.appendText("Tax: " + df.format(order.getTotalTax()));
                html.appendChild(para);
                para = new P();
                para.appendText("Total cost: " + df.format(order.getTotalCost() + order.getTotalTax()));
                html.appendChild(para);

                //orderlines table pending
                Table table = new Table();
                Tr row = new Tr();
                Th header = new Th();
                header.appendText("Product ID");
                row.appendChild(header);
                header = new Th();
                header.appendText("Quantity");
                row.appendChild(header);
                header = new Th();
                header.appendText("Cost");
                row.appendChild(header);
                table.appendChild(row);

                for (OrderLine line : order.getLines()) {
                    row = new Tr();
                    Td cell = new Td();
                    cell.appendText(String.valueOf(line.getProductID()));
                    row.appendChild(cell);
                    cell = new Td();
                    cell.appendText(String.valueOf(line.getQuantity()));
                    row.appendChild(cell);
                    cell = new Td();
                    cell.appendText(String.valueOf(line.getCost()));
                    row.appendChild(cell);
                    table.appendChild(row);
                }
                table.setBorder("1");
                html.appendChild(table);
            }
                else {
                    para = new P();
                    para.appendText("Order not found");
                    html.appendChild(para);
                }
        }

        String response = html.write();
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestAllProducts(HttpExchange exchange) throws IOException {
//        String response =  "This simple web server is NOT designed with help from ChatGPT!";
        String url = "jdbc:sqlite:store.db";

        DataAdapter dao = new DataAdapter();
        dao.connect(url);
        List<Product> list = dao.loadAllProducts();

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("All Products") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("All Products") );
        body.appendChild( h1 );

        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);

        para = new P();
        para.appendChild( new Text("The server has " + list.size() + " products." ));
        body.appendChild(para);

        para = new P();
        para.appendChild( new Text("Individual product info for table entries found at"
                + " /products/productID" ));
        body.appendChild(para);

        Table table = new Table();
        Tr row = new Tr();
        Th header = new Th(); header.appendText("ProductID"); row.appendChild(header);
        header = new Th(); header.appendText("Product name"); row.appendChild(header);
        header = new Th(); header.appendText("Price"); row.appendChild(header);
        header = new Th(); header.appendText("Quantity"); row.appendChild(header);
        table.appendChild(row);

        for (Product product : list) {
            row = new Tr();
            Td cell = new Td(); cell.appendText(String.valueOf(product.getProductID())); row.appendChild(cell);
            cell = new Td(); cell.appendText(product.getName()); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(product.getPrice())); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(product.getQuantity())); row.appendChild(cell);
            table.appendChild(row);
        }

        table.setBorder("1");
        html.appendChild(table);
        String response = html.write();

        System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestOneProduct(HttpExchange exchange) throws IOException {
        DecimalFormat df = new DecimalFormat("$#,##0.00");
        String uri =  exchange.getRequestURI().toString();
        int id = Integer.parseInt(uri.substring(uri.lastIndexOf('/')+1));
        System.out.println(id);

        //SQLite
        String url = "jdbc:sqlite:store.db";
        DataAdapter dao = new DataAdapter();
        dao.connect(url);

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("Products") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild(new Text("Product " + id));
        body.appendChild( h1 );

        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);

        Product product = dao.loadProduct(id);

        if (product != null) {
            para = new P();
            para.appendText("ProductID: " + product.getProductID());
            html.appendChild(para);
            para = new P();
            para.appendText("Product name: " + product.getName());
            html.appendChild(para);
            para = new P();
            para.appendText("Price: " + df.format(product.getPrice()));
            html.appendChild(para);
            para = new P();
            para.appendText("Quantity available: " + product.getQuantity());
            html.appendChild(para);
        }
        else {
            para = new P();
            para.appendText("Product not found");
            html.appendChild(para);
        }

        String response = html.write();

        System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


}
