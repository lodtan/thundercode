package Model;

import java.util.Date;

public class Comment {
    private int idComment;
    private String text;
    private int score;

    private Date commentDate;
    private int idUser;
    private String userName;


    public Comment(int idComment, String text, int score, Date commentDate, int idUser, String userName) {
        this.idComment = idComment;
        this.text = text.replaceAll("`(.*?)`", "<span class=\"lonely-code\">$1</span>")
                .replaceAll("\\*\\*(.*.)\\*\\*", "<b>$1</b>")
                .replaceAll("\\*(.*?)\\*", "<i>$1</i>")
                .replace("\\n", "")
                .replace("\\", "").replace("<br>", " ");


        this.score = score;
        this.commentDate = commentDate;
        this.idUser = idUser;
        this.userName = userName;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }


}
