package Model;

import java.util.Date;

public class WikiPost extends Post {
    public WikiPost(int id, Date creationDate, String body) {
        super(id, creationDate, 0, body, 0, null);
    }
}
