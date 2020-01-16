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
    private ArrayList<String> trendsNames;

    public TrendsPanel(String languageUsed, Controller controller){
        this.controller = controller;
        this.discoverButton = new JToggleButton("Discover unrelated technologies");
        this.discoverButton.setForeground(Color.white);
        discoverButton.setBackground(new Color(217, 83,79));
        this.discover = false;
        ArrayList<String> trendsNames = null;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel usedLanguageString = new JLabel("Language used : " + languageUsed, SwingConstants.CENTER);
        usedLanguageString.setBorder(new EmptyBorder(5,5,5,5));

        trendsHeader = new JPanel();
        trendsHeader.setLayout(new FlowLayout());
        trendsHeader.add(usedLanguageString);
        trendsHeader.add(discoverButton);
        this.add(trendsHeader);


        try {
            if (discover)
                trendsNames = this.controller.getDiscoverTrends();
            else
                trendsNames = getTrendsFor(languageUsed);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.trendsList = getTrends(trendsNames);
        this.add(trendsList);

        discoverButton.addActionListener(e -> {
            ArrayList<String> trendsNames2 = null;
            if (discoverButton.isSelected()) {
                discoverButton.setBackground(new Color(92,184,92));
                discover = true;
                trendsNames2 = this.controller.getDiscoverTrends();
                this.trendsList = getTrends(trendsNames2);
            } else {
                discoverButton.setBackground(new Color(217, 83,79));
                discover = false;
                try {
                    trendsNames2 = getTrendsFor(languageUsed);
                    this.trendsList = getTrends(trendsNames2);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            this.removeAll();
            this.add(trendsHeader);
            this.add(trendsList);
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
