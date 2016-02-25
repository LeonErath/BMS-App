package leon.bms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon E on 23.02.2016.
 */
public class Highlight {

    dbAufgabe aufgabe;
    WebsiteArtikel websiteArtikel;
    int type;


    public Highlight() {
    }

    public Highlight(dbAufgabe aufgabe) {
        this.aufgabe = aufgabe;
        this.type = 0;
    }

    public Highlight(WebsiteArtikel websiteArtikel) {
        this.websiteArtikel = websiteArtikel;
        this.type = 1;
    }

    public dbAufgabe getAufgabe() {
        return aufgabe;
    }

    public void setAufgabe(dbAufgabe aufgabe) {
        this.aufgabe = aufgabe;
    }

    public WebsiteArtikel getWebsiteArtikel() {
        return websiteArtikel;
    }

    public void setWebsiteArtikel(WebsiteArtikel websiteArtikel) {
        this.websiteArtikel = websiteArtikel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
