package leon.bms;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by Leon E on 05.05.2016.
 */
public class PublicClass  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}