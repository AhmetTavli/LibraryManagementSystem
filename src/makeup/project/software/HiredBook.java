package makeup.project.software;

import java.sql.Date;

public class HiredBook {
    private int hb_id;
    private int u_id;
    private int b_id;
    private Date h_date;
    private Date h_return;
    private int timeLeft;
    private String penalty;
    private String action_confirm;

    public int getHb_id() {
        return hb_id;
    }

    public void setHb_id(int hb_id) {
        this.hb_id = hb_id;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getB_id() {
        return b_id;
    }

    public void setB_id(int b_id) {
        this.b_id = b_id;
    }

    public Date getH_date() {
        return h_date;
    }

    public void setH_date(Date h_date) {
        this.h_date = h_date;
    }

    public Date getH_return() {
        return h_return;
    }

    public void setH_return(Date h_return) {
        this.h_return = h_return;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public String getAction_confirm() {
        return action_confirm;
    }

    public void setAction_confirm(String action_confirm) {
        this.action_confirm = action_confirm;
    }
}
