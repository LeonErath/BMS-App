package leon.bms.database;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 21.05.2016.
 */
public class dbSchuljahr extends SugarRecord{
    public String descriptionString;
    public String endDate;
    public int serverid;
    public String startDate;

    public dbSchuljahr() {

    }

    public dbSchuljahr(String descriptionString, String endDate, int id, String startDate) {
        this.descriptionString = descriptionString;
        this.endDate = endDate;
        this.serverid = id;
        this.startDate = startDate;
    }


    public String getDescriptionString() {
        return descriptionString;
    }

    public void setDescriptionString(String descriptionString) {
        this.descriptionString = descriptionString;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
