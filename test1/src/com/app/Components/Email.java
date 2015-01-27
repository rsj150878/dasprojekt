package com.app.Components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.app.EmailSender.EmailSender;
import com.app.bean.EmailBean;
import com.app.dbIO.DBConnection;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;

/**
 *
 * @author patnaikv
 */
public class Email extends Form implements ClickListener, QueryDelegate.RowIdChangeListener {

	/* Email Details */
	private String emailHost;
	private int emailHostPort = 25;
	private String username;
	private String password;
	private String fromEmailId = "";
	private String fromName = "";
	private String bounceAddress;
	/* User messages */
	private String invalidEmaildErrrorMessage = "Enter a Valid Email Id";
	private String emptySubjectErrrorMessage = "Enter a Subject";
	private String attachmentText = "Attachement(s) : ";
	private String removeFilesWindowCaption = "Remove File(s)";
	private String removeAllFilesWindowCaption = "Remove All Files";
	private String selectFilesToBeRemovedCaption = "Select Files To Remove";
	private String successMessage = "Message Sent Successfully";
	private String failureMessage = "Message Sending failed";
	/* Components */

	final private String DISPLAY_CAPTION_PROPERTY = "name";
	private final GridLayout rootLayout = new GridLayout(5, 22);
	private final String[] visibleColumns = new String[] { "to", "cc", "bcc",
			"subject", "attachment", "body" };
	private final Label to = new Label("To");
	private final Label cc = new Label("Cc");
	private final Label bcc = new Label("Bcc");
	private final Label subject = new Label("Subject");
	private TextField subjectTxtField;
	private final Button sendButton = new Button("Send");
	private final Button discardButton = new Button("Discard");
	private final Button removeFilesButton = new Button(
			removeFilesWindowCaption);
	private final Button removeFilesButtonInPopUp = new Button(
			removeFilesWindowCaption);
	private final Button removeAllFilesButtonInPopUp = new Button(
			removeAllFilesWindowCaption);
	private final RichTextArea editor = new RichTextArea();
	private final FileUpload upload = new FileUpload("", new FileReceiver());
	private final Label attachementDetails = new Label(attachmentText);
	private final ProgressBar indicator = new ProgressBar();
	private final Label status = new Label("", Label.CONTENT_RAW);
	private final ListSelect filesListSelect = new ListSelect();
	private final Window popUpWindowForFileRemoval = new Window();
	private List<File> files;

	RowId Emailid = null;

	public Email() {

		rootLayout.setSpacing(true);
		setLayout(rootLayout);

		removeFilesButton.setVisible(false);
		removeFilesButton.addClickListener(this);
		removeAllFilesButtonInPopUp.addClickListener(this);
		sendButton.addClickListener(this);
		removeFilesButtonInPopUp.addClickListener(this);
		discardButton.addClickListener(this);
		UI.getCurrent().setPollInterval(500);

		init();
	}

	private void init() {

		/* Form attributes */
		rootLayout.removeAllComponents();
		removeAllProperties();
		setInvalidCommitted(false);
		setFormFieldFactory(new EmailFormFieldFactory());
		BeanItem<EmailBean> itemDataSource = new BeanItem<EmailBean>(
				new EmailBean());
		setItemDataSource(itemDataSource, Arrays.asList(visibleColumns));

		/* Layout attributes */

		upload.setButtonCaption("Upload File");
		rootLayout.addComponent(upload, 0, 16);
		rootLayout.setComponentAlignment(upload, Alignment.MIDDLE_LEFT);

		rootLayout.addComponent(removeFilesButton, 1, 16);
		rootLayout.setComponentAlignment(removeFilesButton,
				Alignment.MIDDLE_LEFT);

		rootLayout.addComponent(discardButton, 3, 16);
		rootLayout.setComponentAlignment(discardButton, Alignment.MIDDLE_LEFT);

		rootLayout.addComponent(sendButton, 4, 16);
		rootLayout.setComponentAlignment(sendButton, Alignment.MIDDLE_LEFT);

		attachementDetails.setValue(getAttachmentText());
		rootLayout.addComponent(attachementDetails, 0, 19, 4, 21);

		files = new ArrayList<File>();
	}

	public void addField(Object propertyId, Field field) {
		if ("to".equals(propertyId)) {
			addComponent_(to, 0, 0);
			addComponent_(field, 1, 0, 4, 0);
		} else if ("cc".equals(propertyId)) {
			addComponent_(cc, 0, 1);
			addComponent_(field, 1, 1, 4, 1);
		} else if ("bcc".equals(propertyId)) {
			addComponent_(bcc, 0, 2);
			addComponent_(field, 1, 2, 4, 2);
		} else if ("subject".equals(propertyId)) {
			addComponent_(subject, 0, 3);
			addComponent_(field, 1, 3, 4, 3);
		} else if ("body".equals(propertyId)) {
			addComponent_(field, 0, 4, 4, 15);
		}
	}

