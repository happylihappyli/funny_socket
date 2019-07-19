package funnyai;

import java.io.Serializable;

public class ChatObject implements Serializable{

    private String from;
    private String to;
    private String message;
    private String user;
    private String msg;

    public ChatObject() {
    }

    /**
     * 
     * @param from
     * @param to
     * @param message
     * @param user from user id
     * @param msg msg_id
     */
    public ChatObject(String from,String to,String message,String user,String msg) {
        super();
        this.from = from;
        this.to = to;
        this.message = message;
        this.user=user;
        this.msg=msg;
    }

    
    public String getUser() {
        return user;
    }
    public void setUser(String from_user_id) {
        this.user = from_user_id;
    }
    
    
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg_id) {
        this.msg= msg_id;
    }
    
    
    
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    
    
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
