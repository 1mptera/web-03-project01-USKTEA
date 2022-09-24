package view;

import controller.ImagePathController;
import controller.UserPanelController;
import models.Service;
import models.User;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.Optional;

public class UserPanel extends JPanel {
    private UserPanelController userPanelController;
    private Optional<User> session;
    private ImagePathController imagePathController;

    public UserPanel(UserPanelController userPanelController) throws FileNotFoundException {
        this.userPanelController = userPanelController;
        this.imagePathController = new ImagePathController();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        getSession();
        initUserPanel();
    }

    public void getSession() {
        session = userPanelController.getSession();
    }

    private void initUserPanel() throws FileNotFoundException {
        if (session.isPresent()) {
            User user = session.get();

            initHeader();
            initServicePanel(user);
        }
    }

    private void initHeader() {
        User user = session.get();

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel userId = new JLabel(user.id() + "가 요청한 서비스 목록");
        userId.setFont(new Font("NanumGothic", Font.BOLD, 25));
        header.add(userId);

        this.add(header, BorderLayout.NORTH);
    }

    private void initServicePanel(User user) throws FileNotFoundException {
        JPanel servicePanel = new JPanel();
        servicePanel.setLayout(new GridLayout(0, 3));

        for (Service service : userPanelController.myService(user)) {
            String path = imagePathController.getPath(service);
            ImageIcon imageIcon = new ImageIcon(path);
            JButton userService = new JButton(imageIcon);

            servicePanel.add(userService);
        }

        addScrollPane(servicePanel);
    }

    private void addScrollPane(JPanel servicePanel) {
        JScrollPane scrollPane = new JScrollPane(servicePanel);
        scrollPane.setPreferredSize(new Dimension(1000 , 600));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.add(scrollPane);
        this.setVisible(false);
        this.setVisible(true);
    }
}
