package leon.bms.database;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 27.02.2016.
 */

/**
 * @dbThemenbereich ist eine Tabelle der Datenbank für die Themenbereiche des Quiz.
 * Sie verwaltet alle Themenbereiche des Quizes und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbThemenbereich extends SugarRecord {
    /**
     * @zuletztAktualisiert wichtig für die Soriterung nach den neusten Themenbereichen
     * @serverid wichtig für das raussuchen der Themenbereich auf dem server
     * @infos wichtig für weiter Informationen zu diesem Themenbereich
     */
    public String name;
    public String zuletztAktualisiert;
    public int serverid;
    public int globaleid;
    public boolean katalogThemenbereich;
    public String beschreibung;

    public dbFach fach;
    public dbFragen fragen;

    public dbThemenbereich() {
        //empty Construcor needed
    }

    public dbThemenbereich(String name, String zuletztAktualisiert, int serverid, int globaleid, boolean katalogThemenbereich, String beschreibung, dbFach fach, dbFragen fragen) {
        this.name = name;
        this.zuletztAktualisiert = zuletztAktualisiert;
        this.serverid = serverid;
        this.globaleid = globaleid;
        this.katalogThemenbereich = katalogThemenbereich;
        this.beschreibung = beschreibung;
        this.fach = fach;
        this.fragen = fragen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZuletztAktualisiert() {
        return zuletztAktualisiert;
    }

    public void setZuletztAktualisiert(String zuletztAktualisiert) {
        this.zuletztAktualisiert = zuletztAktualisiert;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public int getGlobaleid() {
        return globaleid;
    }

    public void setGlobaleid(int globaleid) {
        this.globaleid = globaleid;
    }

    public boolean isKatalogThemenbereich() {
        return katalogThemenbereich;
    }

    public void setKatalogThemenbereich(boolean katalogThemenbereich) {
        this.katalogThemenbereich = katalogThemenbereich;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public dbFach getFach() {
        return fach;
    }

    public void setFach(dbFach fach) {
        this.fach = fach;
    }

    public dbFragen getFragen() {
        return fragen;
    }

    public void setFragen(dbFragen fragen) {
        this.fragen = fragen;
    }

    /**
     * @param idLocal ist die serverid mit der der Themenbereich gespeichert ist
     * @return gibt diesen Themenbereich zurück wenn er vorhanden ist sonst null
     * @getThemenbereich gibt einen Themebereich zurück der eine gewisse serverid besitzt
     */
    public dbThemenbereich getThemenbereich(int idLocal) {
        List<dbThemenbereich> dbThemenbereichList = dbThemenbereich.find(dbThemenbereich.class, "serverid = ?", String.valueOf(idLocal));
        if (dbThemenbereichList.size() == 1) {
            return dbThemenbereichList.get(0);
        } else {
            Log.d("dbThhemenbereich", "Keinen Themenbereich gefuden");
            return null;
        }
    }

    /**
     * @param id ist die serverid mit der der Themenbereich in der Datenbank gespeichert ist
     * @return gibt diesen Themenbereich zurück wenn er vorhanden ist sonst null
     * @getThemenbereich gibt einen Themebereich zurück der in der Datenbank ist
     */
    public dbThemenbereich getThemenbereich(long id) {
        List<dbThemenbereich> dbThemenbereichList = dbThemenbereich.find(dbThemenbereich.class, "serverid = ?", String.valueOf(id));
        if (dbThemenbereichList.size() == 1) {
            return dbThemenbereichList.get(0);
        } else {
            Log.d("dbThhemenbereich", "Keinen Themenbereich gefuden");
            return null;
        }
    }
    public dbThemenbereich getThemenbereichWithNamen(String name) {
        List<dbThemenbereich> dbThemenbereichList = dbThemenbereich.find(dbThemenbereich.class, "name = ?", String.valueOf(name));
        if (dbThemenbereichList.size() == 1) {
            return dbThemenbereichList.get(0);
        } else {
            Log.d("dbThhemenbereich", "Keinen Themenbereich gefuden");
            return null;
        }
    }

    /**
     * @param idlocal ist die serverid mit der der Themenbreich rausgesucht werden soll
     * @return wenn er vorhanden ist wird true zurückgegeben sonst false
     * @themenbereichVorhanden prüft ob der Themenbereich vorhanden ist
     */
    public boolean themenbereichVorhanden(int idlocal) {
        List<dbThemenbereich> dbThemenbereichList = dbThemenbereich.find(dbThemenbereich.class, "serverid = ?", String.valueOf(idlocal));
        if (dbThemenbereichList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param id ist die serverid des themenbereich
     * @return gibt alle Fragen zurück wenn sie vorhanden sind sonst null
     * @getFragen gibt alle Fragen eines bestimmten Themenbereiches zurück
     */
    public List<dbFragen> getFragen(long id) {
        if (find(dbFragen.class, "themenbereich = ?", String.valueOf(id)).size() != 0) {
            return find(dbFragen.class, "themenbereich = ?", String.valueOf(id));
        } else {
            return null;
        }
    }
}
