package view;

import controller.CartController;

import models.Cart;
import models.CartItem;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;

import java.io.IOException;
import java.util.List;

import java.io.FileNotFoundException;

public class CartPanel extends JPanel {
    private CartController cartController;

    private JPanel CartItemPanel;
    private JPanel CartItemInformation;
    private JPanel buttonPanel;
    private JPanel detailPanel;

    public CartPanel(CartController cartController) throws FileNotFoundException {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.cartController = cartController;

        updateDisplay(cartController.getCart());
    }

    private void addHeader() {
        JPanel header = new JPanel();

        JLabel label = new JLabel("위시 리스트");
        label.setBorder(new EmptyBorder(0, 30, 0, 30));
        label.setHorizontalAlignment(JLabel.CENTER);

        header.add(label);
        this.add(header, BorderLayout.NORTH);
    }

    public void updateDisplay(List<CartItem> items) {
        this.removeAll();

        addHeader();

        JPanel recordPanel = new JPanel(new GridLayout(0, 1));
        recordPanel.setOpaque(false);

        for (CartItem cartItem : items) {
            addCartItemPanel(cartItem);
            addButtonPanel(recordPanel, cartItem);
        }

        addScrollPane(recordPanel);
        addControlPanel();
        addCartDetail(items);
        addOrderButton(items);

        update();
    }

    private void addCartItemPanel(CartItem cartItem) {
        CartItemPanel = new JPanel();
        CartItemPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        CartItemPanel.setOpaque(false);

        CartItemInformation = new JPanel(new GridLayout(0, 1));
        CartItemInformation.add(new JLabel("요청 품목: " + cartItem.name()));
        CartItemInformation.add(new JLabel("가격: " + cartItem.price()));

        CartItemPanel.add(CartItemInformation, BorderLayout.CENTER);
    }

    private void addCartDetail(List<CartItem> items) {
        List<CartItem> cartItems = items;

        int sum = cartItems.stream()
                .map(CartItem::price)
                .map(price -> Integer.parseInt(price))
                .reduce(0, (a, b) -> a + b);

        JPanel cartDetail = new JPanel();
        JLabel totalCount = new JLabel("총 수량: " + cartItems.size() + "개");
        JLabel totalAmount = new JLabel(" 합계: " + sum + "p");

        cartDetail.add(totalCount);
        cartDetail.add(totalAmount);

        detailPanel.add(cartDetail);
    }

    private void addScrollPane(JPanel recordPanel) {
        JScrollPane scrollPane = new JScrollPane(recordPanel);
        scrollPane.setPreferredSize(new Dimension(400 , 600));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void addButtonPanel(JPanel recordPanel, CartItem cartItem) {
        buttonPanel = new JPanel(new GridLayout(0, 1));

        addDeleteButton(cartItem);
        addPurchaseOneButton(cartItem);

        CartItemPanel.add(buttonPanel);
        recordPanel.add(CartItemPanel);
    }

    private void addPurchaseOneButton(CartItem cartItem) {
        JButton orderOne = new JButton("바로 요청");
        orderOne.addActionListener(event -> {
            try {
                if (cartController.order(cartItem).isEmpty()) {
                    final JDialog frame = new JDialog(new Frame(), "Error", true);

                    JPanel error = new JPanel();
                    error.add(new JLabel("보유 포인트가 부족합니다."));

                    frame.getContentPane().add(error);
                    frame.setLocationRelativeTo(null);
                    frame.pack();
                    frame.setVisible(true);

                    return;
                }

                updateDisplay(cartController.getCart());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonPanel.add(orderOne);
    }

    private void addDeleteButton(CartItem cartItem) {
        JButton delete = new JButton("삭제");
        delete.addActionListener(event -> {
            try {

                cartController.delete(cartItem);
                updateDisplay(cartController.getCart());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        buttonPanel.add(delete);
    }

    private void addOrderButton(List<CartItem> items) {
        JButton orderAll = new JButton("요청 하기");
        orderAll.addActionListener(event -> {
            if (items.size() == 0) {
                return;
            }

            try {
                if (cartController.orderAll(items).isEmpty()) {
                    final JDialog frame = new JDialog(new Frame(), "Error", true);

                    JPanel error = new JPanel();
                    error.add(new JLabel("보유 포인트가 부족합니다."));

                    frame.getContentPane().add(error);
                    frame.setLocationRelativeTo(null);
                    frame.pack();
                    frame.setVisible(true);

                    return;
                }

                updateDisplay(cartController.getCart());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        detailPanel.add(orderAll);
    }

    private void addControlPanel() {
        detailPanel = new JPanel(new GridLayout(0, 1));
        this.add(detailPanel, BorderLayout.PAGE_END);
    }

    private void update() {
        this.setVisible(false);
        this.setVisible(true);
    }
}
