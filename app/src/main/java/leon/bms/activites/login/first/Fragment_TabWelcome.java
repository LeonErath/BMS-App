package leon.bms.activites.login.first;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.franciscan.materialstepper.AbstractStep;

import leon.bms.R;

/**
 * @Fragment_TabWelcome zeigt nur eine Willkommensnachricht an
 */
public class Fragment_TabWelcome extends AbstractStep {

    private int i = 1;
    int counter = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mStepper.getExtras().putInt("Click", i++);

        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__tab_welcome, container, false);
    }

    @Override
    public String name() {
        return "Welcome";
    }


    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public boolean nextIf() {
        if (counter == 1) {
            Log.d("TAG", "NEXT IF TRIGGGER __________________");
        }
        counter++;
        return i > 1;

    }

    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }
}
