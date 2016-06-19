package leon.bms.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


/**
 * Created by Leon E on 17.06.2016.
 */
public class dbRaum extends RealmObject {
    @PrimaryKey @Required
    private Integer id;
    @Required
    private String name;
    private Boolean beamer;
    private String note;
    private Boolean computer;
    private String use;
    private Integer capacity;
    private Boolean speaker;
    // defining a relationship
    private dbKlausur klausur;
    private dbSchulstunde stunden;
    private dbVertretung vertretung;

    public dbRaum() {
        //empty Contructor needed!
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBeamer() {
        return beamer;
    }

    public void setBeamer(Boolean beamer) {
        this.beamer = beamer;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getComputer() {
        return computer;
    }

    public void setComputer(Boolean computer) {
        this.computer = computer;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Boolean speaker) {
        this.speaker = speaker;
    }

    public dbKlausur getKlausur() {
        return klausur;
    }

    public void setKlausur(dbKlausur klausur) {
        this.klausur = klausur;
    }

    public dbSchulstunde getStunden() {
        return stunden;
    }

    public void setStunden(dbSchulstunde stunden) {
        this.stunden = stunden;
    }

    public dbVertretung getVertretung() {
        return vertretung;
    }

    public void setVertretung(dbVertretung vertretung) {
        this.vertretung = vertretung;
    }
}
