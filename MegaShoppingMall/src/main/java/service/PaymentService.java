package service;

import models.Cart;
import models.CartItem;
import models.Order;
import models.Payment;
import models.Service;
import models.User;

import java.util.List;
import java.util.Optional;

import java.io.IOException;

public class PaymentService {
    public Optional<Order> purchase(User user, Service service) throws IOException {
       return new Payment().purchase(service, user);
    }

    public Optional<Order> purchase(User user, CartItem cartItem) throws IOException {
        return new Payment().purchase(cartItem, user);
    }

    public Optional<Order> purchase(User user, List<CartItem> items) throws IOException {
        return new Payment().purchase(items, user);
    }
}
