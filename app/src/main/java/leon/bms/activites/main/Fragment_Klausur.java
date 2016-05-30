package leon.bms.activites.main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import leon.bms.R;
import leon.bms.activites.klausur.KlausurActivity;
import leon.bms.adapters.KlausurAdapter;
import leon.bms.controller.KlausurController;
import leon.bms.model.klausurModel;


public class Fragment_Klausur extends Fragment implements KlausurAdapter.ViewHolder.ClickListener, FragmentLifecycle {

    ToggleSwitch toggleSwitch;
    UltimateRecyclerView recyclerView;
    KlausurAdapter klausurAdapter;
    Boolean m_iAmVisible = false;
    KlausurController klausurController;
    List<klausurModel> klausurList = new ArrayList<>();

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

        klausurController = new KlausurController(getActivity());
        klausurController.setUpdateListener(new KlausurController.OnUpdateListener() {
            @Override
            public void onSuccesss() {
                if (klausurController.getAllKlausuren() != null) {
                    klausurList = klausurController.getAllKlausuren();
                    klausurAdapter.changeDataSet(klausurList);
                }
            }

            @Override
            public void onFailure() {
                Log.d("Fragment_Klausur", "failed");
            }
        });

        toggleSwitch = (ToggleSwitch) view.findViewById(R.id.toggleButton);
        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position) {
                if (position == 1) {
                    if (klausurAdapter != null && klausurAdapter.getAdapterItemCount() != 0) {
                        List<klausurModel> klausurModels = klausurController.getAllKlausuren();
                        List<klausurModel> aktuelleKlausren = new ArrayList<klausurModel>();
                        for (klausurModel klausur : klausurModels) {
                            if (!klausur.inThePast) {
                                aktuelleKlausren.add(klausur);
                            }
                        }
                        klausurAdapter.changeDataSet(aktuelleKlausren);
                    }
                } else {
                    if (klausurController.getAllKlausuren() != null) {
                        klausurList = klausurController.getAllKlausuren();
                        klausurAdapter.changeDataSet(klausurList);
                    }
                }
            }
        });


        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recyclerview);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        klausurAdapter = new KlausurAdapter(getActivity(), klausurList, this);
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

        if (klausurAdapter != null && klausurAdapter.getAdapterItemCount() == 0) {
            klausurController.checkUpdate();

        }

    }

    @Override
    public void onItemClicked(int position) {
        if (klausurAdapter.get(position )!= null) {
            Log.d("Fragment_Klausur", "Item click");
            Intent intent = new Intent(getActivity(), KlausurActivity.class);
            intent.putExtra("id", klausurAdapter.get(position).klausur.getId());
            getActivity().startActivity(intent);
        }

    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (klausurAdapter.get(position )!= null) {
            Log.d("Fragment_Klausur", "Item click");
            Intent intent = new Intent(getActivity(), KlausurActivity.class);
            intent.putExtra("id", klausurAdapter.get(position).klausur.getId());
            getActivity().startActivity(intent);
        }
        return false;
    }



    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }
}
