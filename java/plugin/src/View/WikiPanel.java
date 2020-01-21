package View;

import Controller.Controller;
import Model.Post;

import javax.swing.*;
import java.awt.*;

public class WikiPanel extends PostPanel {

    public WikiPanel(Post post, String tagName, Controller controller) {
        super(post, controller);
        detailsButton.setVisible(false);
        showCodeButton.setVisible(false);
        JLabel title = new JLabel("<html>" + tagName + "</html>");
        Font f = title.getFont();
        title.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        unitGroup.add(title, 0);
        JButton hideButton = new JButton("Hide explanations");
        hideButton.addActionListener(e -> togglePanel(hideButton));
        buttonsPanel.add(hideButton);
    }

    private void togglePanel(JButton hidebutton) {
        if (unitGroup.isVisible()){
            unitGroup.setVisible(false);
            hidebutton.setText("Show explanations");
        }
        else {
            unitGroup.setVisible(true);
            hidebutton.setText("Hide explanations");

        }
    }
}
