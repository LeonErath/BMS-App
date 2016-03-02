package leon.bms;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 27.02.2016.
 */
public class dbThemenbereich extends SugarRecord {

    String name;
    String zuletztAktualisiert;
    int serverid;
    dbKurs kurs;
    dbFragen fragen;
    public dbThemenbereich() {
    }

    public dbThemenbereich(String name, String zuletztAktualisiert, Integer id, dbKurs kurs) {
        this.name = name;
        this.zuletztAktualisiert = zuletztAktualisiert;
        this.serverid = id;
        this.kurs = kurs;
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


    public Integer getIdLocal() {
        return serverid;
    }

    public void setIdLocal(Integer idLocal) {
        this.serverid = idLocal;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public dbThemenbereich getThemenbereich(int idLocal){
        List<dbThemenbereich> dbThemenbereichList= dbThemenbereich.find(dbThemenbereich.class,"serverid = ?", String.valueOf(idLocal));
        if (dbThemenbereichList.size() == 1){
            return dbThemenbereichList.get(0);
        }else {
            Log.d("dbThhemenbereich","Keinen Themenbereich gefuden");
            return null;}
    }
    public dbThemenbereich getThemenbereich(long id){
        List<dbThemenbereich> dbThemenbereichList= dbThemenbereich.find(dbThemenbereich.class,"id = ?", String.valueOf(id));
        if (dbThemenbereichList.size() == 1){
            return dbThemenbereichList.get(0);
        }else {
            Log.d("dbThhemenbereich","Keinen Themenbereich gefuden");
            return null;}
    }
    public boolean themenbereichVorhanden(int idlocal){
        List<dbThemenbereich> dbThemenbereichList= dbThemenbereich.find(dbThemenbereich.class,"serverid = ?", String.valueOf(idlocal));
        if (dbThemenbereichList.size() > 0){
            return true;
        }else {
            return false;
        }
    }
    public List<dbFragen> getFragen(long id){
        if (find(dbFragen.class, "themenbereich = ?", String.valueOf(id)).size() != 0){
            return find(dbFragen.class, "themenbereich = ?", String.valueOf(id));
        }else {
            return null;
        }
    }
}