	public void addComponent_(Component field, int column, int row) {
		rootLayout.addComponent(field, column, row);
	}

	public void addComponent_(Component field, int fromColumn, int fromRow,
			int toColumn, int toRow) {
		rootLayout.addComponent(field, fromColumn, fromRow, toColumn, toRow);
	}

	class FileUpload extends Upload implements Upload.SucceededListener,
			Upload.FailedListener, Upload.StartedListener,
			Upload.FinishedListener, Upload.ProgressListener {

		public FileUpload(String caption, Receiver uploadReceiver) {
			super(caption, uploadReceiver);
			setImmediate(true);
			setButtonCaption(caption);

			addSucceededListener(this);
			addFailedListener(this);
			addStartedListener(this);
			addFinishedListener(this);
			addProgressListener(this);

		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			removeComponent_(indicator);
			addComponent_(status, 1, 17, 3, 17);
		}

		@Override
		public void uploadFailed(FailedEvent event) {
			removeComponent_(indicator);
			addComponent_(status, 1, 17, 3, 17);
		}

		@Override
		public void uploadStarted(StartedEvent event) {
			removeComponent_(status);
			addComponent_(indicator, 1, 17, 3, 17);
			rootLayout.setComponentAlignment(indicator, Alignment.MIDDLE_LEFT);
		}

		@Override
		public void uploadFinished(FinishedEvent event) {
			FileReceiver fileReceiver = (FileReceiver) getReceiver();
			files.add(fileReceiver.getFile());

			updateAttachementDetails();
			removeFilesButton.setVisible(true);
			removeComponent_(indicator);
			removeComponent_(status);
		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {
			indicator.setValue(new Float(readBytes / (float) contentLength));
		}
	}

	private void updateAttachementDetails() {

		String SEPERATOR = ",";
		String value = "";
		for (File file : files) {
			value = value + SEPERATOR + file.getName();
		}
		attachementDetails.setValue(getAttachmentText()
				+ ((value.startsWith(SEPERATOR)) ? value.substring(1) : value));
	}

	class FileReceiver implements Upload.Receiver {

		private File file;
		private String fileName;

		@Override
		public OutputStream receiveUpload(String fileName, String MIMEType) {
			this.fileName = fileName;
			FileOutputStream fos = null;
			file = new File(fileName);
			if (!file.exists()) {
				try {
					file.createNewFile();
					fos = new FileOutputStream(file);
				} catch (Exception ex) {
					Logger.getLogger(FileReceiver.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
			return fos;

		}

		public File getFile() {
			return file;
		}

		public String getFileName() {
			return fileName;
		}
	}

	public void buttonClick(ClickEvent event) {

		if (event.getSource() == sendButton) {

			this.commit();
			editor.commit();

			EmailBean bean = ((BeanItem<EmailBean>) this.getItemDataSource())
					.getBean();
			if ("".equals(bean.getTo().trim())
					&& "".equals(bean.getBcc().trim())
					&& "".equals(bean.getCc().trim())) {
				this.setComponentError(new UserError(
						getInvalidEmaildErrrorMessage(),
						UserError.CONTENT_TEXT, UserError.ERROR));
				return;
			} else {
				this.setComponentError(null); // for resetting the error
												// message.
			}
			if (validations(bean)) {
				try {
					EmailSender.sendHTMLMail(bean);
					saveEmail(bean);
					status.setValue("Message Sent Successfully");
					Notification.show(getSuccessMessage());
				} catch (Exception ex) {
					Logger.getLogger(Email.class.getName()).log(Level.SEVERE,
							null, ex);
					status.setValue("Message sending failed");
				}

				removeComponent_(status);
				addComponent_(status, 1, 17, 3, 17);
				init();// reinitialising the form.
			}
		} else if (event.getSource() == removeFilesButton) {
			Window window = getPopUpWindowToRemoveFiles();
			UI.getCurrent().addWindow(window);
		} else if (event.getSource() == removeFilesButtonInPopUp) {
			Set collections = (Set) filesListSelect.getValue();
			Iterator iterator = collections.iterator();
			while (iterator.hasNext()) {
				int index = ((Integer) iterator.next()).intValue();
				File file = files.get(index);
				file.delete();
				files.remove(index);
			}

			if (files.size() == 0) {
				removeFilesButton.setVisible(false);
			}

			UI.getCurrent().removeWindow(popUpWindowForFileRemoval);
			updateAttachementDetails();
		} else if (event.getSource() == removeAllFilesButtonInPopUp) {

			deleteAllFiles();
			removeFilesButton.setVisible(false);
			files.clear();
			UI.getCurrent().removeWindow(popUpWindowForFileRemoval);
			updateAttachementDetails();
		} else if (event.getSource() == discardButton) {
			deleteAllFiles();
			init();
		}

	}

	private void saveEmail(EmailBean EmailObject) {

		TableQuery aq = new TableQuery("email",
				DBConnection.INSTANCE.getConnectionPool());
		aq.setVersionColumn("version");

		TableQuery attTQ = new TableQuery("emailattachment",
				DBConnection.INSTANCE.getConnectionPool());
		attTQ.setVersionColumn("version");

		try {
			SQLContainer sqlc = new SQLContainer(aq);

			SQLContainer attc = new SQLContainer(attTQ);

			Object newObject = sqlc.addItem();
			Item newItem = sqlc.getItem(newObject);

			newItem.getItemProperty("an").setValue(EmailObject.getTo());
			newItem.getItemProperty("bc").setValue(EmailObject.getCc());
			newItem.getItemProperty("bcc").setValue(EmailObject.getBcc());
			newItem.getItemProperty("betreff").setValue(
					EmailObject.getSubject());
			newItem.getItemProperty("inhalt").setValue(EmailObject.getBody());

			newItem.getItemProperty("attachement").setValue(" ");
			sqlc.addRowIdChangeListener(this);
			sqlc.commit();

			String nameSeperator = "";
			String filenames = "";
			try {
				for (File file : files) {
					filenames = filenames + nameSeperator + file.getName();
					nameSeperator = ";";

					String byteFile = FileUtils.readFileToString(file);

					Item newAttItem = attc.getItem(attc.addItem());

					newAttItem.getItemProperty("emailid").setValue(
							new Integer(Emailid.getId()[0].toString()));
					newAttItem.getItemProperty("filename").setValue(
							file.getName());
					newAttItem.getItemProperty("datei").setValue(byteFile);

				}
			} catch (IOException e) {

			}

			// newItem.getItemProperty("attachement").setValue(filenames);

			sqlc.addRowIdChangeListener(new QueryDelegate.RowIdChangeListener() {

				@Override
				public void rowIdChange(RowIdChangeEvent event) {
					// TODO Auto-generated method stub
					Emailid = event.getNewRowId();

				}

			});

		} catch (SQLException ex) {

		}

	}

	private void deleteAllFiles() {
		for (File file : files) {
			file.delete();
		}
	}

	private Window getPopUpWindowToRemoveFiles() {
		popUpWindowForFileRemoval.setCaption(getRemoveFilesWindowCaption());
		popUpWindowForFileRemoval.setDraggable(true);
		GridLayout layout = new GridLayout(2, 8);
		layout.setMargin(true);
		layout.setSpacing(true);

		ListSelect select = initialiseListSelect();
		layout.addComponent(select, 0, 0, 0, 7);
		layout.setComponentAlignment(select, Alignment.MIDDLE_CENTER);

		layout.addComponent(removeFilesButtonInPopUp, 1, 2);
		layout.addComponent(removeAllFilesButtonInPopUp, 1, 4);

		popUpWindowForFileRemoval.setContent(layout);
		return popUpWindowForFileRemoval;
	}

	private ListSelect initialiseListSelect() {
		filesListSelect.removeAllItems();
		filesListSelect.setRows(10);
		filesListSelect.setNullSelectionAllowed(true);
		filesListSelect.setMultiSelect(true);
		filesListSelect.setImmediate(true);
		filesListSelect.setItemCaptionPropertyId(DISPLAY_CAPTION_PROPERTY);
		filesListSelect
				.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);

		filesListSelect.setContainerDataSource(getFilesListContainer());
		return filesListSelect;
	}

	private Container getFilesListContainer() {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(DISPLAY_CAPTION_PROPERTY, String.class,
				null);
		for (int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			Item systemItem = container.addItem(i);
			systemItem.getItemProperty(DISPLAY_CAPTION_PROPERTY).setValue(
					file.getName());
		}
		return container;
	}

	private void removeComponent_(Component component) {
		rootLayout.removeComponent(component);
	}

	private boolean validations(EmailBean bean) {
		if (validateEmail(bean.getTo()) && validateEmail(bean.getCc())
				&& validateEmail(bean.getBcc())
				&& validateSubject(bean.getSubject())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * For validating the email entries.
	 *
	 * @param data
	 * @return
	 */
	private boolean validateEmail(String data) {
		for (String emailId : getEmailIds(data)) {
			if (!(emailId.contains("@") && emailId.contains(".") && !emailId
					.endsWith("."))) {// failure condition
				this.setComponentError(new UserError(
						getInvalidEmaildErrrorMessage(),
						UserError.CONTENT_TEXT, UserError.ERROR));
				return false;
			}
		}
		return true;
	}

	/**
	 * For validating the subject.
	 *
	 * @param data
	 * @return
	 */
	private boolean validateSubject(String data) {
		if (!(data != null && !"".equals(data))) {
			this.subjectTxtField.focus();
			this.setComponentError(new UserError(
					getEmptySubjectErrrorMessage(), UserError.CONTENT_TEXT,
					UserError.ERROR));
			return false;
		}

		return true;
	}

	/**
	 * For getting email Entries.
	 *
	 * @param data
	 * @return
	 */
	private List<String> getEmailIds(String data) {

		if ("".equals(data.trim())) {
			return new ArrayList<String>();
		}

		String SEPERATOR = "_";
		data = data.replaceAll(",", SEPERATOR);
		data = data.replaceAll(";", SEPERATOR);

		String[] emailIds = data.split(SEPERATOR);
		return Arrays.asList(emailIds);

	}
	
	@Override
	public void rowIdChange(RowIdChangeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("event " + event.getNewRowId());
		Emailid = event.getNewRowId();

	}

	public String getInvalidEmaildErrrorMessage() {
		return invalidEmaildErrrorMessage;
	}

	public void setInvalidEmaildErrrorMessage(String invalidEmaildErrrorMessage) {
		this.invalidEmaildErrrorMessage = invalidEmaildErrrorMessage;
	}

	public String getEmptySubjectErrrorMessage() {
		return emptySubjectErrrorMessage;
	}

	public void setEmptySubjectErrrorMessage(String emptySubjectErrrorMessage) {
		this.emptySubjectErrrorMessage = emptySubjectErrrorMessage;
	}

	public String getEmailHost() {
		return emailHost;
	}

	public void setEmailHost(String emailHost) {
		this.emailHost = emailHost;
	}

	public String getFromEmailId() {
		return fromEmailId;
	}

	public void setFromEmailId(String fromEmailId) {
		this.fromEmailId = fromEmailId;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getEmailHostPort() {
		return emailHostPort;
	}

	public void setEmailHostPort(int emailHostPort) {
		this.emailHostPort = emailHostPort;
	}

	public String getBounceAddress() {
		return bounceAddress;
	}

	public void setBounceAddress(String bounceAddress) {
		this.bounceAddress = bounceAddress;
	}

	public void setCaptionTo(String to) {
		this.to.setValue(to);
	}

	public void setCaptionCc(String cc) {
		this.cc.setValue(cc);
	}

	public void setCaptionBcc(String bcc) {
		this.bcc.setValue(bcc);
	}

	public void setCaptionSubject(String subject) {
		this.subject.setValue(subject);
	}

	public void setCaptionSendButton(String captionSendButton) {
		this.sendButton.setCaption(captionSendButton);
	}

	public void setCaptionDiscardButton(String captionDiscardButton) {
		this.discardButton.setCaption(captionDiscardButton);
	}

	public void setCaptionUploadButton(String captionUploadButton) {
		this.upload.setButtonCaption(captionUploadButton);
	}

	public void setCaptionRemoveFilesButton(String captionRemoveButton) {
		this.removeFilesButton.setCaption(captionRemoveButton);
		this.removeFilesButtonInPopUp.setCaption(captionRemoveButton);
	}

	public void setCaptionRemoveAllFilesButton(String captionRemoveButton) {
		this.removeAllFilesButtonInPopUp.setCaption(captionRemoveButton);
	}

	public String getAttachmentText() {
		return attachmentText;
	}

	public void setAttachmentText(String attachmentText) {
		this.attachmentText = attachmentText;
	}

	public String getRemoveFilesWindowCaption() {
		return removeFilesWindowCaption;
	}

	public void setRemoveFilesWindowCaption(String removeFilesWindowCaption) {
		this.removeFilesWindowCaption = removeFilesWindowCaption;
	}

	public String getSelectFilesToBeRemovedCaption() {
		return selectFilesToBeRemovedCaption;
	}

	public void setSelectFilesToBeRemovedCaption(
			String selectFilesToBeRemovedCaption) {
		this.selectFilesToBeRemovedCaption = selectFilesToBeRemovedCaption;
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
}
