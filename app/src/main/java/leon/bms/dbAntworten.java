package leon.bms;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 27.02.2016.
 */
public class dbAntworten extends SugarRecord {

    boolean richtig;
    String antwort;
    String langfassung;
    int serverid;

    dbFragen fragen;

    public dbAntworten() {
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

    public List<dbAntworten> getAntworten(int idLocal){
        List<dbAntworten> dbAntwortenList= dbAntworten.find(dbAntworten.class,"serverid = ?", String.valueOf(idLocal));
        if (dbAntwortenList.size() >0){
            return dbAntwortenList;
        }else {
            Log.d("dbAufgaben", "Keine Aufgaben gefuden");
        return null;}
    }
    public boolean antwortVorhanden(int idlocal){
        List<dbAntworten> dbAntwortenList= dbAntworten.find(dbAntworten.class, "serverid = ?", String.valueOf(idlocal));
        if (dbAntwortenList.size() > 0){
            return true;
        }else {
            return false;
        }
    }
}
