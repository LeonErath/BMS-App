package leon.bms.database;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 21.05.2016.
 */
public class dbKursart extends SugarRecord {
    public String abkuerzung;
    public int gloablId;
    public String name;

    dbKurs kurs;

    public dbKursart() {
    }

    public dbKursart(String abkuerzung, int serverId, String name, dbKurs kurs) {
        this.abkuerzung = abkuerzung;
        this.gloablId = serverId;
        this.name = name;
        this.kurs = kurs;
    }

    public String getAbkuerzung() {
        return abkuerzung;
    }

    public void setAbkuerzung(String abkuerzung) {
        this.abkuerzung = abkuerzung;
    }

    public int getGloablId() {
        return gloablId;
    }

    public void setGloablId(int gloablId) {
        this.gloablId = gloablId;
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
