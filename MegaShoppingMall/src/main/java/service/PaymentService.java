package service;

import models.Cart;
import models.CartItem;
import models.Order;
import models.Payment;
import models.Product;
import models.User;

import java.util.Optional;

import java.io.IOException;

public class PaymentService {
    public Optional<Order> purchase(User user, Product product) throws IOException {
       return new Payment().purchase(product, user);
    }

    public Optional<Order> purchase(User user, CartItem cartItem) throws IOException {
        return new Payment().purchase(cartItem, user);
    }

    public Optional<Order> purchase(User user, Cart cart) throws IOException {
        return new Payment().purchase(cart, user);
    }
}
