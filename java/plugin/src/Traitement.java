
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Traitement {
    private String consoleOutput;
    public Traitement(String consoleOutput){
        System.out.println("Prêt au traitement");
        this.consoleOutput = consoleOutput;
        System.out.println(consoleOutput);
        String dir = System.getProperty("idea.plugins.path");
        dir = dir.replaceAll("\\/", "/");
        Properties properties = new Properties();
        String pathToPlugin = dir+"/Plugin/classes/properties/connexion.properties";
        try{

            FileInputStream in = new FileInputStream(pathToPlugin);
            properties.load(in);
            in.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String uri = properties.getProperty("URI");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        ArrayList<Integer> idList = new ArrayList<Integer>();
        idList.add(31);
        idList.add(29);
        ConnexionBd connection = new ConnexionBd( uri,  user, password );
        ArrayList<Answer> resultsList = connection.readNode(idList);
        for (int i=0; i<resultsList.size(); i++)
            System.out.println(i + resultsList.get(i).getBody());


    }


}
