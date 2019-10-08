package View;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SuggestionWindow extends JDialog {
    private boolean sendData;
    private String code;
    private JLabel suggestedInfo;


    public SuggestionWindow(JFrame parent, String title, boolean modal, String code, int line){
        super(parent, title, modal);
        this.code = code;


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

        JLabel codePane = new JLabel("<html>"+code+"</html>");

        codeInfo.add(codePane);


        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));

        JPanel control = new JPanel();
        control.setLayout(new BoxLayout(control, BoxLayout.LINE_AXIS));


        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                //zInfo = new ZDialogInfo(nom.getText(), (String)sexe.getSelectedItem(), getAge(), (String)cheveux.getSelectedItem() ,getTaille());
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

        //content.add(control);
        content.add(codeInfo);

        this.getContentPane().add(content, BorderLayout.CENTER);
        this.getContentPane().add(control, BorderLayout.SOUTH);
    }

}
