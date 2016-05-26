package leon.bms.activites.main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import leon.bms.R;
import leon.bms.activites.note.NotenActivity;
import leon.bms.adapters.NotenAdapter;
import leon.bms.database.dbKurs;
import leon.bms.database.dbNote;
import leon.bms.model.notenModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Note extends Fragment implements FragmentLifecycle, NotenAdapter.ViewHolder.ClickListener {
    UltimateRecyclerView recyclerView;
    TextView textViewDurchschnitt;
    NotenAdapter notenAdapter;
    List<notenModel> notenModelList = new ArrayList<>();
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Boolean success = bundle.getBoolean("myKey");
            if (success) {
                notenAdapter.changeDataSet(notenModelList);
            } else {

            }
        }
    };
    Handler handler2 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String antwort = bundle.getString("myKey");
            textViewDurchschnitt.setText(antwort);
        }
    };

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

        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recyclerview);
        textViewDurchschnitt = (TextView) view.findViewById(R.id.textViewDurchschnitt);

        setUpRecyclerView();
        updateDurchschnitt();


    }

    private void setUpRecyclerView() {
        doInBackground();
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        notenAdapter = new NotenAdapter(getActivity(), notenModelList, this);
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
                        doInBackground();
                    }
                }, 1000);
            }
        });
    }

    private void doInBackground() {
        Runnable runnable = new Runnable() {
            public void run() {
                if (getNotenList() != null) {
                    notenModelList = getNotenList();
                }
                Message msg = handler.obtainMessage();
                boolean sucess = true;
                Bundle bundle = new Bundle();
                bundle.putBoolean("myKey", sucess);
                msg.setData(bundle);
                handler.sendMessage(msg);

            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }

    private List<notenModel> getNotenList() {
        if (new dbKurs().getAllActiveKurse() != null) {
            List<notenModel> notenModelList = new ArrayList<>();
            List<dbKurs> kursList = new dbKurs().getAllActiveKurse();
            for (dbKurs kurs : kursList) {
                notenModel notenModel1 = new notenModel();
                notenModel1.setKurs(kurs);
                if (kurs.getNoteWithId(kurs.getId(), 1) != null) {
                    List<dbNote> schriftlich = kurs.getNoteWithId(kurs.getId(), 1);
                    notenModel1.setSchriftlicheNoten(schriftlich);
                }
                if (kurs.getNoteWithId(kurs.getId(), 0) != null) {
                    List<dbNote> mündlich = kurs.getNoteWithId(kurs.getId(), 0);
                    notenModel1.setMündlicheNoten(mündlich);
                }
                notenModel1.setDurchschnitt(round(getNotenDurchschnitt(notenModel1), 1));

                notenModelList.add(notenModel1);
            }
            if (notenModelList.size() > 0) {
                return sortListASC(notenModelList);
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
        setUpRecyclerView();
        updateDurchschnitt();
    }



    private void updateDurchschnitt() {
        Runnable runnable = new Runnable() {
            public void run() {
                String antwort = "Gesamtdurchschnitt: NA";
                if (new dbKurs().getAllActiveKurse() != null) {
                    List<notenModel> notenModelList = new ArrayList<>();
                    List<dbKurs> kursList = new dbKurs().getAllActiveKurse();
                    for (dbKurs kurs : kursList) {
                        notenModel notenModel1 = new notenModel();
                        notenModel1.setKurs(kurs);

                        if (kurs.getNoteWithId(kurs.getId(), 1) != null) {
                            List<dbNote> schriftlich = kurs.getNoteWithId(kurs.getId(), 1);
                            notenModel1.setSchriftlicheNoten(schriftlich);
                        }
                        if (kurs.getNoteWithId(kurs.getId(), 0) != null) {
                            List<dbNote> mündlich = kurs.getNoteWithId(kurs.getId(), 0);
                            notenModel1.setMündlicheNoten(mündlich);
                        }

                        if (getNotenDurchschnitt(notenModel1) != 0) {
                            notenModel1.setDurchschnitt(round(getNotenDurchschnitt(notenModel1), 1));
                            notenModelList.add(notenModel1);
                        }
                    }
                    if (notenModelList != null && notenModelList.size() > 0) {
                        double gesamtdurchschnitt = 0;
                        for (notenModel notenModel1 : notenModelList) {
                            gesamtdurchschnitt += notenModel1.getDurchschnitt();
                        }
                        gesamtdurchschnitt = gesamtdurchschnitt / notenModelList.size();
                         antwort = "Gesamtdurchschnitt: " + round(gesamtdurchschnitt, 1);
                    } else {
                        antwort ="Gesamtdurchschnitt: NA";
                    }

                }

                Message msg = handler2.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("myKey",antwort);
                msg.setData(bundle);
                handler2.sendMessage(msg);

            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();


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
                newdurchschnitt += note1.punkte;
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
                return lhs.getKurs().name.compareTo(rhs.getKurs().name);
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
