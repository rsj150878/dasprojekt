package com.app.bean;

import java.io.Serializable;

/**
 * @author Ondrej Kvasnovsky
 */
public class MyBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private String authenticated;
    private String notAuthenticated;

    public MyBean(String authenticated, String notAuthenticated) {
        this.authenticated = authenticated;
        this.notAuthenticated = notAuthenticated;
    }

    public String getAuthenticated() {
        return authenticated;
    }

    public String getNotAuthenticated() {
        return notAuthenticated;
    }
}
