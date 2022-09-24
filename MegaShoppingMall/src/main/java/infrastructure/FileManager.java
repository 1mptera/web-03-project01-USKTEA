package infrastructure;

import models.Cart;
import models.CartItem;
import models.ImagePath;
import models.Mall;
import models.Order;
import models.Service;
import models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class FileManager {
    private File file;
    private Scanner scanner;

    public FileManager(String filePath) {
        this.file = new File(filePath);
    }

    public Optional<User> findAccount(String[] idAndEmail) throws IOException {
        scanner = new Scanner(file);

        String id = idAndEmail[0];
        String password = idAndEmail[1];

        while (scanner.hasNextLine()) {
            String information = scanner.nextLine();

            if (information.equals("")) {
                continue;
            }

            String[] accountInformation = information.split(",");
            String accountId = accountInformation[0];
            String accountPassword = accountInformation[1];

            if (id.equals(accountId) && password.equals(accountPassword)) {
                return Optional.of(new User(accountInformation));
            }
        }

        return Optional.empty();
    }

    public void modifyMoney(String id, int balance) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String[] information = scanner.nextLine().split(",");

            if (information[0].equals(id)) {
                information[2] = Integer.toString(balance);
            }

            stringBuffer.append(String.join(",", information) + "\n");
        }

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(String.valueOf(stringBuffer));
        fileWriter.close();
    }

    public void recordOrder(Order order) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        String[] information = order.information();

        String id = information[0];
        String userId = information[1];
        String productName = information[2];
        String productPrice = information[3];
        String delivered = information[4];

        fileWriter.write(id + "," + userId + "," + productName + "," + productPrice + "," + delivered + "\n");
        fileWriter.close();
    }

    public List<Order> getOrders() throws FileNotFoundException {
        scanner = new Scanner(file);
        List<Order> orders = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String text = scanner.nextLine();

            if (text.equals("")) {
                continue;
            }

            String[] words = text.split(",");

            long id = Long.parseLong(words[0]);
            String userId = words[1];
            String productName = words[2];
            String productPrice = words[3];
            boolean delivered = Boolean.valueOf(words[4]);

            Order order = new Order(id, userId, productName, productPrice, delivered);
            orders.add(order);
        }

        return orders;
    }

    public void setDelivered(Order order) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String[] information = scanner.nextLine().split(",");
            ;
            String id = Long.toString(order.id());

            if (information[0].equals(id)) {
                information[4] = "true";
            }

            stringBuffer.append(String.join(",", information) + "\n");
        }

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(String.valueOf(stringBuffer));
        fileWriter.close();
    }

    public void deleteOrder(Order order) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String[] information = scanner.nextLine().split(",");
            ;
            String id = Long.toString(order.id());

            if (information[0].equals(id)) {
                continue;
            }

            stringBuffer.append(String.join(",", information) + "\n");
        }

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(String.valueOf(stringBuffer));
        fileWriter.close();
    }

    public Cart getCart(User user) throws FileNotFoundException {
        List<CartItem> items = new ArrayList<>();
        scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String text = scanner.nextLine();

            if (text.equals("")) {
                continue;
            }

            String[] words = text.split(",");

            String cartItemId = words[0];
            String name = words[1];
            String price = words[2];
            String userId = words[3];

            if (user.id().equals(userId)) {
                items.add(new CartItem(cartItemId, name, price, userId));
            }
        }

        return new Cart(items);
    }

    public void addItemToCart(Service service, User user) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);

        long id = System.currentTimeMillis();
        String name = service.name();
        String price = service.price();
        String userId = user.id();

        fileWriter.write(id + "," + name + "," + price + "," + userId + "\n");
        fileWriter.close();
    }

    public void deleteItemFromCart(CartItem cartItem) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String[] information = scanner.nextLine().split(",");
            ;
            String id = information[0];

            if (id.equals(Long.toString(cartItem.id()))) {
                continue;
            }

            stringBuffer.append(String.join(",", information) + "\n");
        }

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(String.valueOf(stringBuffer));
        fileWriter.close();
    }

    public void cartItemToOrder(CartItem cartItem) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);

        String id = Long.toString(cartItem.id());
        String userId = cartItem.userId();
        String productName = cartItem.name();
        String productPrice = cartItem.price();
        String delivered = "false";

        fileWriter.write(id + "," + userId + "," + productName + "," + productPrice + "," + delivered + "\n");
        fileWriter.close();
    }

    public Mall mallProducts() throws FileNotFoundException {
        List<Service> services = new ArrayList<>();
        scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String text = scanner.nextLine();

            if (text.equals("")) {
                continue;
            }

            String[] words = text.split(",");

            String name = words[0];
            String price = words[1];
            String imagePath = words[2];

            services.add(new Service(name, price, imagePath));
        }

        return new Mall(services);
    }

    public List<Service> getServices(User user) throws FileNotFoundException {
        scanner = new Scanner(file);
        List<Service> services = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String text = scanner.nextLine();

            if (text.equals("")) {
                continue;
            }

            String[] words = text.split(",");

            String userId = words[1];
            String productName;

            while (true) {
                if (!words[2].contains(" ")) {
                    productName = words[2];

                    break;
                }

                if (words[2].contains(" ")) {
                    String[] service = words[2].split(" ");

                    if (service[1].equals("외")) {
                        productName = service[0];

                        break;
                    }

                    productName = service[0] + " " + service[1];

                    break;
                }
            }

            boolean delivered = Boolean.valueOf(words[4]);

            if (userId.equals(user.id()) && delivered == true) {
                services.add(new Service(productName));
            }
        }

        return services;
    }

    public ImagePath imagePaths() throws FileNotFoundException {
        HashMap<String, String> imagePaths = new HashMap<>();

        scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String[] words = scanner.nextLine().split(",");

            String image = words[0];
            String path = words[1];

            imagePaths.put(image, path);
        }

        return new ImagePath(imagePaths);
    }
}
