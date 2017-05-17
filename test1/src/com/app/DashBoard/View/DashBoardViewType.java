package com.app.DashBoard.View;

import com.app.Components.HundeImport;
import com.app.DashBoard.DashboardView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;

public enum DashBoardViewType {
    DASHBOARD("Dashboard", DashboardView.class, VaadinIcons.HOME, true),
    MYDOGS("Meine Hunde", MyHundeView.class, VaadinIcons.BAR_CHART, false),  
    MITGLIEDER("Mitgliederliste", MitgliederView.class, VaadinIcons.TABLE, false),
    EMAILZUSATZ("Emailadresse alt", EmailAdressNewsletterView.class, VaadinIcons.MAILBOX, false),
    VERANSTALTUNG("Veranstaltungen", VeranstaltungsUebersicht.class, VaadinIcons.PARAGRAPH, false),
    MAILABGLEICH("Mailadressenabgleich", MailReplicateView.class, VaadinIcons.FORWARD, false),
    SCHOWIMPORT("SchowDatenImport", ShowImporter.class, VaadinIcons.EXCLAMATION, false),
    KURSGRUNDDATEN("KursGrundDaten", KursDetails.class, VaadinIcons.CODE, false),
    KURSUEBERSICHT("KursÜbersicht", KursUebersicht.class, VaadinIcons.CLIPBOARD, false),
    CUPBASISBERECHUNG("ShowCup Basis", CupBasisBerechnungsView.class, VaadinIcons.DATABASE, false),
    HUNDEIMPORT("Hunde importieren", HundeImport.class, VaadinIcons.TICKET, false),
    SCHAUUEBERSICHT("Schauübersicht", ShowUebersicht.class,  VaadinIcons.SHARE, false),
    WUERFE("Würfe", WurfUebersicht.class, VaadinIcons.MICROPHONE, false),
    TREE("Der Baum", TestTreeGrid.class, VaadinIcons.FILE_TREE, false),
    TREE2("Der Baum2", TestTreeGrid2.class, VaadinIcons.FILE_TREE, false);
    ;
 
    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private DashBoardViewType(final String viewName,
            final Class<? extends View> viewClass, final Resource icon,
            final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
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
