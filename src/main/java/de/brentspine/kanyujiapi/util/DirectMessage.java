package de.brentspine.kanyujiapi.util;

import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;

public class DirectMessage {

    private String target;
    private String sender;
    private String message;
    private Timestamp time;

    public DirectMessage(String target, String sender, String message, Timestamp time) {
        this.target = target;
        this.sender = sender;
        this.message = message;
        this.time = time;
    }

    public DirectMessage(String target, String sender, String message) {
        this.target = target;
        this.sender = sender;
        this.message = message;
    }

    public DirectMessage() {
    }







    public String getTarget() {
        return target;
    }

    public DirectMessage setTarget(String target) {
        this.target = target;
        return this;
    }

    public String getSender() {
        return sender;
    }

    public DirectMessage setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public DirectMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public Timestamp getTime() {
        return time;
    }

    public DirectMessage setTime(Timestamp time) {
        this.time = time;
        return this;
    }

}
