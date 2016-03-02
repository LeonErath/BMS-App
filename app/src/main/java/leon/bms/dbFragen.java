package leon.bms;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 27.02.2016.
 */
public class dbFragen extends SugarRecord {

    String frage;
    String path;
    int serverid;
    int richtigCounter;
    int schwirigkeit;
    String langfassung;

    dbKurs kurs;
    dbAntworten antworten;
    dbThemenbereich themenbereich;

    public dbFragen() {
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

    public dbFragen getFrage(int idLocal){
        List<dbFragen> dbFragenList = new dbFragen().find(dbFragen.class,"serverid = ?",String.valueOf(idLocal));
        if (dbFragenList.size() == 1){
            return dbFragenList.get(0);
        }else {
            return null;
        }
    }
    public List<dbAntworten> getAnworten(long id){
        List<dbAntworten> dbAntwortenList = new dbAntworten().find(dbAntworten.class,"fragen = ?",String.valueOf(id));
        if (dbAntwortenList.size() > 0){
            return dbAntwortenList;
        }else {
            return null;
        }
    }

    public boolean frageVorhanden(int idlocal){
        List<dbFragen> dbFragenList= dbFragen.find(dbFragen.class, "serverid = ?", String.valueOf(idlocal));
        if (dbFragenList.size() > 0){
            return true;
        }else {
            return false;
        }
    }
}
