package models;

import repository.OrderRepository;
import repository.UserRepository;
import service.CartService;
import service.OrderService;
import service.UserService;

import java.util.List;
import java.util.Optional;
import java.io.IOException;

public class Payment {
    public Optional<Order> purchase(Service service, User user) throws IOException {
        if (Integer.parseInt(service.price()) > user.balance()) {
            return Optional.empty();
        }

        Optional<Order> order = Optional.of(new Order(service, user.id()));

        receivePayment(user, order.get());
        recordOrder(order.get());

        return order;
    }

    public Optional<Order> purchase(CartItem cartItem, User user) throws IOException {
        if (Integer.parseInt(cartItem.price()) > user.balance()) {
            return Optional.empty();
        }

        Service service = new Service(cartItem.name(), Integer.parseInt(cartItem.price()));

        Optional<Order> order = Optional.of(new Order(service, user.id()));

        receivePayment(user, order.get());
        recordOrder(order.get());
        deleteCartItem(cartItem);

        return order;
    }

    public Optional<Order> purchase(List<CartItem> items, User user) throws IOException {
        List<CartItem> cartItems = items;

        int sum = cartItems.stream()
                .map(CartItem::price)
                .map(price -> Integer.parseInt(price))
                .reduce(0, (a, b) -> a + b);

        if (sum > user.balance()) {
            return Optional.empty();
        }

        CartItem cartItem = cartItems.get(0);

        int totalCount = cartItems.size();

        Service service = new Service(cartItem.name() + " 외 " + totalCount + "개", sum);
        Optional<Order> order = Optional.of(new Order(service, user.id()));

        receivePayment(user, order.get());
        recordOrder(order.get());

        for (CartItem item: cartItems) {
            deleteCartItem(item);
        }

        return order;
    }

    private void deleteCartItem(CartItem cartItem) throws IOException {
        new CartService().delete(cartItem);
    }

    public void receivePayment(User user, Order order) throws IOException {
        UserService userService = new UserService(new UserRepository());
        userService.purchase(user, order);
    }

    private void recordOrder(Order order) throws IOException {
        OrderService orderService = new OrderService(new OrderRepository());
        orderService.record(order);
    }
}
