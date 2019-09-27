import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class DriverLifecycleExample implements AutoCloseable
{
    private final Driver driver;
    public DriverLifecycleExample( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }
    @Override
    public void close() throws Exception
    {
        driver.close();
    }
}

