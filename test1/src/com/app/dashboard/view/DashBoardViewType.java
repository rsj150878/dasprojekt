package com.app.dashboard.view;

import com.app.dashboard.DashboardView;
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
    KURSGRUNDDATEN("KursGrundDaten", KursDetails.class, VaadinIcons.CODE, false, null),
    KURSUEBERSICHT("KursÜbersicht", KursUebersicht.class, VaadinIcons.CLIPBOARD, false, null),
    SCHAUUEBERSICHT("Schauübersicht", ShowUebersicht.class,  VaadinIcons.SHARE, false, null),
    SHOWBEWERTUNG("Schaubewertung", ShowBewertungUebersicht.class, VaadinIcons.FILE_TREE, false, "show"),
    SHOWBEWERTUNGKOKMPLETT("PDF Ergebnisse", ShowBewertungDruckUebersicht.class, VaadinIcons.PAPERCLIP, false, "show"),
    ERGEBNISSE("Punkte erfassen", VeranstaltungPunkteEingabeView.class, VaadinIcons.POINTER, false, null),
    TESTSCAN("testscan", TestBarCodeScan.class, VaadinIcons.POINTER, false, null)
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
