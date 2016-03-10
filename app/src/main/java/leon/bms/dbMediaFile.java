package leon.bms;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 17.02.2016.
 */

/**
 * @dbMediaFile ist eine Tabelle der Datenbank für die Bilder
 * Sie verwaltet alle Bilder der Aufgaben und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbMediaFile extends SugarRecord<dbMediaFile> {

    String path;
    dbAufgabe aufgaben;

    public dbMediaFile() {
        //empty Constructor needed!
    }

    public dbMediaFile(String path, dbAufgabe aufgaben) {
        this.path = path;

        this.aufgaben = aufgaben;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public dbAufgabe getAufgabe() {
        return aufgaben;
    }

    public void setAufgabe(dbAufgabe aufgaben) {
        this.aufgaben = aufgaben;
    }
}
