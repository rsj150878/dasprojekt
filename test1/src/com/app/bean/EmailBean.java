package com.app.bean;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class EmailBean implements Serializable {

    private String to ="";
    private String bcc ="";
    private String cc ="";
    private String body ="";
    private String subject ="";
    private String attachment ="";
    private String from ="";
    private List<File> files;

    public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public String getFrom() {
		return from;   
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

