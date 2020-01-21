package View;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Process.FileModif;
import com.intellij.ui.components.JBScrollPane;
import org.jdesktop.swingx.JXComboBox;

public class SuggestionWindow extends JDialog {
    private boolean sendData;
    private String bodyPost;
    private JLabel suggestedInfo;
    private String filePath;
    FileModif fileModif;
    private int line;
    private JComboBox selectCodeDropdown;
    private JTextPane suggestedCode;

    public SuggestionWindow(JFrame parent, String title, boolean modal, String bodyPost, int line, String filePath) {
        super(parent, title, modal);
        this.bodyPost = bodyPost;
        this.filePath = filePath;

        this.suggestedCode = new JTextPane();
        suggestedCode.setContentType("text/html");
        suggestedCode.setEditable(false);
        suggestedCode.setBackground(null);
        suggestedCode.setBorder(null);

        this.line = line;
        this.setSize(500, 700);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.sendData = false;
        this.initComponent();

        this.setVisible(true);
    }


    private void initComponent() {
        JBScrollPane userCodeJsp = new JBScrollPane();
        JPanel userCode = new JPanel();
        userCode.setLayout(new BoxLayout(userCode, BoxLayout.PAGE_AXIS));

        JBScrollPane suggCodeJsp = new JBScrollPane();
        JPanel suggCode = new JPanel();
        suggCode.setLayout(new BoxLayout(suggCode, BoxLayout.PAGE_AXIS));
        String codeLine = null;
        try {
            codeLine = Files.readAllLines(Paths.get(filePath)).get(line-1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        JLabel userCodeLabel = new JLabel("<html> <b> Your code : </b> <br>" + codeLine + "</html>");
        userCodeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        userCode.add(userCodeLabel);

        JLabel suggCodeLabel = new JLabel("<html> <b> Suggested code : </b> </html>");
        suggCodeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        suggCode.add(suggCodeLabel);


        // Add Dropdown menu and update suggested code
        Vector dropdownChoices = new Vector();
        ArrayList<String> codeList = new ArrayList<>();
        Pattern pattern = Pattern.compile("<div class=\"code-block\">(.*?)</div>"); // Capture du code dans le corps du Post
        Matcher matcher = pattern.matcher(bodyPost);
        Integer codeNumber = 0;
        while (matcher.find()) {
            codeList.add(matcher.group(1));
            String dropdownChoice = "Code " + Integer.toString(codeNumber);
            dropdownChoices.add(dropdownChoice);
            codeNumber++;
        }

        selectCodeDropdown = new JXComboBox(dropdownChoices);
        suggCode.add(selectCodeDropdown);
        if (codeNumber == 0)
            selectCodeDropdown.setVisible(false);
        suggestedCode.setText(codeList.get(0));
        suggCode.add(suggestedCode);

        selectCodeDropdown.addActionListener(e -> {
            String code = codeList.get(selectCodeDropdown.getSelectedIndex());
            suggestedCode.setText(code);
            fileModif = new FileModif(filePath, code, line);
            suggCode.revalidate();
        });


        userCodeJsp.setViewportView(userCode);
        suggCodeJsp.setViewportView(suggCode);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));


        JPanel control = new JPanel();
        control.setLayout(new BoxLayout(control, BoxLayout.LINE_AXIS));


        JButton okButton = new JButton("Switch code");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                fileModif.changeCode();
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
