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
import android.widget.TextView;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nostra13.universalimageloader.utils.L;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import leon.bms.R;
import leon.bms.activites.note.NotenActivity;
import leon.bms.adapters.NotenAdapter;
import leon.bms.model.notenModel;
import leon.bms.realm.RealmQueries;
import leon.bms.realm.dbKurs;
import leon.bms.realm.dbNote;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Note extends Fragment implements FragmentLifecycle, NotenAdapter.ViewHolder.ClickListener {
    UltimateRecyclerView recyclerView;
    TextView textViewDurchschnitt;
    NotenAdapter notenAdapter;
    List<notenModel> notenModelList = new ArrayList<>();
    RealmQueries realmQueries;
    List<dbKurs> kursList;

    public Fragment_Note() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__note, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        realmQueries = new RealmQueries(getActivity());
        kursList = realmQueries.getAktiveKurse();


        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recyclerview);
        textViewDurchschnitt = (TextView) view.findViewById(R.id.textViewDurchschnitt);
        realmQueries = new RealmQueries(getActivity());
        setUpRecyclerView();
        updateDurchschnitt(kursList);


    }

    private void setUpRecyclerView() {

        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        notenAdapter = new NotenAdapter(getActivity(), getNotenList(kursList), this);
        recyclerView.setAdapter(notenAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
                        notenAdapter.changeDataSet(getNotenList(kursList));
                    }
                }, 1000);
            }
        });
    }


    private List<notenModel> getNotenList(List<dbKurs> aktiveKurse) {
        if (aktiveKurse != null) {
            List<notenModel> notenModelList2 = new ArrayList<>();
            for (dbKurs kurs : aktiveKurse) {
                notenModel notenModel1 = new notenModel();
                notenModel1.setKurs(kurs);
                if (realmQueries.getNoteFromKurs(kurs, true) != null) {
                    List<dbNote> schriftlich = realmQueries.getNoteFromKurs(kurs, true);
                    notenModel1.setSchriftlicheNoten(schriftlich);
                }
                if (realmQueries.getNoteFromKurs(kurs, false) != null) {
                    List<dbNote> mündlich = realmQueries.getNoteFromKurs(kurs, false);
                    notenModel1.setMündlicheNoten(mündlich);
                }
                notenModel1.setDurchschnitt(round(getNotenDurchschnitt(notenModel1), 1));

                notenModelList2.add(notenModel1);
            }
            if (notenModelList2.size() > 0) {
                return sortListASC(notenModelList2);
            }
        }
        return null;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void onResume() {
        super.onResume();
        kursList = realmQueries.getAktiveKurse();
        setUpRecyclerView();
        updateDurchschnitt(kursList);
    }


    private void updateDurchschnitt(List<dbKurs> aktiveKurse) {
        String antwort = "Gesamtdurchschnitt: NA";
        if (aktiveKurse != null) {
            List<notenModel> notenModelList2 = new ArrayList<>();
            for (dbKurs kurs : kursList) {
                notenModel notenModel1 = new notenModel();
                notenModel1.setKurs(kurs);

                if (realmQueries.getNoteFromKurs(kurs, true) != null) {
                    List<dbNote> schriftlich = realmQueries.getNoteFromKurs(kurs, true);
                    notenModel1.setSchriftlicheNoten(schriftlich);
                }
                if (realmQueries.getNoteFromKurs(kurs, false) != null) {
                    List<dbNote> mündlich = realmQueries.getNoteFromKurs(kurs, false);
                    notenModel1.setMündlicheNoten(mündlich);
                }

                if (getNotenDurchschnitt(notenModel1) != 0) {
                    notenModel1.setDurchschnitt(round(getNotenDurchschnitt(notenModel1), 1));
                    notenModelList2.add(notenModel1);
                }
            }
            if (notenModelList2 != null && notenModelList2.size() > 0) {
                double gesamtdurchschnitt = 0;
                for (notenModel notenModel1 : notenModelList2) {
                    gesamtdurchschnitt += notenModel1.getDurchschnitt();
                }
                gesamtdurchschnitt = gesamtdurchschnitt / notenModelList2.size();
                antwort = "Gesamtdurchschnitt: " + round(gesamtdurchschnitt, 1);
            } else {
                antwort = "Gesamtdurchschnitt: NA";
            }

        }
        textViewDurchschnitt.setText(antwort);
    }

    private double getNotenDurchschnitt(notenModel notenModel1) {
        notenModel note = notenModel1;
        List<dbNote> alleNoten = new ArrayList<>();
        if (note.getMündlicheNoten() != null) {
            alleNoten.addAll(note.getMündlicheNoten());
        }
        if (note.getSchriftlicheNoten() != null) {
            alleNoten.addAll(note.getSchriftlicheNoten());
        }
        if (alleNoten.size() > 0) {
            Double newdurchschnitt = 0.0;
            for (dbNote note1 : alleNoten) {
                newdurchschnitt += note1.getPunkte();
            }
            newdurchschnitt = newdurchschnitt / alleNoten.size();
            return newdurchschnitt;
        }
        return 0;

    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    public List<notenModel> sortListASC(List<notenModel> list) {
        Collections.sort(list, new Comparator<notenModel>() {
            @Override
            public int compare(notenModel lhs, notenModel rhs) {
                return lhs.getKurs().getName().compareTo(rhs.getKurs().getName());
            }
        });
        //Collections.reverse(list);
        return list;
    }

    @Override
    public void onItemClicked(int position) {
        if (notenAdapter.get(position) != null) {
            Log.d("Fragment_Note", "Item click");
            Intent intent = new Intent(getActivity(), NotenActivity.class);
            intent.putExtra("id", notenAdapter.get(position).getKurs().getId());
            getActivity().startActivity(intent);
        }


    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (notenAdapter.get(position) != null) {
            Log.d("Fragment_Note", "Item click");
            Intent intent = new Intent(getActivity(), NotenActivity.class);
            intent.putExtra("id", notenAdapter.get(position).getKurs().getId());
            getActivity().startActivity(intent);
        }
        return false;
    }
}
