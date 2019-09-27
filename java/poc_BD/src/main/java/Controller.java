import org.neo4j.driver.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

public class Controller implements AutoCloseable
{
    private final Driver driver;

    public Controller(String uri, String user, String password )
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


    public void readNode( final ArrayList<Integer> postsId )
    {
        try ( Session session = driver.session() )
        {
            ArrayList<Post> resultsList = new ArrayList<Post>();

            Map<String, Object> params = new HashMap<>();
            params.put( "postsId", postsId);
            String query ="MATCH (post:Post) WHERE post.IdPost IN $postsId RETURN post";
            StatementResult result = session.run(query, params);
            while (result.hasNext())
            {
                Record res = result.next();
                System.out.println(res.get("post").get("Score"));

                Answer newAnswer = new Answer();

                /*(int id, Timestamp creationDate, int score, String body, int ownerUserId, String lastEditorDisplayName, java.sql.Timestamp
                lastEditDate, java.sql.Timestamp lastActivityDate, int commentCount, int parentId) { */
               // Integer postId = res.get("post.IdPost").asInt();
                resultsList.add(postId);
            }
            //System.out.println(resultsList.size());
            //for (int i = 0; i < resultsList.size(); i++) System.out.println(resultsList.get(i));
        }
    }


    public static void main( String... args ) throws Exception
    {
        ArrayList<Integer> resultsList = new ArrayList<Integer>();
        resultsList.add(470);
        resultsList.add(471);

        try ( Controller connection = new Controller( "bolt://localhost:7687",
                "elodie", "lab" ) )
        {
            connection.readNode(resultsList);
        }
    }
}