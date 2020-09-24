package com.app.enumdatatypes;

import java.util.ArrayList;
import java.util.List;

public enum ShowEmailSender {

	MAIL_ABSENDER_CSS(1, "show@retrieverclub.at", "?Show66", "mail.mymagenta.business", Boolean.FALSE,
			"Werter Aussteller!" + "<br>" + "Im Anhang erhalten Sie das Bewertungsblatt und die Urkunde Ihres Hundes "
					+ "XXX_HUNDENAME_XXX der Ausstellung XXX_SHOWNAME_XXX" + "<br>" + "<br>"
					+ "Wir danken für Ihren Besuch!<br>" + "<br>" + "mit freundlichen Grüßen<br>" + "<br>"
					+ "Das ÖRC Show Referat"),
	MAIL_ABSENDER_FESTIVAL(2,"info@retrieverfestival.at", "Kathi4711","smtp.world4you.com", Boolean.TRUE,
			"Werter Aussteller!" + "<br>" + "Im Anhang erhalten Sie das Bewertungsblatt und die Urkunde Ihres Hundes "
					+ "XXX_HUNDENAME_XXX der Ausstellung XXX_SHOWNAME_XXX" + "<br>" + "<br>"
					+ "Wir danken für Ihren Besuch!<br>" + "<br>" + "mit freundlichen Grüßen<br>" + "<br>"
					+ "Das Team der Landesgruppe Niederösterreich")
	;

	private String absender;
	private String password;
	private String host;
	private String mailText;
	private Integer senderId;
	private Boolean useSSL;

	private ShowEmailSender(Integer senderId, String absender, String password, String host, Boolean useSSL,
			String mailText) {

		this.absender = absender;
		this.password = password;
		this.host = host;
		this.mailText = mailText;
		this.senderId = senderId;
		this.useSSL = useSSL;

	}

	public String getAbsender() {
		return absender;
	}

	public void setAbsender(String absender) {
		this.absender = absender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getMailText() {
		return mailText;
	}

	public void setMailText(String mailText) {
		this.mailText = mailText;
	}

	public Integer getSenderId() {
		return senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	public Boolean getUseSSL() {
		return useSSL;
	}

	public void setUseSSL(Boolean useSSL) {
		this.useSSL = useSSL;
	}

	public static ShowEmailSender getEmailSenderForID(Integer mailSenderID) {

		for (ShowEmailSender o : ShowEmailSender.values()) {
			if (o.getSenderId().equals(mailSenderID)) {
				return o;
			}

		}

		return null;

	}
}
