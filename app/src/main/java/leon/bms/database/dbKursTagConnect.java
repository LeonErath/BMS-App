package leon.bms.database;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon E on 06.03.2016.
 */

/**
 * @dbKursTagConnext ist eine Tabelle der Datenbank für die Verbindung vo Kursen und TAGs.
 * Sie verwaltet alle Kurse die TAGs besitzen und andersrum und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbKursTagConnect extends SugarRecord {

    // WICHTIG diese Klasse ist für eine Many-to-Many Relationship zuständig
    public dbKurs kurs;
    public dbWebsiteTag tag;

    public dbKursTagConnect() {
        //empty Constructor needed!
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

    /**
     * gibt alle TAGs eines bestimmten Kurs zurück
     *
     * @param id ist die ID des Kurses wovon die TAGs rausgesucht werden sollen
     * @return gibt alle websiteTAGs vom Kurs wenn diese Vorhanden sind sonst null
     */
    public List<dbWebsiteTag> getTags(long id) {
        List<dbKursTagConnect> kursTagConnectList = dbKursTagConnect.find(dbKursTagConnect.class, "kurs = ?", String.valueOf(id));
        List<dbWebsiteTag> websiteTags = new ArrayList<>();
        if (kursTagConnectList.size() > 0) {
            for (dbKursTagConnect kursTagConnect : kursTagConnectList) {
                websiteTags.add(kursTagConnect.getTag());
            }
            return websiteTags;
        } else {
            return null;
        }

    }
}
