import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileModif {
    private String path;

    public FileModif(String path) {
        this.path = path;
        try {
            setVariable(1, "ok", path);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(path+"/src/testFile1.java");
    }

    public void setVariable(int lineNumber, String data, String path) throws IOException {
        try {
            File file = new File(path + "/src/test1.java");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "", newText = "";
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if (i != lineNumber)
                    newText += line + "\r\n";
                else
                    newText += data;
                i++;
            }
            reader.close();
            FileWriter writer = new FileWriter(path + "/src/test1.java");
            writer.write(newText);
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
