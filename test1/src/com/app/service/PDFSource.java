package com.app.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Date;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.context.ApplicationContext;

import com.app.dbIO.PathHandler;
import com.example.testUIWithLogin.testUIWithLogin;
import com.vaadin.server.StreamResource.StreamSource;

public class PDFSource implements StreamSource {

	String name; // A trivial content data model

	/** Constructor gets a content data model as parameter */
	public PDFSource(String name) {
		this.name = name;
	}

	public PDFSource() {

	}

	@Override
	public InputStream getStream() {
		// Generate the FO content. You could use the Java DOM API
		// here as well and pass the DOM to the transformer.
		String fo = "<?xml version='1.0' encoding='ISO-8859-1'?>\n"
				+ "<fo:root xmlns:fo='http://www.w3.org/1999/XSL/Format'>\n"
				+ "<fo:layout-master-set>"
				+ "  <fo:simple-page-master master-name='A4' margin='2cm'>"
				+ "    <fo:region-body />" + "  </fo:simple-page-master>"
				+ "</fo:layout-master-set>"
				+ "<fo:page-sequence master-reference='A4'>"
				+ "    <fo:flow flow-name='xsl-region-body'>"
				+ "    <fo:block text-align='center'>" + "Hello There, " + name
				+ "!</fo:block>" + "  </fo:flow>" + "</fo:page-sequence>"
				+ "</fo:root>\n";

		Class cls = this.getClass();
		ProtectionDomain pDomain = cls.getProtectionDomain();
		CodeSource cSource = pDomain.getCodeSource();

		URL loc = cSource.getLocation();
		System.out.println(loc);

		String basePath = new File("").getAbsolutePath();
		System.out.println(basePath);

		//SecurityContext ctx = getApplication().getContext();

	//	ApplicationContext ctx =
		//WebApplicationContext webCtx = (WebApplicationContext) ctx;
		// ServletContext sc = webCtx.getHttpSession().getServletContext();

		//ServletContext sc = webCtx.getServletContext();
//		try {
//			sc.getResource("kursblat.xml");
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}

		File file = new File(PathHandler.INSTANCE.getPathName() + "/files/kursblat.xml");
		//ByteArrayInputStream foStream = new ByteArrayInputStream(file);

		ByteArrayInputStream foStream = null; // = new
												// ByteArrayInputStream(fo.getBytes());

		FileInputStream xx;
		try {
			xx = new FileInputStream(file);
			byte[] xxy = new byte[65535];
			xx.read(xxy);
			System.out.println("länge" + xxy.length);
			//foStream = new ByteArrayInputStream(fo.getBytes());
			foStream = new ByteArrayInputStream(xxy);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Basic FOP configuration. You could create this object
		// just once and keep it.
		FopFactory fopFactory = FopFactory.newInstance();

		fopFactory.setStrictValidation(false); // For an example

		// Configuration for this PDF document - mainly metadata
		FOUserAgent userAgent = fopFactory.newFOUserAgent();
		userAgent.setProducer("My Vaadin Application");
		userAgent.setCreator("Me, Myself and I");
		userAgent.setAuthor("Da Author");
		userAgent.setCreationDate(new Date());
		userAgent.setTitle("Hello to " + name);
		userAgent.setKeywords("PDF Vaadin example");
		userAgent.setTargetResolution(300); // DPI

		// Transform to PDF
		ByteArrayOutputStream fopOut = new ByteArrayOutputStream();
		try {
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent,
					fopOut);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			Source src = new javax.xml.transform.stream.StreamSource(foStream);
			Result res = new SAXResult(fop.getDefaultHandler());
			transformer.transform(src, res);
			fopOut.close();
			return new ByteArrayInputStream(fopOut.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}