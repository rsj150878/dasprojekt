package com.app.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import com.app.dbIO.PathHandler;
import com.app.printClasses.Hund;
import com.app.printClasses.Kursart;
import com.app.printClasses.Kursteilnehmer;
import com.app.printClasses.Person;
import com.java4less.xreport.fop.FOProcessor;
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

		Hund hund = new Hund();
		Person person = new Person();
		Kursart kursart = new Kursart();
		person.setName("test");
		kursart.setWelpen1("x");
		kursart.setWelpen2(" ");

		Kursteilnehmer kursteilnehmer = new Kursteilnehmer();
		kursteilnehmer.setHund(hund);
		kursteilnehmer.setHundeBesitzer(person);
		kursteilnehmer.setKursTeilnehmer(person);
		kursteilnehmer.setKursart(kursart);

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
			Transformer transformer = factory
					.newTransformer(new javax.xml.transform.stream.StreamSource(
							new File(PathHandler.INSTANCE.getPathName(),
									"/files/kursblatt.fo")));
			JAXBContext jc = JAXBContext.newInstance(Kursteilnehmer.class);

		
			//System.out.println(jc);
			Marshaller marshaller = jc.createMarshaller();
			System.out.println(marshaller);
			 ByteArrayOutputStream ba = new ByteArrayOutputStream();
			 marshaller.marshal(kursteilnehmer, ba);
			//ByteArrayInOutStream bos = new ByteArrayInOutStream();
			//System.out.println("xml of data" + new String(bos.toByteArray()));
			//marshaller.marshal(kursteilnehmer, bos);
			
			System.out.println("xml of data" + new String(ba.toByteArray()));
			//Source src = new javax.xml.transform.stream.StreamSource(
			//		bos.getInputStream());
			//Result res = new SAXResult(fop.getDefaultHandler());
			//transformer.transform(src, res);
			fopOut.close();
			
			FOProcessor processor=new FOProcessor();
			processor.process(new ByteArrayInputStream(ba.toByteArray()), new FileInputStream(PathHandler.INSTANCE.getPathName()+
					"/files/kursblatt.fo"), fopOut);
		
			fopOut.close();
			//ByteArrayInOutStream boa1 = new ByteArrayInOutStream(fopOut);
			return new ByteArrayInputStream(fopOut.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
