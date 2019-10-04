import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import org.jvnet.ws.wadl.Link;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PostPanel extends JPanel {
    JLabel textField;
    JButton detailsButton;
    JButton showCodeButton;

    public PostPanel(Post post) {

        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));


        //Create the text field format, and then the text field.
        textField = new JLabel(post.body);
        detailsButton = new JButton("Details");
        showCodeButton = new JButton("Code");

        detailsButton.addActionListener(e -> showDetails());
        showCodeButton.addActionListener(e -> showCode());


        JPanel unitGroup = new JPanel() {
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
            public Dimension getPreferredSize() {
                return new Dimension(300,
                        super.getPreferredSize().height);
            }
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };

        unitGroup.setLayout(new BoxLayout(unitGroup,
                BoxLayout.PAGE_AXIS));

        unitGroup.setBorder(BorderFactory.createEmptyBorder(
                0,0,0,5));

        unitGroup.add(textField);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel,
                BoxLayout.PAGE_AXIS));

        buttonsPanel.add(detailsButton);
        buttonsPanel.add(showCodeButton);

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(unitGroup);
        add(buttonsPanel);
        unitGroup.setAlignmentY(TOP_ALIGNMENT);
        buttonsPanel.setAlignmentY(TOP_ALIGNMENT);


    }

    private void showDetails() {
        setVisible(false);
        //PostDetail postDetail = new PostDetail();
    }

    private void showCode() {

    }
}
