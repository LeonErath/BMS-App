package leon.bms.activites.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leon.bms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Note extends Fragment {


    public Fragment_Note() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__note, container, false);
    }

}
