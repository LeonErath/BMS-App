package leon.bms.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leon E on 12.03.2016.
 */
public class quizfragen implements Serializable {
    public String themenbereichFrage;
    public  String frage;
    public int fradeID;
    public  boolean richtigOderFalsch;
    public  String langfassung;
    public  List<quizantworten> quizantwortenList;

    public quizfragen() {
    }

    public quizfragen(String themenbereichFrage, String frage, int fradeID, boolean richtigOderFalsch, String langfassung, List<quizantworten> quizantwortenList) {
        this.themenbereichFrage = themenbereichFrage;
        this.frage = frage;
        this.fradeID = fradeID;
        this.richtigOderFalsch = richtigOderFalsch;
        this.langfassung = langfassung;
        this.quizantwortenList = quizantwortenList;
    }

    public String getThemenbereichFrage() {
        return themenbereichFrage;
    }

    public void setThemenbereichFrage(String themenbereichFrage) {
        this.themenbereichFrage = themenbereichFrage;
    }

    public String getFrage() {
        return frage;
    }

    public void setFrage(String frage) {
        this.frage = frage;
    }

    public int getFradeID() {
        return fradeID;
    }

    public void setFradeID(int fradeID) {
        this.fradeID = fradeID;
    }

    public boolean isRichtigOderFalsch() {
        return richtigOderFalsch;
    }

    public void setRichtigOderFalsch(boolean richtigOderFalsch) {
        this.richtigOderFalsch = richtigOderFalsch;
    }

    public String getLangfassung() {
        return langfassung;
    }

    public void setLangfassung(String langfassung) {
        this.langfassung = langfassung;
    }

    public List<quizantworten> getQuizantwortenList() {
        return quizantwortenList;
    }

    public void setQuizantwortenList(List<quizantworten> quizantwortenList) {
        this.quizantwortenList = quizantwortenList;
    }

}
