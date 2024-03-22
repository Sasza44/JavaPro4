package ua.meta.atipikin;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Message {
    private Date date = new Date();
    private String from;
    private String to;
    private String text;

    public Message(String from, String text) {
        this.from = from;
        this.text = text;
    }
    public String toJSON () {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();
        return gson.toJson(this);
    }
    public static Message fromJSON (String s) {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();
        return gson.fromJson(s, Message.class);
    }
    @Override
    public String toString() {
        return new StringBuilder().append("[").append(date)
                .append(", From: ").append(from).append(", To: ").append(to)
                .append("] ").append(text).toString();
    }

    public Date getDate() {return date;}
    public String getFrom() {return from;}
    public String getTo() {return to;}
    public String getText() {return text;}
    public void setDate(Date date) {this.date = date;}
    public void setFrom(String from) {this.from = from;}
    public void setTo(String to) {this.to = to;}
    public void setText(String text) {this.text = text;}
}