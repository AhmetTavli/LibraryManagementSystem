package makeup.project.software;

public class UserMail {
    private int u_mail; // user mail-id
    private String mail_from;
    private String body;
    private int user_id;

    public int getU_mail() {
        return u_mail;
    }

    public void setU_mail(int u_mail) {
        this.u_mail = u_mail;
    }

    public String getMail_from() {
        return mail_from;
    }

    public void setMail_from(String mail_from) {
        this.mail_from = mail_from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
