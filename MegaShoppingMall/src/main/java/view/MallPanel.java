package view;

import controller.MallController;
import models.Mall;
import models.CartItem;
import models.Order;
import models.Product;
import models.User;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MallPanel extends JPanel { // TODO session
    private Mall mall;
    private MallController mallController;
    private List<Product> products = new ArrayList<>();

    private JPanel header;

    public MallPanel(MallController mallController) {
        this.mallController = mallController;
        this.mall = new Mall();

        addProducts();
        initMallPanel();
    }

    private void addProducts() { // 임시데이터
        for (int i = 0; i < 12; i += 1) {
            products.add(new Product("상품", 100));
        }

        mall.set(products);
    }

    private void initMallPanel() {
        this.setLayout(new BorderLayout());

        addHeader();
        addCartItemPanel();
    }

    private void addHeader() {
        String[] userInformation = mallController.userInformation();

        header = new JPanel();
        header.setLayout(new GridLayout(0, 3));
        header.add(new JLabel("ID : " + userInformation[0]));
        header.add(new JLabel("보유 금액 : " + userInformation[1]));
        header.add(new JLabel("회원 등급 : " + userInformation[2]));
        header.setBorder(new EmptyBorder(10, 10 ,10 ,10));

        this.add(header, BorderLayout.NORTH);
    }

    private void addCartItemPanel() {
        JPanel CartItemPanel = new JPanel();
        CartItemPanel.setLayout(new GridLayout(0, 2));

        this.add(CartItemPanel, BorderLayout.CENTER);

        for (Product product : mall.get()) {
            JPanel panel = new JPanel();
            Button name = new Button(product.name() + "사진");
            JLabel price = new JLabel("가격: " + product.price() + " 원");

            JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
            JButton toCart = new JButton("장바구니");
            toCart.addActionListener(event -> {
                try {
                    mallController.toCart(product);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            addOrderButton(product, buttonPanel);

            panel.add(name);
            panel.add(price);
            panel.add(buttonPanel);

            buttonPanel.add(toCart);

            CartItemPanel.add(panel);
        }
    }

    private void addOrderButton(Product product, JPanel buttonPanel) {
        JButton order = new JButton("주문");
        order.addActionListener(event -> {
            if (isGuest()) {
                return;
            }

            try {
                purchase(product);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        buttonPanel.add(order);
    }

    private boolean isGuest() {
        Optional<User> user = mallController.getSession();

        if (user.isEmpty()) {
            final JDialog frame = new JDialog(new Frame(), "Error", true);
            JPanel error = new JPanel();
            error.add(new JLabel("로그인이 필요한 서비스입니다."));

            frame.getContentPane().add(error);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);

            return true;
        }

        return false;
    }

    private void purchase(Product product) throws IOException {
        Optional<Order> order = mallController.purchase(product);

        if (order.isEmpty()) {
            final JDialog frame = new JDialog(new Frame(), "Error", true);

            JPanel error = new JPanel();
            error.add(new JLabel("보유 금액이 부족합니다."));

            frame.getContentPane().add(error);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);

            return;
        }

        updateHeader();
    }

    private void updateHeader() {
        header.removeAll();

        String[] userInformation = mallController.userInformation();

        header.add(new JLabel("ID : " + userInformation[0]));
        header.add(new JLabel("보유 금액 : " + userInformation[1]));
        header.add(new JLabel("회원 등급 : " + userInformation[2]));

        header.setVisible(false);
        header.setVisible(true);
    }
}
