package leon.bms;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 27.02.2016.
 */

/**
 * @dbFragen ist eine Tabelle der Datenbank für die Fragen des Quizes.
 * Sie verwaltet alle Fragen des Quizes und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbFragen extends SugarRecord {

    /**
     * @richtigCounter wichtig für die Anzeige alle richtigen bzw. falschen Fragen
     * @schwierigkeit wichtig für die Errechnung des Scores für den User
     * @serverid wichtig um die Beziehung mit den Anworten herzustellen sowie zum raussuchen
     * von Fragen am Server
     * @langfassung Erklärung der Frage . Nacher für die Anzeige der richtigen Antwort wichtig.
     */
    String frage;
    String path;
    int serverid;
    int richtigCounter;
    int schwirigkeit;
    String langfassung;
    String date;
    String stufe;

    dbKurs kurs;
    dbAntworten antworten;
    dbThemenbereich themenbereich;

    public dbFragen() {
        //empty Constructor needed!
    }

    public dbFragen(String frage, String path, int idLocal, int richtigCounter, int schwirigkeit, String langfassung, dbKurs kurs, dbAntworten antworten, dbThemenbereich themenbereich) {
        this.frage = frage;
        this.path = path;
        this.serverid = idLocal;
        this.richtigCounter = richtigCounter;
        this.schwirigkeit = schwirigkeit;
        this.langfassung = langfassung;
        this.kurs = kurs;
        this.antworten = antworten;
        this.themenbereich = themenbereich;
    }

    public String getLangfassung() {
        return langfassung;
    }

    public void setLangfassung(String langfassung) {
        this.langfassung = langfassung;
    }

    public String getFrage() {
        return frage;
    }

    public void setFrage(String frage) {
        this.frage = frage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public int getRichtigCounter() {
        return richtigCounter;
    }

    public void setRichtigCounter(int richtigCounter) {
        this.richtigCounter = richtigCounter;
    }

    public int getSchwirigkeit() {
        return schwirigkeit;
    }

    public void setSchwirigkeit(int schwirigkeit) {
        this.schwirigkeit = schwirigkeit;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public dbAntworten getAntworten() {
        return antworten;
    }

    public void setAntworten(dbAntworten antworten) {
        this.antworten = antworten;
    }

    public dbThemenbereich getThemenbereich() {
        return themenbereich;
    }

    public void setThemenbereich(dbThemenbereich themenbereich) {
        this.themenbereich = themenbereich;
    }

    /**
     * @param idLocal ist die Serverid zum raussuchen der Frage
     * @return gibt die Frage zurck , wenn diese Frage vorhanden ist. Sonst wird null zurückgegeben
     * @getFrage gibt alle Fragen einer bestimmten ID zurück. Diese Id kann nur eine Frage besitzen
     */
    public dbFragen getFrage(int idLocal) {
        List<dbFragen> dbFragenList = new dbFragen().find(dbFragen.class, "serverid = ?", String.valueOf(idLocal));
        if (dbFragenList.size() == 1) {
            return dbFragenList.get(0);
        } else {
            return null;
        }
    }

    /**
     * @param id ist die ID der frage um die Antworten rauszusuchen
     * @return gibt eine Liste mit den Antowrten zurück falls diese existieren
     * @getAntworten gibt alle Antworten einer bestimmten Fragen zurück.
     */
    public List<dbAntworten> getAnworten(long id) {
        List<dbAntworten> dbAntwortenList = new dbAntworten().find(dbAntworten.class, "fragen = ?", String.valueOf(id));
        if (dbAntwortenList.size() > 0) {
            return dbAntwortenList;
        } else {
            return null;
        }
    }

    /**
     * @param idlocal ist die Serverid zum raussuchen der Frage
     * @return wenn die Frage vorhanden ist gibt er true zurück sonst false
     * @frageVorhanden guckt ob die Frage vorhanden ist anhand der Serverid
     */
    public boolean frageVorhanden(int idlocal) {
        List<dbFragen> dbFragenList = dbFragen.find(dbFragen.class, "serverid = ?", String.valueOf(idlocal));
        if (dbFragenList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
