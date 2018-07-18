package com.app.DashBoard.View;

import com.app.DashBoard.DashboardView;
import com.app.components.HundeImport;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;

public enum DashBoardViewType {
    DASHBOARD("Dashboard", DashboardView.class, VaadinIcons.HOME, true, null),
    MYDOGS("Meine Hunde", MyHundeView.class, VaadinIcons.BAR_CHART, false, null),  
    MITGLIEDER("Mitgliederliste", MitgliederView.class, VaadinIcons.TABLE, false, null),
    EMAILZUSATZ("Emailadresse alt", EmailAdressNewsletterView.class, VaadinIcons.MAILBOX, false, null),
    VERANSTALTUNG("Veranstaltungen", VeranstaltungsUebersicht.class, VaadinIcons.PARAGRAPH, false, "work"),
    MAILABGLEICH("Mailadressenabgleich", MailReplicateView.class, VaadinIcons.FORWARD, false, null),
    SCHOWIMPORT("SchowDatenImport", ShowImporter.class, VaadinIcons.EXCLAMATION, false, null),
    KURSGRUNDDATEN("KursGrundDaten", KursDetails.class, VaadinIcons.CODE, false, null),
    KURSUEBERSICHT("KursÜbersicht", KursUebersicht.class, VaadinIcons.CLIPBOARD, false, null),
    CUPBASISBERECHUNG("ShowCup Basis", CupBasisBerechnungsView.class, VaadinIcons.DATABASE, false, null),
    HUNDEIMPORT("Hunde importieren", HundeImport.class, VaadinIcons.TICKET, false, null),
    SCHAUUEBERSICHT("Schauübersicht", ShowUebersicht.class,  VaadinIcons.SHARE, false, null),
    WUERFE("Würfe", WurfUebersicht.class, VaadinIcons.MICROPHONE, false, null),
    SHOWBEWERTUNG("Schaubewertung", ShowBewertungUebersicht.class, VaadinIcons.FILE_TREE, false, "show"),
    SHOWBEWERTUNGKOKMPLETT("PDF Ergebnisse", ShowBewertungDruckUebersicht.class, VaadinIcons.PAPERCLIP, false, "show"),
    //BEZAHLTUNGTEST("TestPaymen", TestPayment.class, VaadinIcons.MONEY, false, null);
    SIGNTEST("TestUnterschrift", SignTestView.class, VaadinIcons.SIGN_IN, false, null)
    ;
 
    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;
    private final String rolle;

    private DashBoardViewType(final String viewName,
            final Class<? extends View> viewClass, final Resource icon,
            final boolean stateful, final String rollen) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
        this.rolle = rollen;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }
    
    public String  getRollen() {
    	return rolle;
    }

    public static DashBoardViewType getByViewName(final String viewName) {
        DashBoardViewType result = null;
        for (DashBoardViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
