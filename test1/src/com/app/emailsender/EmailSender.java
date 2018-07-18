package com.app.emailsender;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import com.app.bean.EmailBean;

public class EmailSender {
	public static boolean sendMail(String from, String password,
			String message, String to[]) {

		SimpleEmail xxx = new SimpleEmail();

		xxx.setHostName("retrieverdata.at");
		xxx.setAuthentication("no-reply@retrieverdata.at", "waterloo123");
		xxx.setSmtpPort(25);

		try {
			// Create a default MimeMessage object.
			// Set From: header field of the header.
			xxx.setFrom(from);

			// Set To: header field of the header.

			List<InternetAddress> toList = new ArrayList<InternetAddress>();
			for (String emailId : Arrays.asList(to)) {
				toList.add(new InternetAddress(emailId));
			}

			xxx.setTo(toList);

			// Set Subject: header field
			xxx.setSubject("This is the Subject Line!");

			// Now set the actual message
			xxx.setMsg(message);

			// Send message
			xxx.send();

			System.out.println("Sent message successfully....");
			return true;

		} catch (EmailException mex) {
			mex.printStackTrace();
		} catch (AddressException aex) {
			aex.printStackTrace();
		}

		return false;
	}

	public static boolean sendHTMLMail(EmailBean bean) {

		HtmlEmail email = new HtmlEmail();

		email.setHostName("retrieverdata.at");
		email.setAuthentication("no-reply@retrieverdata.at", "waterloo123");
		email.setSmtpPort(25);

		try {
			if (bean.getTo() != null && !"".equals(bean.getTo())) {
				email.setTo(getInternetEmailIds(getEmailIds(bean.getTo())));
			}
			if (bean.getCc() != null && !"".equals(bean.getCc())) {
				email.setCc(getInternetEmailIds(getEmailIds(bean.getCc())));
			}
			if (bean.getBcc() != null && !"".equals(bean.getBcc())) {
				email.setBcc(getInternetEmailIds(getEmailIds(bean.getBcc())));
			}
			//email.setFrom(bean.getFrom());
			email.setFrom("no-reply@retrieverdata.at");
			/*
			 * setting bounce back email id if
			 * (!"".equals(bean.getFromEmailId()) && !"".equals(getFromName()))
			 * { email.setFrom(getFromEmailId(), getFromName()); } else if
			 * (!"".equals(getFromEmailId())) { email.setFrom(getFromEmailId(),
			 * getFromEmailId()); } else { email.setFrom(getUsername(),
			 * getUsername()); }
			 */
			// if (getBounceAddress() != null) {
			// email.setBounceAddress(getBounceAddress());
			// }
			email.setSubject(bean.getSubject());

			if (bean.getBody() != null && !"".equals(bean.getBody())) {
				email.setHtmlMsg(bean.getBody());
			}
		
			email.send();
			
			return true;

		} catch (EmailException ex) {
			ex.printStackTrace();
		} catch (AddressException ex) {
			ex.printStackTrace();
		}

		
		/*
		 * if (!files.isEmpty()) { for (File file : files) { EmailAttachment
		 * attachment = new EmailAttachment();
		 * attachment.setDisposition(EmailAttachment.ATTACHMENT);
		 * attachment.setName(file.getName());
		 * attachment.setPath(file.getAbsolutePath()); email.attach(attachment);
		 * } } email.send();
		 * 
		 * if (!files.isEmpty()) { for (File file : files) { file.delete(); } }
		 * files.clear(); updateAttachementDetails();
		 */
		return false;
	}

	private static List<String> getEmailIds(String data) {

		if ("".equals(data.trim())) {
			return new ArrayList<String>();
		}

		String SEPERATOR = "_";
		data = data.replaceAll(",", SEPERATOR);
		data = data.replaceAll(";", SEPERATOR);

		String[] emailIds = data.split(SEPERATOR);
		return Arrays.asList(emailIds);

	}

	private static List<InternetAddress> getInternetEmailIds(
			List<String> emailIds) throws AddressException {
		List<InternetAddress> addresses = new ArrayList<InternetAddress>();
		for (String emailId : emailIds) {
			addresses.add(new InternetAddress(emailId));
		}
		return addresses;
	}

}
