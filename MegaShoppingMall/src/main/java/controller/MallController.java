package controller;

import models.Product;
import models.Order;
import models.User;
import service.PaymentService;
import service.UserService;

import java.util.Optional;
import java.io.IOException;

public class MallController {
    private UserService userService;
    private Optional<User> session;
    private User user;

    public MallController(UserService userService) {
        this.userService = userService;
        this.session = userService.getSession();
    }

    public String[] userInformation() {
        if (session.isEmpty()) {
            return new String[] {"Guest", "-", "방문 고객"};
        }

        return session.get().information();
    }

    public Optional<Order> purchase(Product product) throws IOException {
        user = session.get();

        Optional<Order> order = new PaymentService().purchase(user, product);

        if (order.isEmpty()) {
            return Optional.empty();
        }

        return order;
    }

    public Optional<User> getSession() {
        return session;
    }
}
