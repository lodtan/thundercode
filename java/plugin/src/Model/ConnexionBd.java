package Model;

import org.jsoup.parser.Tag;
import org.neo4j.driver.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConnexionBd implements AutoCloseable
{

    private final Driver driver;

    public ConnexionBd()
    {
        Properties properties = new Properties();
        this.getClass().getResourceAsStream("/properties/connexion.properties");
        try{

            properties.load(this.getClass().getResourceAsStream("/properties/connexion.properties"));
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
            String query ="MATCH (post:Answer) WHERE post.IdPost IN $postsId RETURN post";
            StatementResult result = session.run(query, params);
            System.out.println(query);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

            while (result.hasNext())
            {
                Date creationDate = null;
                Record res = result.next();

                String creationDateStr = res.get("post").get("CreationDate").asString();
                if (!creationDateStr.equals("null"))
                    creationDate = date.parse(creationDateStr);

                int postId = res.get("post").get("IdPost").asInt();
                int score = res.get("post").get("Score").asInt();
                String body = res.get("post").get("Body").asString();
                int parentId = res.get("post").get("ParentId").asInt();

                Answer newAnswer = new Answer(postId, creationDate, score, body, parentId);

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

    public Post getQuestionFromAnswer(int idAnswer){
        try ( Session session = driver.session() ) {
            String query = "MATCH (a:Answer)-[:ANSWERS]->(post:Question) where a.IdPost=$idPost return post";
            Map<String, Object> params = new HashMap<>();
            params.put("idPost", idAnswer);
            StatementResult result = session.run(query, params);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

            Date creationDate = null;
            Record res = result.next();

            String creationDateStr = res.get("post").get("CreationDate").asString();
            if (!creationDateStr.equals("null"))
                creationDate = date.parse(creationDateStr);


            int postId = res.get("post").get("IdPost").asInt();
            int score = res.get("post").get("Score").asInt();
            String body = res.get("post").get("Body").asString();
            int viewCount = res.get("post").get("ViewCount").asInt();
            int acceptedAnswerId = res.get("post").get("AcceptedAnswerId").asInt();
            String tags = res.get("post").get("Tags").asString();
            String title = res.get("post").get("Title").asString();

            Question postQuestion = new Question(postId, creationDate, score, body, title, tags, viewCount, acceptedAnswerId);
            return postQuestion;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public ArrayList<Answer> getAnswersFromQuestion(int id) {
        ArrayList<Answer> resultsList = new ArrayList<Answer>();

        try ( Session session = driver.session() )
        {

            Map<String, Object> params = new HashMap<>();
            params.put("idPost", id);
            String query = "MATCH (post:Answer)-[:ANSWERS]->(a:Question) where a.IdPost=$idPost return post order by post.Score desc";
            StatementResult result = session.run(query, params);
            System.out.println(query);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

            while (result.hasNext())
            {
                Date creationDate = null;
                Record res = result.next();

                String creationDateStr = res.get("post").get("CreationDate").asString();
                if (!creationDateStr.equals("null"))
                    creationDate = date.parse(creationDateStr);

                int postId = res.get("post").get("IdPost").asInt();
                int score = res.get("post").get("Score").asInt();
                String body = res.get("post").get("Body").asString();
                int parentId = res.get("post").get("ParentId").asInt();

                Answer newAnswer = new Answer(postId, creationDate, score, body, parentId);

                resultsList.add(newAnswer);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultsList;
    }

    public ArrayList<Question> searchNodes(String searchField) {
        ArrayList<Question> resultsList = new ArrayList<Question>();
        try ( Session session = driver.session() ) {
            String query = "CALL db.index.fulltext.queryNodes('postsIndex', $searchField) YIELD node, score where score>0.7 and node.Score>50 RETURN node LIMIT 10";
            Map<String, Object> params = new HashMap<>();
            params.put("searchField", searchField);
            StatementResult result = session.run(query, params);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

            while (result.hasNext())
            {
                Date creationDate = null;
                Record res = result.next();

                String creationDateStr = res.get("node").get("CreationDate").asString();
                if (!creationDateStr.equals("null"))
                    creationDate = date.parse(creationDateStr);


                int postId = res.get("node").get("IdPost").asInt();
                int score = res.get("node").get("Score").asInt();
                String body = res.get("node").get("Body").asString();
                int viewCount = res.get("node").get("ViewCount").asInt();
                int acceptedAnswerId = res.get("node").get("AcceptedAnswerId").asInt();
                String tags = res.get("node").get("Tags").asString();
                String title = res.get("node").get("Title").asString();

                Question newQuestion = new Question(postId, creationDate, score, body, title, tags, viewCount, acceptedAnswerId);

                resultsList.add(newQuestion);
            }
            return  resultsList;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Question> searchNodesByTags(String searchField, String tagsField) {
        ArrayList<Question> resultsList = new ArrayList<Question>();
        try ( Session session = driver.session() ) {

            String[] tagsTab = tagsField.split(" ");

            String query = "CALL db.index.fulltext.queryNodes('postsIndex', $searchField) YIELD node, score where score>0.7 and node.Score>50 ";
            for (int i=0 ; i<tagsTab.length; i++) {
                query += "AND node.Tags CONTAINS '<" + tagsTab[i] + ">' ";
            }

            query += "RETURN node LIMIT 10";
            Map<String, Object> params = new HashMap<>();
            params.put("searchField", searchField);
            //params.put("tags", tagsList);
            StatementResult result = session.run(query, params);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

            while (result.hasNext())
            {
                Date creationDate = null;
                Record res = result.next();

                String creationDateStr = res.get("node").get("CreationDate").asString();
                if (!creationDateStr.equals("null"))
                    creationDate = date.parse(creationDateStr);


                int postId = res.get("node").get("IdPost").asInt();
                int score = res.get("node").get("Score").asInt();
                String body = res.get("node").get("Body").asString();
                int viewCount = res.get("node").get("ViewCount").asInt();
                int acceptedAnswerId = res.get("node").get("AcceptedAnswerId").asInt();
                String tags = res.get("node").get("Tags").asString();
                String title = res.get("node").get("Title").asString();

                Question newQuestion = new Question(postId, creationDate, score, body, title, tags, viewCount, acceptedAnswerId);

                resultsList.add(newQuestion);
            }
            return  resultsList;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public WikiPost getWikiPostFromTag(String tagName) {
        WikiPost wikiPost = null;
        try (Session session = driver.session()) {
            Map<String, Object> params = new HashMap<>();
            params.put("tagName", tagName);
            String query = "MATCH (post:WikiPost)-[:EXPLAINS]->(t:Tag) where t.TagName=$tagName return post";
            StatementResult result = session.run(query, params);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            if (result.hasNext()){
                Date creationDate = null;
                Record res = result.next();
                int id = res.get("post").get("IdPost").asInt();
                String body = res.get("post").get("Body").asString();
                String creationDateStr = res.get("post").get("CreationDate").asString();
                if (!creationDateStr.equals("null"))
                    creationDate = date.parse(creationDateStr);


                wikiPost = new WikiPost(id, creationDate, body);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return wikiPost;
    }
}
