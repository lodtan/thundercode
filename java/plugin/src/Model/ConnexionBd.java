package Model;

import Process.Utils;
import org.neo4j.driver.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnexionBd implements AutoCloseable {

    private final Driver driver;

    public ConnexionBd() {
        Properties properties = new Properties();

        this.getClass().getResourceAsStream("/properties/connexion.properties");
        try {

            properties.load(this.getClass().getResourceAsStream("/properties/connexion.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String uri = properties.getProperty("URI");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public ConnexionBd(String s) {
        Properties properties = new Properties();

        this.getClass().getResourceAsStream("/properties/connexion.properties");
        try {

            properties.load(this.getClass().getResourceAsStream("/properties/connexion.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String uri = properties.getProperty("URI2");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() {
        driver.close();
    }

    public Post getQuestionFromAnswer(int idAnswer) {
        try (Session session = driver.session()) {
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

            Map<String, Object> paramsUser = new HashMap<>();
            paramsUser.put("idPost", postId);
            String queryUser = "MATCH (post:Question)<-[:WRITE]-(u:User) where post.IdPost=$idPost return u";
            StatementResult resultUser = session.run(queryUser, paramsUser);
            String userName = "Unknown User";
            int idUser = 0;
            if (resultUser.hasNext()) {
                Record resUser = resultUser.next();
                userName = resUser.get("u").get("DisplayName").asString();
                idUser = resUser.get("u").get("IdUser").asInt();
            }
            return new Question(postId, creationDate, score, body, title, tags, viewCount, acceptedAnswerId, idUser, userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public ArrayList<Answer> getAnswersFromQuestion(int id) {
        ArrayList<Answer> resultsList = new ArrayList<>();

        try (Session session = driver.session()) {

            Map<String, Object> params = new HashMap<>();
            params.put("idPost", id);
            String query = "MATCH (post:Answer)-[:ANSWERS]->(a:Question) where a.IdPost=$idPost return post order by post.Score desc";
            StatementResult result = session.run(query, params);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

            while (result.hasNext()) {
                Date creationDate = null;
                Record res = result.next();

                String creationDateStr = res.get("post").get("CreationDate").asString();
                if (!creationDateStr.equals("null"))
                    creationDate = date.parse(creationDateStr);

                int postId = res.get("post").get("IdPost").asInt();
                int score = res.get("post").get("Score").asInt();
                String body = res.get("post").get("Body").asString();
                int parentId = res.get("post").get("ParentId").asInt();

                Map<String, Object> paramsUser = new HashMap<>();
                paramsUser.put("idPost", postId);
                String queryUser = "MATCH (u:User)-[:WRITE]->(post:Answer) where post.IdPost=$idPost return u order by post.Score desc";
                StatementResult resultUser = session.run(queryUser, paramsUser);
                String userName = "Unknown User";
                int idUser = 0;
                if (resultUser.hasNext()) {
                    Record resUser = resultUser.next();
                    userName = resUser.get("u").get("DisplayName").asString();
                    idUser = resUser.get("u").get("IdUser").asInt();
                }

                Answer newAnswer = new Answer(postId, creationDate, score, body, parentId, idUser, userName);

                resultsList.add(newAnswer);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultsList;
    }

    public ArrayList<Question> searchNodesByTags(String searchField, String tagsField) {
        ArrayList<Question> resultsList = new ArrayList<>();

        searchField = "'" + Utils.cleanString(searchField) + "'" ;

        try (Session session = driver.session()) {
            String[] tagsTab = tagsField.split(" ");
            String query = "CALL db.index.fulltext.queryNodes('postsIndex', $searchField) YIELD node, score where node.Score>0  ";
            for (String s : tagsTab) {
                query += "AND node.Tags CONTAINS '<" + s + ">' ";
            }
            query += "RETURN node ORDER BY node.Score DESC LIMIT 10";
            Map<String, Object> params = new HashMap<>();
            params.put("searchField", searchField);
            //params.put("tags", tagsList);
            StatementResult result = session.run(query, params);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            while (result.hasNext()) {
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

                Map<String, Object> paramsUser = new HashMap<>();
                paramsUser.put("idPost", postId);
                String queryUser = "MATCH (u:User)-[:WRITE]->(post:Question) where post.IdPost=$idPost return u order by post.Score desc";
                StatementResult resultUser = session.run(queryUser, paramsUser);
                String userName = "Unknown User";
                int idUser = 0;
                if (resultUser.hasNext()) {
                    Record resUser = resultUser.next();
                    userName = resUser.get("u").get("DisplayName").asString();
                    idUser = resUser.get("u").get("IdUser").asInt();
                }
                Question newQuestion = new Question(postId, creationDate, score, body, title, tags, viewCount, acceptedAnswerId, idUser, userName);

                resultsList.add(newQuestion);
            }
            return resultsList;
        } catch (Exception e) {
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
            if (result.hasNext()) {
                Date creationDate = null;
                Record res = result.next();
                int id = res.get("post").get("IdPost").asInt();
                String body = res.get("post").get("Body").asString();
                String creationDateStr = res.get("post").get("CreationDate").asString();
                if (!creationDateStr.equals("null"))
                    creationDate = date.parse(creationDateStr);


                wikiPost = new WikiPost(id, creationDate, body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wikiPost;
    }

    public ArrayList<Question> getQuestionsFromTag(String tagName) {
        ArrayList<Question> resultsList = new ArrayList<>();
        try (Session session = driver.session()) {
            String query = "MATCH (node:Question)-[HAS_TAG]->(t:Tag) where t.TagName = $tagName return node ORDER BY node.ViewCount DESC LIMIT 25";
            Map<String, Object> params = new HashMap<>();
            params.put("tagName", tagName);
            StatementResult result = session.run(query, params);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            while (result.hasNext()) {
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

                Map<String, Object> paramsUser = new HashMap<>();
                paramsUser.put("idPost", postId);
                String queryUser = "MATCH (u:User)-[:WRITE]->(post:Question) where post.IdPost=$idPost return u";
                StatementResult resultUser = session.run(queryUser, paramsUser);
                String userName = "Unknown User";
                int idUser = 0;
                if (resultUser.hasNext()) {
                    Record resUser = resultUser.next();
                    userName = resUser.get("u").get("DisplayName").asString();
                    idUser = resUser.get("u").get("IdUser").asInt();
                }
                Question newQuestion = new Question(postId, creationDate, score, body, title, tags, viewCount, acceptedAnswerId, idUser, userName);

                resultsList.add(newQuestion);
            }
            return resultsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Question> getRecommendationsFrom(String tagName, String language) {
        ArrayList<Question> resultsList = new ArrayList<>();
        try (Session session = driver.session()) {
            String query = "MATCH (a:Tag)<-[b:HAS_TAG]-(node:Question)-[c:HAS_TAG]->(t:Tag) where t.TagName = $tagName AND a.TagName = $language return node ORDER BY node.ViewCount DESC LIMIT 25";
            Map<String, Object> params = new HashMap<>();
            params.put("language", language);
            params.put("tagName", tagName);
            StatementResult result = session.run(query, params);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            while (result.hasNext()) {
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

                Map<String, Object> paramsUser = new HashMap<>();
                paramsUser.put("idPost", postId);
                String queryUser = "MATCH (u:User)-[:WRITE]->(post:Question) where post.IdPost=$idPost return u";
                StatementResult resultUser = session.run(queryUser, paramsUser);
                String userName = "Unknown User";
                int idUser = 0;
                if (resultUser.hasNext()) {
                    Record resUser = resultUser.next();
                    userName = resUser.get("u").get("DisplayName").asString();
                    idUser = resUser.get("u").get("IdUser").asInt();
                }
                Question newQuestion = new Question(postId, creationDate, score, body, title, tags, viewCount, acceptedAnswerId, idUser, userName);

                resultsList.add(newQuestion);
            }
            return resultsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Comment> getCommentFrom(int idPost, String typePost) {
        ArrayList<Comment> resultsList = new ArrayList<>();
        try (Session session = driver.session()) {
            String query = "MATCH p=(c:Comment)-[r:COMMENTS]->(q:"+typePost+") where q.IdPost=$idPost RETURN c order by c.IdComment";
            Map<String, Object> params = new HashMap<>();
            params.put("idPost", idPost);

            StatementResult result = session.run(query, params);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            while (result.hasNext()) {
                Record res = result.next();
                int idComment = res.get("c").get("IdComment").asInt();
                String text = res.get("c").get("Text").asString();
                int score = res.get("c").get("Score").asInt();
                Date commentDate = null;
                String commentDateStr = res.get("c").get("CreationDate").asString();
                if (!commentDateStr.equals("null"))
                    commentDate = date.parse(commentDateStr);

                Map<String, Object> paramsUser = new HashMap<>();
                paramsUser.put("idComment", idComment);
                String queryUser = "MATCH p=(u:User)-[r:WRITE_COMMENT]->(c:Comment) where c.IdComment=$idComment RETURN u";
                StatementResult resultUser = session.run(queryUser, paramsUser);
                String userName = "Unknown User";
                int idUser = 0;
                if (resultUser.hasNext()) {
                    Record resUser = resultUser.next();
                    idUser = resUser.get("u").get("IdUser").asInt();
                    userName = resUser.get("u").get("DisplayName").asString();
                }
                resultsList.add(new Comment(idComment, text, score, commentDate, idUser, userName));
            }
            return resultsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<Answer> searchAnswerByError(String errorText, boolean summarized) {
        ArrayList<Answer> resultsList = new ArrayList<>();

        errorText = "'" + Utils.cleanString(errorText) + "'";
        System.out.println(errorText);
        try (Session session = driver.session()) {
            String query = "CALL db.index.fulltext.queryNodes('postsIndex', $searchField) YIELD node RETURN node.IdPost as id, node.Title as title LIMIT 10";
            Map<String, Object> params = new HashMap<>();
            params.put("searchField", errorText);
            StatementResult result = session.run(query, params);
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

            while (result.hasNext()) {
                Record resQ = result.next();
                int questionId = resQ.get("id").asInt();
                String title = resQ.get("title").asString();
                String queryAnswer = "MATCH (a:Answer)-[r:ANSWERS]->(q:Question) where q.IdPost=$idQuestion RETURN a as node order by a.Score desc LIMIT 1";
                Map<String, Object> paramsQ = new HashMap<>();
                paramsQ.put("idQuestion", questionId);
                StatementResult resultQ = session.run(queryAnswer, paramsQ);
                while (resultQ.hasNext()) {

                    Date creationDate = null;
                    Record res = resultQ.next();

                    String creationDateStr = res.get("node").get("CreationDate").asString();
                    if (!creationDateStr.equals("null"))
                        creationDate = date.parse(creationDateStr);


                    int postId = res.get("node").get("IdPost").asInt();
                    int score = res.get("node").get("Score").asInt();
                    String body;
                    if (summarized) {
                        body = res.get("node").get("Summary").asString();
                        ArrayList<String> codes = new ArrayList<String>();
                        Matcher m = Pattern.compile("(<pre><code>.*?</code></pre>)")
                                .matcher(res.get("node").get("Body").asString());
                        while (m.find()) {
                            codes.add(m.group());
                        }
                        for (String code : codes){
                            try {
                                body = body.replaceFirst("codeBlock.", code);
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                        body = res.get("node").get("Body").asString();
                    int parentId = res.get("node").get("ParentId").asInt();
                    Map<String, Object> paramsUser = new HashMap<>();
                    paramsUser.put("idPost", postId);
                    String queryUser = "MATCH (u:User)-[:WRITE]->(post:Answer) where post.IdPost=$idPost return u order by post.Score desc";
                    StatementResult resultUser = session.run(queryUser, paramsUser);
                    String userName = "Unknown User";
                    int idUser = 0;
                    if (resultUser.hasNext()) {
                        Record resUser = resultUser.next();
                        userName = resUser.get("u").get("DisplayName").asString();
                        idUser = resUser.get("u").get("IdUser").asInt();
                    }
                    Answer newAnswer = new Answer(postId, creationDate, score, body, parentId, idUser, userName, title);

                    resultsList.add(newAnswer);
                }
            }
            return resultsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getDiscoverTrends() {
        ArrayList<String> resultsList = new ArrayList<>();
        String currentYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR)-1);

        try (Session session = driver.session()) {
            String query = "MATCH (n:Tag)<-[:HAS_TAG]-(q:Question) WHERE q.CreationDate CONTAINS $currentYear RETURN n, count(q) as NbPost ORDER BY NbPost DESC LIMIT 25";
            Map<String, Object> params = new HashMap<>();

            params.put("currentYear", currentYear);

            StatementResult result = session.run(query, params);

            while (result.hasNext()) {
                Record res = result.next();
                String tagName = res.get("n").get("TagName").asString();

                resultsList.add(tagName);
            }
            return resultsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
