package leon.bms.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Leon E on 17.06.2016.
 */
public class dbKursart extends RealmObject {
    private int id;
    @Required
    private String name;
    @Required
    private String abbreviation;
    private int gloablId;

    private dbKurs kurs;

    public dbKursart() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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
