package OERC.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;

import OERC.Domain.Hund;
import OERC.Domain.Person;
import OERC.enums.RetrieverRassen;

public class RoentgenBegleitScheinPrint extends CustomComponent {
	
	/** The original PDF file. */
	public static final String DATASHEET = "files/ROENTGENBEGLEITSCHEIN.pdf";
	
	public static final String RESULT = "rbs/Roentgenbegleitschein.pdf";

	private AbsoluteLayout mainLayout;

	public RoentgenBegleitScheinPrint(Person person, Hund hund, RetrieverRassen rasse) {

		try {

			File resultFile = new File(RESULT);
			resultFile.getParentFile().mkdirs();
		       
			mainLayout = new AbsoluteLayout();
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			setCompositionRoot(mainLayout);
			
			
			PdfDocument pdfDoc = new PdfDocument(new PdfReader(DATASHEET), new PdfWriter(RESULT));
			
			PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
			
			StringBuilder nameBuilder = new StringBuilder();
			nameBuilder.append(person.getTitel() == null || person.getTitel().isEmpty() ? "" : person.getTitel() + " ");
			nameBuilder.append(person.getNachname() + " ");
			nameBuilder.append(person.getVorname());
			
			StringBuilder adressBuilder = new StringBuilder();
			adressBuilder.append(person.getPlz() +  " ");
			adressBuilder.append(person.getOrt() + ", ");
			adressBuilder.append(person.getStrasse());
			
			form.getField("name_besitzer").setValue(nameBuilder.toString());
			form.getField("hundename").setValue(hund.getName());
			form.getField("rasse").setValue(rasse.getRassenLangBezeichnung());
			form.getField("adresse").setValue(adressBuilder.toString());
			form.getField("wurftag").setValue(new SimpleDateFormat("dd.MM.yyyy").format(hund.getWurfTag()));
			form.getField("telefon").setValue(person.getTelefon1() == null || person.getTelefon1().isEmpty()
							? person.getTelefon2() : person.getTelefon1());
			form.getField("oehzb_nr").setValue(hund.getZbnr());
			form.getField("Geschlecht").setValue(hund.getIdGeschlecht().equals(1) ? "Rüde":"Hündin");
			form.getField("chipnr").setValue(hund.getChipNummer());
			
			form.flattenFields();
			
			pdfDoc.close();

			TemporaryFileDownloadResource s = null;
			try {
				s = new TemporaryFileDownloadResource(RESULT, "application/pdf", resultFile);
			} catch (final FileNotFoundException e) {

			}

			BrowserFrame e = new BrowserFrame("Röntgenbegleitschein", s);
			mainLayout.addComponent(e);

			// e.addDetachListener(mainLayout.getp);

		} catch (

		Exception ee) {
			ee.printStackTrace();
		}
	}

}
