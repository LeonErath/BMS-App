package leon.bms;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.franciscan.materialstepper.AbstractStep;


public class Fragment_TabWelcome extends AbstractStep {

    private int i = 1;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mStepper.getExtras().putInt("Click",i++);

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
        return i > 1;
    }

    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }
}
