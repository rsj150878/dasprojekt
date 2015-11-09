package com.app.Components.Listener;



import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class PrintButtonListener implements ClickListener {
	
	    @Override
    public void buttonClick(ClickEvent event) {
        // Create the PDF source and pass the data model to it
//        StreamSource source =
//            new PDFSource();
//        
//        // Create the stream resource and give it a file name
//        String filename = "pdf_printing_example.pdf";
//        StreamResource resource =
//                new StreamResource(source, filename);
//        
//        // These settings are not usually necessary. MIME type
//        // is detected automatically from the file name, but
//        // setting it explicitly may be necessary if the file
//        // suffix is not ".pdf".
//        resource.setMIMEType("application/pdf");
//        resource.getStream().setParameter("Content-Disposition",
//                "attachment; filename="+filename);
//
//        // Extend the print button with an opener
//        // for the PDF resource
//        BrowserWindowOpener opener =
//                new BrowserWindowOpener(resource);
//        
//        opener.extend(event.getButton());
        
        //opener.extend(target);
        System.out.println("bin im event");
        //event.
     
      
        //name.setEnabled(false);
        //ok.setEnabled(false);
        //print.setEnabled(true);
    }

}
