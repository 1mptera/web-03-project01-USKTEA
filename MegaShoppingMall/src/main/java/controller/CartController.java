package controller;

import models.Cart;
import models.CartItem;
import models.Order;
import models.Service;
import models.User;
import service.CartService;
import service.PaymentService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CartController {
    private CartService cartService;
    private User user;

    public CartController(User user) {
        this.cartService = new CartService();
        this.user = user;
    }

    public List<CartItem> getCart() throws FileNotFoundException {
        return cartService.getCart(user);
    }

    public void add(Service service) throws IOException {
        cartService.add(service, user);
    }

    public void delete(CartItem cartItem) throws IOException {
        cartService.delete(cartItem);
    }

    public Optional<Order> order(CartItem cartItem) throws IOException {
        Optional<Order> order = new PaymentService().purchase(user, cartItem);

        return order;
    }

    public Optional<Order> orderAll(List<CartItem> items) throws IOException {
        Optional<Order> order = new PaymentService().purchase(user, items);

        return order;
    }
}
