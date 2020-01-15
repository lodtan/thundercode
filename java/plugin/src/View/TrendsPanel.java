package View;

import Controller.Controller;
import Model.Post;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import Process.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static Process.Utils.getTrendsFor;


public class TrendsPanel extends JPanel {
    private Controller controller;
    private JToggleButton discoverButton;
    private Boolean discover;
    private JPanel trendsHeader;
    private JPanel trendsList;

    public TrendsPanel(String languageUsed, Controller controller){
        this.controller = controller;
        this.discoverButton = new JToggleButton("Discover unrelated technologies");
        this.discoverButton.setForeground(Color.white);
        discoverButton.setBackground(new Color(217, 83,79));
        this.discover = false;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel usedLanguageString = new JLabel("Language used : " + languageUsed, SwingConstants.CENTER);
        usedLanguageString.setBorder(new EmptyBorder(5,5,5,5));

        trendsHeader = new JPanel();
        trendsHeader.setLayout(new FlowLayout());
        trendsHeader.add(usedLanguageString);
        trendsHeader.add(discoverButton);
        this.add(trendsHeader);

        ArrayList<String> trends = null;
        try {
            trends = getTrendsFor(languageUsed);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.trendsList = getTrends(trends);
        this.add(trendsList);

        ArrayList<String> finalTrends = trends;
        discoverButton.addActionListener(e -> {
            if (discoverButton.isSelected()) {
                discoverButton.setBackground(new Color(92,184,92));
                discoverButton.setOpaque(true);
                discover = false;
            } else {
                discoverButton.setBackground(new Color(217, 83,79));
                discover = true;
            }
            this.trendsList = getTrends(finalTrends);
        });
    }

    public JPanel getTrends(ArrayList<String> trends) {
        trendsList = new JPanel();
        GridLayout layout = new GridLayout(10,1);
        layout.setHgap(10);
        layout.setVgap(10);
        trendsList.setLayout(layout);

        for (int i = 0; i < 5; i++) {
            String trendName = trends.get(i);
            JButton trend = new JButton(trendName);
            trend.addActionListener(event -> {
                controller.displayPostsFromTrend(trendName, discover);
            });
            trendsList.add(trend);
        }
        return trendsList;
    }
}
