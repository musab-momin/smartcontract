package com.smartcontract.smartcontract.helper;

public class Message 
{
    private String type;
    private String subject;

    public Message(String type, String subject) {
        this.type = type;
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    
    
}
