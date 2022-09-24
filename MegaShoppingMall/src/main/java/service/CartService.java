package service;

import models.Cart;
import models.CartItem;
import models.Service;
import models.User;

import repository.CartRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class CartService {
    private CartRepository cartRepository;

    public CartService() {
        this.cartRepository = new CartRepository();
    }

    public List<CartItem> getCart(User user) throws FileNotFoundException {
        return cartRepository.getCart(user);
    }

    public void add(Service service, User user) throws IOException {
        cartRepository.add(service, user);
    }

    public void delete(CartItem cartItem) throws IOException {
        cartRepository.delete(cartItem);
    }
}
