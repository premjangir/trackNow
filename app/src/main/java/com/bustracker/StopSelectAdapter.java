package com.bustracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class StopSelectAdapter extends RecyclerView.Adapter<StopSelectAdapter.StopView>  {


    private final Context context;
    private ArrayList<BusStop> busStops = new ArrayList<>();
    private StopSelectInterface stopSelectInterface;


    public StopSelectAdapter(Context context, StopSelectInterface stopSelectInterface) {
        this.context = context;
        this.stopSelectInterface  = stopSelectInterface;
    }

    @NonNull
    @Override
    public StopView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view= inflater.inflate(R.layout.stop_select_item,parent,false);
        return new StopView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StopView holder, int position) {

        holder.stationName.setText(busStops.get(position).stopName);
    }

    public void setBusStops(ArrayList<BusStop> busStops) {
        this.busStops = busStops;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return busStops.size();
    }


    class StopView extends RecyclerView.ViewHolder{
        TextView stationName;

        public StopView(View itemView) {
            super(itemView);
            stationName = itemView.findViewById(R.id.stopName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopSelectInterface.onStopSelected( busStops.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }
    }

    public interface StopSelectInterface{
        void onStopSelected(BusStop busStop , int position);
    }
}
