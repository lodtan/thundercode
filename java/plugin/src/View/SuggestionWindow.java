package View;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import Process.FileModif;
import com.intellij.ui.components.JBScrollPane;

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

        this.setSize(500, 700);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.sendData = false;
        this.initComponent();

        this.setVisible(true);
    }


    private void initComponent(){
        JBScrollPane userCodeJsp = new JBScrollPane();
        JPanel userCode = new JPanel();
        userCode.setLayout(new BoxLayout(userCode, BoxLayout.PAGE_AXIS));

        JBScrollPane suggCodeJsp = new JBScrollPane();
        JPanel suggCode = new JPanel();
        suggCode.setLayout(new BoxLayout(suggCode, BoxLayout.PAGE_AXIS));

        JLabel userCodeLabel = new JLabel("<html> <b> Your code : </b> <br>"+code+"</html>");
        userCodeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        userCode.add(userCodeLabel);

        JLabel suggCodeLabel = new JLabel("<html> <b> Suggested code : </b> <br>"+code+"</html>");
        suggCodeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        suggCode.add(suggCodeLabel);

        userCodeJsp.setViewportView(userCode);
        suggCodeJsp.setViewportView(suggCode);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));


        JPanel control = new JPanel();
        control.setLayout(new BoxLayout(control, BoxLayout.LINE_AXIS));


        JButton okButton = new JButton("Switch code");
        okButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                fileModif.setVariable();
                setVisible(false);
            }

        });

        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> setVisible(false));

        control.add(okButton);
        control.add(cancelButton);
        control.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        content.add(userCodeJsp);
        content.add(suggCodeJsp);
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.getContentPane().add(content, BorderLayout.CENTER);
        this.getContentPane().add(control, BorderLayout.SOUTH);
    }

}
