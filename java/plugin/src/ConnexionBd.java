

import org.neo4j.driver.*;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.neo4j.driver.Values.parameters;

public class ConnexionBd implements AutoCloseable
{

    private final Driver driver;

    public ConnexionBd(String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public void writeNode( final String message )
    {
        try ( Session session = driver.session() )
        {
            String command = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run( "CREATE (post:Post) " +
                                    "SET a.message = $message " +
                                    "RETURN a.message + ', from node ' + id(a)", parameters( "message", message ) );


                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( command );
        }
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

        Properties properties = new Properties();
        FileInputStream in = new FileInputStream("properties/connexion.properties");
        properties.load(in);
        in.close();
        String uri = properties.getProperty("URI");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        try ( ConnexionBd connection = new ConnexionBd( uri,
                user, password ) )
        {
            ArrayList<Answer> resultsList = connection.readNode(idList);
            for (int i=0; i<resultsList.size(); i++)
                System.out.println(resultsList.get(i).getBody());
        }
    }
}
