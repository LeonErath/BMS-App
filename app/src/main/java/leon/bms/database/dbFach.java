package leon.bms.database;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 21.05.2016.
 */
public class dbFach extends SugarRecord{
    public int serverId;
    public String kuerzel;
    public String name;

    public dbKurs kurse;

    public dbFach() {

    }

    public dbFach(int serverId, String kuerzel, String name) {
        this.serverId = serverId;
        this.kuerzel = kuerzel;
        this.name = name;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<dbFach> getAllFaecher(){
        List<dbFach> dbKursList = dbFach.listAll(dbFach.class);
        return dbKursList;
    }

    public List<dbKurs> getAllKurse(long id) {
        return dbKurs.find(dbKurs.class, "fachnew = ?", String.valueOf(id));
    }

    /**
     * @getKursWithFach gibt den Kurs zurück des fachnew
     * Ist bei der AufgabenActivity von Relevanz
     */
    public dbFach getFachWithName(String fach) {
        List<dbFach> dbFachList = dbFach.find(dbFach.class, "name = ?", fach);
        if (dbFachList != null && dbFachList.size() == 1){
            return dbFachList.get(0);
        }
       return null;
    }

    public dbKurs getKursWithFachId(long fachid) {
        List<dbKurs> dbKursList = dbKurs.find(dbKurs.class, "fachnew = ? and aktiv = ?", String.valueOf(fachid),String.valueOf(1));
        if (dbKursList.size() == 1) {
            return dbKursList.get(0);
        } else {
            return null;
        }
    }
}
