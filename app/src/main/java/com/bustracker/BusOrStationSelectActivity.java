package com.bustracker;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Explode;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BusOrStationSelectActivity extends AppCompatActivity implements TextView.OnEditorActionListener,
        TextWatcher {

    private static final String TAG = "BusOrStationSelectActivity";

    private EditText searchEditText;
    private CardView backButton;
    private ProgressBar progressBar;
    private RecyclerView searchList;

    private HashMap<String,String> allBuses = new HashMap<>();



    private BusChooseAdapter busSelectAdapter;
    private StopSelectAdapter stopSelectAdapter;
    private Handler handler = new Handler();

    private BusStopManager stopManager;

    private Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
                  searchForBus();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.searchEditText);
        backButton = findViewById(R.id.backButton);
        searchEditText.setOnEditorActionListener(this);
        searchEditText.addTextChangedListener(this);
        searchList = findViewById(R.id.searchList);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        progressBar = findViewById(R.id.progressBar);

        final String action = getIntent().getAction();

        searchList.setLayoutManager(new LinearLayoutManager(this));
        stopManager  =BusStopManager.getInstance();

        if (action.equals("busNo")){

            busSelectAdapter = new BusChooseAdapter(this, new BusChooseAdapter.BusChooseInterface() {
                @Override
                public void onBusChoose(String bus) {
                    stopManager.loadBusStops(bus);
                    Intent intent = new Intent();
                    intent.putExtra("busNo",bus);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });

            searchList.setAdapter(busSelectAdapter);

        }else{
            stopSelectAdapter = new StopSelectAdapter(this, new StopSelectAdapter.StopSelectInterface() {
                    @Override
                    public void onStopSelected(BusStop busStop , int position) {
                    Intent intent = new Intent();

                    if (action.equals("source"))
                        stopManager.setSourcePosition(position);
                    else stopManager.setDestPosition(position);


                    intent.putExtra("stop",busStop);
                    setResult(RESULT_OK,intent);
                    finish();

                }
            });

            searchList.setAdapter(stopSelectAdapter);

            if (stopManager.isStopLoaded()){
                stopSelectAdapter.setBusStops(stopManager.getAllBusStops());

            }else {
                progressBar.setVisibility(View.VISIBLE);
                 stopManager.setLoadedInterface(new BusStopManager.StopLoadedInterface() {
                  @Override
                  public void onBusLoaded(ArrayList<BusStop> busStops) {
                      stopSelectAdapter.setBusStops(busStops);
                      progressBar.setVisibility(View.GONE);
                  }
              });

            }
        }

        initBus();
    }


    @VisibleForTesting
    private void initBus(){

        allBuses.put("1","Demo");
        allBuses.put("2","Demo");
        allBuses.put("3","Demo");
        allBuses.put("4","Demo");
        allBuses.put("5","Demo");
        allBuses.put("6","Demo");
        allBuses.put("AC1","Demo");
        allBuses.put("AC2","Demo");
        allBuses.put("AC3","Demo");
        allBuses.put("B1","Demo");
        allBuses.put("B2","Demo");
        allBuses.put("B3","Demo");
        allBuses.put("1A","Demo");
        allBuses.put("2B","Demo");
        allBuses.put("3C","Demo");

    }


    private void searchForBus() {
         ArrayList<Pair<String,String>> searchBuses = new ArrayList<>();

        if (searchEditText.getText()!=null && !TextUtils.isEmpty(searchEditText.getText())){
            Iterator<String> allStation = allBuses.keySet().iterator();
            while (allStation.hasNext()) {
               String bus = allStation.next();
               if (bus.contains(searchEditText.getText().toString().toUpperCase())){
                   Pair<String,String> pair = new Pair<>(bus,allBuses.get(bus));
                   searchBuses.add(pair);
               }
            }
            busSelectAdapter.setData(searchBuses);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
           searchPostDelayed();
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if( i2==0){
            busSelectAdapter.clearData();
           return;
        }

        searchPostDelayed();

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    @VisibleForTesting
    private void searchPostDelayed(){
        progressBar.setVisibility(View.VISIBLE);
        handler.removeCallbacks(searchRunnable);
        handler.postDelayed(searchRunnable,Util.getRamdomTime() * 1000);
    }

    private void exitTransition() {
        getWindow().setExitTransition(new Explode());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitTransition();

    }
}
