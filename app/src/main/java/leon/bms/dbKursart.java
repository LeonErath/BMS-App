package leon.bms;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 21.05.2016.
 */
public class dbKursart extends SugarRecord {
    String abkuerzung;
    int serverId;
    String name;

    dbKurs kurs;

    public dbKursart() {
    }

    public dbKursart(String abkuerzung, int serverId, String name, dbKurs kurs) {
        this.abkuerzung = abkuerzung;
        this.serverId = serverId;
        this.name = name;
        this.kurs = kurs;
    }

    public String getAbkuerzung() {
        return abkuerzung;
    }

    public void setAbkuerzung(String abkuerzung) {
        this.abkuerzung = abkuerzung;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }
}
