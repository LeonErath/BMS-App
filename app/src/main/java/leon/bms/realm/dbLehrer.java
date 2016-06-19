package leon.bms.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


/**
 * Created by Leon E on 17.06.2016.
 */
public class dbLehrer extends RealmObject {
    @PrimaryKey  @Required
    private Integer id;
    private String title;
    private String email;
    private String abbreviation;
    private String first_name;
    private String last_name;

    private dbKurs kurs;
    private dbSchulstunde schulstunde;
    private dbKlausuraufsicht aufsicht;

    public dbLehrer() {
        //empty Constructor needed!
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public dbSchulstunde getSchulstunde() {
        return schulstunde;
    }

    public void setSchulstunde(dbSchulstunde schulstunde) {
        this.schulstunde = schulstunde;
    }

    public dbKlausuraufsicht getAufsicht() {
        return aufsicht;
    }

    public void setAufsicht(dbKlausuraufsicht aufsicht) {
        this.aufsicht = aufsicht;
    }
}
