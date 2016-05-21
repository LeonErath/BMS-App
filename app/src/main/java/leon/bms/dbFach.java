package leon.bms;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 21.05.2016.
 */
public class dbFach extends SugarRecord{
    int serverId;
    String kuerzel;
    String name;

    dbKurs kurs;

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
}
