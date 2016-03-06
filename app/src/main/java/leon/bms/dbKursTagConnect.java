package leon.bms;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon E on 06.03.2016.
 */
public class dbKursTagConnect extends SugarRecord {

    dbKurs kurs;
    dbWebsiteTag tag;

    public dbKursTagConnect() {
    }

    public dbKursTagConnect(dbKurs kurs, dbWebsiteTag tag) {
        this.kurs = kurs;
        this.tag = tag;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public dbWebsiteTag getTag() {
        return tag;
    }

    public void setTag(dbWebsiteTag tag) {
        this.tag = tag;
    }

    public List<dbWebsiteTag> getTags(long id){
        List<dbKursTagConnect> kursTagConnectList= dbKursTagConnect.find(dbKursTagConnect.class, "kurs = ?", String.valueOf(id));
        List<dbWebsiteTag> websiteTags = new ArrayList<>();
        if (kursTagConnectList.size() >0){
            for (dbKursTagConnect kursTagConnect: kursTagConnectList){
                websiteTags.add(kursTagConnect.getTag());
            }
            return websiteTags;
        }else {
            return null;
        }

    }
}
