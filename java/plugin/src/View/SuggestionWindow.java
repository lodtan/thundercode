package View;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import Process.FileModif;

public class SuggestionWindow extends JDialog {
    private boolean sendData;
    private String code;
    private JLabel suggestedInfo;
    private String filePath;
    FileModif fileModif;



    public SuggestionWindow(JFrame parent, String title, boolean modal, String code, int line, String filePath){
        super(parent, title, modal);
        this.code = code;
        this.filePath = filePath;
        this.fileModif = new FileModif(filePath, code, line);

        this.setSize(500, 200);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.sendData = false;
        this.initComponent();

        this.setVisible(true);
    }


    private void initComponent(){
        JPanel codeInfo = new JPanel();
        codeInfo.setPreferredSize(new Dimension(220, 60));
        codeInfo.setLayout(new BoxLayout(codeInfo, BoxLayout.PAGE_AXIS));
        codeInfo.setBorder(BorderFactory.createTitledBorder("Your code : "));

        JPanel codeSuggPanel = new JPanel();
        codeSuggPanel.setPreferredSize(new Dimension(220, 60));
        codeSuggPanel.setLayout(new BoxLayout(codeSuggPanel, BoxLayout.PAGE_AXIS));
        codeSuggPanel.setBorder(BorderFactory.createTitledBorder("Suggested code : "));

        JLabel codePane = new JLabel("<html>"+code+"</html>");
        codeInfo.add(codePane);

        JLabel codeSuggested = new JLabel("<html>"+code+"</html>");
        codeSuggPanel.add(codeSuggested);


        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));

        JPanel control = new JPanel();
        control.setLayout(new BoxLayout(control, BoxLayout.LINE_AXIS));


        JButton okButton = new JButton("Switch code");
        okButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                try {
                    fileModif.setVariable();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setVisible(false);
            }

        });

        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
            }
        });

        control.add(okButton);
        control.add(cancelButton);

        content.add(codeInfo);
        content.add(codeSuggPanel);

        this.getContentPane().add(content, BorderLayout.CENTER);
        this.getContentPane().add(control, BorderLayout.SOUTH);
    }

}
