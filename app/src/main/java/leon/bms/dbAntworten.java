package leon.bms;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 27.02.2016.
 */

/** @dbAntworten ist eine Tabelle der Datenbank für die Antworten des Quizes.
 *  Sie verwaltet alle Antworten des Quizes und beinhaltet Filter für die weitere
 *  Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbAntworten extends SugarRecord {

    /** @richtig boolean ob die Antwort richtig oder falsch ist
     *  @anwort ist die Antwort die nacher angezeigt wird
     *  @langfassung ist nacher für die Ergebnisse wichtig um nachzuvollziehen warum die
     *  anwort falsch oder richtig ist
     *  @serverid ist für Relationships zwischen den Datenbanken wichtig und um bei Fragen zu den Antworten
     *  die Antworten besser rauszusuchen zu können
     */
    boolean richtig;
    String antwort;
    String langfassung;
    int serverid;

    dbFragen fragen;


    public dbAntworten() {
        // empty Constructor needed!
    }

    public dbAntworten(boolean richtig, String antwort, String langfassung, int idLocal, dbFragen fragen) {
        this.richtig = richtig;
        this.antwort = antwort;
        this.langfassung = langfassung;
        this.serverid = idLocal;
        this.fragen = fragen;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public String getLangfassung() {
        return langfassung;
    }

    public void setLangfassung(String langfassung) {
        this.langfassung = langfassung;
    }

    public boolean isRichtig() {
        return richtig;
    }

    public void setRichtig(boolean richtig) {
        this.richtig = richtig;
    }

    public String getAntwort() {
        return antwort;
    }

    public void setAntwort(String antwort) {
        this.antwort = antwort;
    }

    public dbFragen getFragen() {
        return fragen;
    }

    public void setFragen(dbFragen fragen) {
        this.fragen = fragen;
    }

    /** @getAntwort gibt alle Antowrten einer bestimmten ID zurück
     * @param idLocal ist die ID um die Antworten herauszusuchen
     *                sie ist gleich mit der ID der Frage sodass dadurch die Antworten ermittelt
     *                werden können
     * @return  wenn es die Antworten gibt , gibt er diese zurück . Wenn es sie nicht gibt , gibt
     *          er null zurück
     */
    public List<dbAntworten> getAntworten(int idLocal){
        List<dbAntworten> dbAntwortenList= dbAntworten.find(dbAntworten.class,"serverid = ?", String.valueOf(idLocal));
        if (dbAntwortenList.size() >0){
            return dbAntwortenList;
        }else {
            Log.d("dbAufgaben", "Keine Aufgaben gefuden");
        return null;}
    }

    /** @antwortVorhanden prüft ob speielle Antwort in der Datenbank vorhanden ist oder nicht
     * @param idlocal ist die ID um die Antworten herauszusuchen
     *                sie ist gleich mit der ID der Frage sodass dadurch die Antworten ermittelt
     *                werden können
     * @return  gibt true zurück wenn sie existiert und false wenn nicht
     */
    public boolean antwortVorhanden(int idlocal){
        List<dbAntworten> dbAntwortenList= dbAntworten.find(dbAntworten.class, "serverid = ?", String.valueOf(idlocal));
        if (dbAntwortenList.size() > 0){
            return true;
        }else {
            return false;
        }
    }
}
