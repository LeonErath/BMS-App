package leon.bms;

/**
 * Created by Leon E on 10.05.2016.
 */
public abstract class  Constants {
    public static final String LOGIN_URL ="http://app.marienschule.de/api/login.php";
    public static final String GET_ALL_DATA_URL ="http://app.marienschule.de/api/getAllData.php";
    public static final String QUIZ_URL ="http://app.marienschule.de/api/quiz.php";
    public static final String QUIZ_KATALOG_URL ="http://app.marienschule.de/api/quizCatalog.php";
    public static final String VERTRETUNGSPLAN_URL ="http://app.marienschule.de/api/vertretungsplan.php";
    public static final String NACHRICHTEN_URL ="http://app.marienschule.de/api/nachrichten.php";

    public static String getLoginUrl() {
        return LOGIN_URL;
    }

    public static String getGetAllDataUrl() {
        return GET_ALL_DATA_URL;
    }

    public static String getQuizUrl() {
        return QUIZ_URL;
    }

    public static String getQuizKatalogUrl() {
        return QUIZ_KATALOG_URL;
    }

    public static String getVertretungsplanUrl() {
        return VERTRETUNGSPLAN_URL;
    }

    public static String getNachrichtenUrl() {
        return NACHRICHTEN_URL;
    }
}
