package leon.bms.model;

import leon.bms.database.dbAufgabe;

/**
 * Created by Leon E on 25.05.2016.
 */
public class aufgabenModel {
    dbAufgabe aufgabe;
    int status;
    String textHeader;

    public aufgabenModel() {
    }

    public aufgabenModel(dbAufgabe aufgabe, int status, String textHeader) {
        this.aufgabe = aufgabe;
        this.status = status;
        this.textHeader = textHeader;
    }

    public dbAufgabe getAufgabe() {
        return aufgabe;
    }

    public void setAufgabe(dbAufgabe aufgabe) {
        this.aufgabe = aufgabe;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTextHeader() {
        return textHeader;
    }

    public void setTextHeader(String textHeader) {
        this.textHeader = textHeader;
    }
}
