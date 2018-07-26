package com.bustracker;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BusStopManager {

    private static final String TAG = "BusStopManager";

    private ArrayList<BusStop> allBusStops = new ArrayList<>();

    private ArrayList<BusStop> busStops = new ArrayList<>();

    static android.os.Handler handler = new android.os.Handler();

    private StopLoadedInterface loadedInterface;

    private HashMap<String ,LatLng> busesLocation = new HashMap<>();



    //BUS ROUTE SELECT FIELDS
    private String busNo;
    private int sourcePosition;
    private int destPosition;

    private boolean isSourceSet = false;
    private boolean isDestSet  =false;
    private boolean isStopLoaded = false;
    private boolean isBusSet = false;

    private static BusStopManager stopManager;

    public synchronized static BusStopManager getInstance() {
        if (stopManager == null)
            stopManager = new BusStopManager();
        return stopManager;
    }

    public BusStopManager() {
        initBusStops();
    }

    private void initBusStops() {

        allBusStops.add(new BusStop("AC1", "Sanganer Police Station Bus stand", 1,
                new LatLng(26.815630, 75.784188)));
        allBusStops.add(new BusStop("AC1", "Tonk Fatak Bus Stand", 2, new LatLng(26.881542, 75.799691)));
        allBusStops.add(new BusStop("AC1", "Rambhag Bus stand", 3, new LatLng(26.899676, 75.812906)));
        allBusStops.add(new BusStop("AC1", "Ajmeri Gate Bus Stop ", 4, new LatLng(26.915295, 75.816929)));
        allBusStops.add(new BusStop("AC1", "Ramniwas Bagh", 5, new LatLng(26.915909, 75.820062)));
        allBusStops.add(new BusStop("AC1", "Sanganeri Gate Bus stand", 6, new LatLng(26.915220, 75.823463)));
        allBusStops.add(new BusStop("AC1", "Badi Chopar Bus stand", 7, new LatLng(26.922960, 75.827236)));
        allBusStops.add(new BusStop("AC1", "Ramdhar Mod Bus stand", 8, new LatLng(26.944026, 75.840958)));
        allBusStops.add(new BusStop("AC1", "Jal Mahal Bus stand", 9, new LatLng(26.952065, 75.841885)));
        allBusStops.add(new BusStop("AC1", "Amber Fort Bus stand", 10, new LatLng(26.987349, 75.854561)));

        allBusStops.add(new BusStop("AC1", "Sanganer Police Station Bus stand", 1,
                new LatLng(26.815630, 75.784188)));
        allBusStops.add(new BusStop("AC2", "Tonk Fatak Bus Stand", 2, new LatLng(26.881542, 75.799691)));
        allBusStops.add(new BusStop("AC2", "Rambhag Bus stand", 3, new LatLng(26.899676, 75.812906)));
        allBusStops.add(new BusStop("AC2", "Ajmeri Gate Bus Stop ", 4, new LatLng(26.915295, 75.816929)));
        allBusStops.add(new BusStop("AC2", "Ramniwas Bagh", 5, new LatLng(26.915909, 75.820062)));
        allBusStops.add(new BusStop("AC2", "Sanganeri Gate Bus stand", 6, new LatLng(26.915220, 75.823463)));


        allBusStops.add(new BusStop("3", "Ajmeri Gate Bus Stop ", 4, new LatLng(26.915295, 75.816929)));
        allBusStops.add(new BusStop("2", "Ramniwas Bagh", 5, new LatLng(26.915909, 75.820062)));
        allBusStops.add(new BusStop("3", "Sanganeri Gate Bus stand", 6, new LatLng(26.915220, 75.823463)));




        busesLocation.put("AC1",new LatLng(26.915295, 75.816929));
        busesLocation.put("AC2",new LatLng(26.952065, 75.841885));
        busesLocation.put("3",new LatLng(26.899676, 75.812906));

    }


    private void searchRouteForBus(String busNo) {
        this.busNo = busNo;
        for (int i = 0; i < allBusStops.size(); i++) {
            if (searchOnIndex(i, busNo))
                busStops.add(allBusStops.get(i));
        }
        isStopLoaded = true;
        if (loadedInterface != null)
            loadedInterface.onBusLoaded(busStops);
    }


    public void loadBusStops(final String busNo) {
        isStopLoaded = false;
        isBusSet = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchRouteForBus(busNo);
            }
        }, Util.getRamdomTime() * 1000);

    }

    public boolean isStopLoaded() {
        return isStopLoaded;
    }

    public void setLoadedInterface(StopLoadedInterface stopLoadedInterface) {
        this.loadedInterface = stopLoadedInterface;
    }

    private boolean searchOnIndex(int index, String busNo) {
        BusStop busStop = allBusStops.get(index);
        return busStop.busNo.equals(busNo);
    }


    public boolean isBusSet() {
        return isBusSet;
    }

    public ArrayList<BusStop> getAllBusStops() {
        return allBusStops;
    }


    public ArrayList<BusStop> getSearchedBusStop() {
        return busStops;
    }


    public interface StopLoadedInterface {
        void onBusLoaded(ArrayList<BusStop> busStops);

    }


    public void setSourcePosition(int sourcePosition) {
        this.sourcePosition = sourcePosition;
        isSourceSet = true;
    }

    public void setDestPosition(int destPosition) {
        this.destPosition = destPosition;
        isDestSet = true;
    }


    public BusStop getSource(){
        return busStops.get(sourcePosition);
    }


    public BusStop getDest(){
        return busStops.get(destPosition);
    }

    public String getBusNo() {
        return busNo;
    }

    public boolean isAllFieldsSet(){
        return isBusSet && isDestSet && isSourceSet;
    }

    public List<BusStop> allBetweenStation(){
        ArrayList<BusStop> busStops =new ArrayList<>();
        int i ,j;
        if (sourcePosition<destPosition){
            i=sourcePosition;
            j =destPosition;
        }else {
            i = destPosition;
            j= sourcePosition;
        }
        return busStops.subList(i,j);

    }
}
