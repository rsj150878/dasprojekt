package com.app.dashboard.view;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

public class TestBarCodeScan extends VerticalLayout implements View {
	
	Button scanButton;
	
	public TestBarCodeScan() {
		scanButton = new Button("scan");
		scanButton.addClickListener(event -> {
			try {
				byte []result =  passwordHashStage1("1");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		addComponent(scanButton);
		
	}
	
	 byte[] passwordHashStage1(String password) throws NoSuchAlgorithmException {
		  MessageDigest md=MessageDigest.getInstance("SHA-1");
		  StringBuffer cleansedPassword=new StringBuffer();
		  int passwordLength=password.length();
		  for (int i=0; i < passwordLength; i++) {
		    char c=password.charAt(i);
		    if ((c == ' ') || (c == '\t')) {
		      continue;
		    }
		    cleansedPassword.append(c);
		  }
		  return md.digest(cleansedPassword.toString().getBytes());
		}
		 
	
}
