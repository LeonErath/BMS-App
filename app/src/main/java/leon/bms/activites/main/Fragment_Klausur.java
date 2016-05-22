package leon.bms.activites.main;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import leon.bms.R;
import leon.bms.adapters.KlausurAdapter;
import leon.bms.controller.KlausurController;
import leon.bms.database.dbAufgabe;
import leon.bms.database.dbKlausur;


public class Fragment_Klausur extends Fragment {

    ToggleSwitch toggleSwitch;
    UltimateRecyclerView recyclerView;
    KlausurAdapter klausurAdapter;
    Boolean m_iAmVisible;
    KlausurController klausurController;

    public Fragment_Klausur() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__klausur, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toggleSwitch = (ToggleSwitch) view.findViewById(R.id.toggleButton);
        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position) {

            }
        });


        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recyclerview);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        klausurAdapter = new KlausurAdapter(getActivity());
        recyclerView.setAdapter(klausurAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               klausurController.checkUpdate();
                               Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
                               recyclerView.setRefreshing(false);
                               //   ultimateRecyclerView.scrollBy(0, -50);
                               mLayoutManager.scrollToPosition(0);
                           }
                       }, 1000);
           }
        });

        if (m_iAmVisible==true) {
            Log.d("Visibility","visible");
            if (klausurAdapter != null && klausurAdapter.getAdapterItemCount() == 0) {
                klausurController = new KlausurController(getActivity());
                klausurController.setUpdateListener(new KlausurController.OnUpdateListener() {
                    @Override
                    public void onSuccesss() {

                    }

                    @Override
                    public void onFailure() {
                        Log.d("Fragment_Klausur","failed");
                    }
                });
                klausurController.checkUpdate();

            }
        } else {

        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        m_iAmVisible = isVisibleToUser;

    }
}
