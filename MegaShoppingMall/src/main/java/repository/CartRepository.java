package repository;

import infrastructure.FileManager;
import models.Cart;
import models.CartItem;
import models.Service;
import models.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class CartRepository {
    FileManager fileManager;
    Cart cart;

    public CartRepository() {
        this.fileManager = new FileManager("cart.csv");
    }

    public List<CartItem> getCart(User user) throws FileNotFoundException {
        cart = fileManager.getCart(user);

        return cart.items();
    }

    public void add(Service service, User user) throws IOException {
        fileManager.addItemToCart(service, user);
    }

    public void delete(CartItem cartItem) throws IOException {
        fileManager.deleteItemFromCart(cartItem);
    }
}
