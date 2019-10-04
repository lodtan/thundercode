

import org.neo4j.driver.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.neo4j.driver.Values.parameters;

public class ConnexionBd implements AutoCloseable
{

    private final Driver driver;

    public ConnexionBd()
    {
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
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }


    public ArrayList<Answer> readNode(final ArrayList<Integer> postsId )
    {
        ArrayList<Answer> resultsList = new ArrayList<Answer>();

        try ( Session session = driver.session() )
        {

            Map<String, Object> params = new HashMap<>();
            params.put( "postsId", postsId);
            String query ="MATCH (post) WHERE post.IdPost IN $postsId RETURN post";
            StatementResult result = session.run(query, params);
            System.out.println(query);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

            while (result.hasNext())
            {
                Date creationDate = null, lastActivityDate = null;
                Record res = result.next();

                String creationDateStr = res.get("post").get("CreationDate").asString();
                if (!creationDateStr.equals("null"))
                    creationDate = date.parse(creationDateStr);

                String lastActivityDateStr = res.get("post").get("LastActivityDate").asString();
                if (!lastActivityDateStr.equals("null"))
                    lastActivityDate = date.parse(lastActivityDateStr);

                int postId = res.get("post").get("IdPost").asInt();
                int score = res.get("post").get("Score").asInt();
                String body = res.get("post").get("Body").asString();
                int parentId = res.get("post").get("ParentId").asInt();

                Answer newAnswer = new Answer(postId, creationDate, score, body, lastActivityDate, parentId);

                resultsList.add(newAnswer);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultsList;
    }


    public void getNodes() throws Exception
    {
        ArrayList<Integer> idList = new ArrayList<Integer>();
        idList.add(516);
        idList.add(397);

        try ( ConnexionBd connection = new ConnexionBd() )
        {
            ArrayList<Answer> resultsList = connection.readNode(idList);
            for (int i=0; i<resultsList.size(); i++)
                System.out.println(resultsList.get(i).getBody());
        }
    }
}
