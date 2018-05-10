package com.app.DashBoard.View;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import com.florentis.signature.DynamicCapture;
import com.florentis.signature.SigCtl;
import com.florentis.signature.SigObj;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class SignTestView extends Panel implements View {
	
	private BufferedImage signatureImage;
	private TextArea textArea;
	
	VerticalLayout mainVerticalLayout = new VerticalLayout();

	public SignTestView() {
		textArea = new TextArea();
		String property = System.getProperty("java.library.path");
		StringTokenizer parser = new StringTokenizer(property, ";");
		while (parser.hasMoreTokens()) {
		    System.err.println(parser.nextToken());
		    }

		mainVerticalLayout.addComponent(textArea);
		sign();
		
	}
	
	private void sign() {
		SigCtl sigCtl = new SigCtl();
		sigCtl.licence("AgAZAPZTkH0EBVdhY29tClNESyBTYW1wbGUBAoECA2UA");
		DynamicCapture dc = new DynamicCapture();
		
		// Generate hash value to attach to signature
		
		int rc = dc.capture(sigCtl, "who", "why", null, null);
		
        if(rc == 0) {
        	textArea.setValue("signature captured successfully\n");
			String fileName = "sig1.png";
			SigObj sig = sigCtl.signature();
			sig.extraData("AdditionalData", "CaptureImage.java Additional Data");
			int flags = SigObj.outputFilename | SigObj.color32BPP | SigObj.encodeData;
			sig.renderBitmap(fileName, 200, 150, "image/png", 0.5f, 0xff0000, 0xffffff, 0.0f, 0.0f, flags );
			paintSignature(fileName);
		}
		else {
			textArea.setValue("signature capture error res="+rc+"\n");
			switch(rc) {
				case 1:   textArea.setValue("Cancelled\n");
					break;
				case 100: textArea.setValue("Signature tablet not found\n");
					break;
				case 103: textArea.setValue("Capture not licensed\n");
					break;
				default:  textArea.setValue("Unexpected error code\n");
			}
		}
  }
	
	
	private void paintSignature(String fileName) {
		try
    {
	    signatureImage = ImageIO.read(new File(fileName));
	    FileResource resource = new FileResource(new File(fileName));

	    //Show the image in the application
	    Image image = new Image("Image from file", resource);
	    mainVerticalLayout.addComponent(image);

	    
	    //drawPanel.repaint();
	    //setContent(image);
	    
    } catch (IOException e)
    {
    	System.out.println(e.toString());
    }
	}
	  

}
