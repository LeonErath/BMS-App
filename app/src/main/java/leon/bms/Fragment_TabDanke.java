package leon.bms;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.franciscan.materialstepper.AbstractStep;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_TabDanke extends AbstractStep {


    public Fragment_TabDanke() {
        // Required empty public constructor
    }

    @Override
    public String name() {
        return "Danke";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__tab_danke, container, false);
    }

}
