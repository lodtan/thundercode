package Process;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileModif {
    private String path;
    private String code;
    private int lineNumber;

    public FileModif(String path, String code, int lineNumber) {
        this.path = path;
        this.code = code;
        this.lineNumber = lineNumber;
    }

    public void setVariable() throws IOException {
        try {
            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "", newText = "";
            int i = 1;
            while ((line = reader.readLine()) != null) {
                if (i != lineNumber)
                    newText += line + "\r\n";
                else
                    newText += code;
                i++;
            }
            reader.close();
            FileWriter writer = new FileWriter(path);
            writer.write(newText);
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
