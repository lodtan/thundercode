package View;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HoverButton implements MouseListener {


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        JButton c = (JButton) mouseEvent.getComponent();
        c.setContentAreaFilled(false);
        c.setBackground(c.getBackground().darker());
        c.setOpaque(true);

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        JButton c = (JButton) mouseEvent.getComponent();
        c.setContentAreaFilled(false);
        c.setBackground(c.getBackground().brighter());
        c.setOpaque(true);

    }
}
