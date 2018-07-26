package com.bustracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SearchActivity";

    private TextView busTextview;
    private TextView sourceStationTextiew;
    private TextView destinationStationTextview;
    private View busCardView;
    private View sourceButton;
    private View destButton;
    private Button search;

    private final int REQUEST_BUS_SELECT = 1;
    private final int REQ_SOURCE = 2;
    private final int REQ_DESTINATION = 3;

    private BusStopManager busStopManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bus_choose);

        busTextview = findViewById(R.id.busTextView);
        sourceStationTextiew = findViewById(R.id.sourceTextView);
        destinationStationTextview = findViewById(R.id.destinationTextView);
        busCardView = findViewById(R.id.busSearch);
        sourceButton = findViewById(R.id.sourceButton);
        destButton = findViewById(R.id.destButton);
        search = findViewById(R.id.search);


        busStopManager = BusStopManager.getInstance();

        busCardView.setOnClickListener(this);
        search.setOnClickListener(this);
        sourceButton.setOnClickListener(this);
        destButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, BusOrStationSelectActivity.class);

        switch (view.getId()) {

            case R.id.busSearch:
                intent.setAction("busNo");
                startActivityForResult(intent, REQUEST_BUS_SELECT);
                break;
            case R.id.sourceButton:

                if (!busStopManager.isBusSet()) {
                    Toast.makeText(this, "Please select Bus no first.", Toast.LENGTH_LONG).show();
                    return;
                }

                intent.setAction("source");
                startActivityForResult(intent, REQ_SOURCE);
                break;
            case R.id.destButton:
                if (!busStopManager.isBusSet()) {
                    Toast.makeText(this, "Please select Bus no first.", Toast.LENGTH_LONG).show();
                    return;
                }

                intent.setAction("dest");
                startActivityForResult(intent, REQ_DESTINATION);
                break;
            case R.id.search:

                if (!busStopManager.isAllFieldsSet()){
                    Toast.makeText(this,"Please select Source and Destination",Toast.LENGTH_LONG).show();
                    return;
                }

                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_BUS_SELECT:


                    busTextview.setText(data.getStringExtra("busNo"));
                    break;
                case REQ_SOURCE:

                    BusStop source =(BusStop) data.getParcelableExtra("stop");
                    sourceStationTextiew.setText(source.stopName);
                    break;
                case REQ_DESTINATION:
                    BusStop dest = data.getParcelableExtra("stop");
                    destinationStationTextview.setText(dest.stopName);

                    break;

            }

        }

    }
}
