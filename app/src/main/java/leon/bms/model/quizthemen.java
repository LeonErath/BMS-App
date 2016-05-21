package leon.bms.model;

/**
 * Created by Leon E on 28.02.2016.
 */

/**
 * @quizthemen klasse die alle Daten für die QuizThemen Anzeige bietet. Constructor sowie Getter/Setter
 * Methode sind implementiert.
 */
public class quizthemen {

    public String themenbereich;
    public int fragen;
    public  double richtig;
    public  Long id;
    public  String kursId;
    public  String lehrer;
    public  String datum;

    public quizthemen() {
        //Constructor must be empty"
    }

    public quizthemen(String themenbereich, int fragen, double richtig, Long id, String kursId, String lehrer, String datum) {
        this.themenbereich = themenbereich;
        this.fragen = fragen;
        this.richtig = richtig;
        this.id = id;
        this.kursId = kursId;
        this.lehrer = lehrer;
        this.datum = datum;
    }

    public double getRichtig() {
        return richtig;
    }

    public void setRichtig(double richtig) {
        this.richtig = richtig;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKursId() {
        return kursId;
    }

    public void setKursId(String kursId) {
        this.kursId = kursId;
    }

    public String getLehrer() {
        return lehrer;
    }

    public void setLehrer(String lehrer) {
        this.lehrer = lehrer;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getThemenbereich() {
        return themenbereich;
    }

    public void setThemenbereich(String themenbereich) {
        this.themenbereich = themenbereich;
    }

    public int getFragen() {
        return fragen;
    }

    public void setFragen(int fragen) {
        this.fragen = fragen;
    }
}
