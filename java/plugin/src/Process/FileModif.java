package Process;

import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;


public class FileModif {
    private String path;
    private String code;
    private int lineNumber;

    public FileModif(String path, String code, int lineNumber) {
        this.path = path;

        this.code = Utils.switchHTMLToCode(code);
        this.code = this.code.substring(3, this.code.length()-4);
        this.lineNumber = lineNumber;
    }

    public void setVariable() {
        try {
            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line, newText = "";
            int i = 1;
            while ((line = reader.readLine()) != null) {
                int nbTab=0;
                if (i == lineNumber -2){
                    System.out.println(lineNumber);
                    nbTab = StringUtils.countMatches(line, "\t");
                    System.out.println(line);
                    System.out.println(nbTab);

                }
                if (i != lineNumber)
                    newText += line + "\r\n";

                else {
                    code.replaceAll("\n", "\n" + "\t".repeat(nbTab));
                    newText += code;
                }
                i++;
            }
            reader.close();
            FileWriter writer = new FileWriter(path);
            writer.write(newText);
            writer.close();
            try {
                Robot robot = new Robot();

                robot.keyPress(KeyEvent.VK_ALT);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_Y);

                robot.keyRelease(KeyEvent.VK_ALT);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyRelease(KeyEvent.VK_Y);



            } catch (AWTException e) {
                e.printStackTrace();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
