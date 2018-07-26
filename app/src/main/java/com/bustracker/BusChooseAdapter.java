package com.bustracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class BusChooseAdapter extends RecyclerView.Adapter<BusChooseAdapter.SearchItemView> {


    private Context context ;
    private ArrayList<Pair<String,String>> buses;
    private BusChooseInterface busChooseInterface;


    public BusChooseAdapter(Context context , BusChooseInterface busChooseInterface){
        this.context = context;
        buses = new ArrayList<>();
        this.busChooseInterface =busChooseInterface;
    }

    @NonNull
    @Override
    public SearchItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view  = layoutInflater.inflate(R.layout.search_result_item , parent,false);
        return new SearchItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemView holder, int position) {
        Pair<String ,String> bus = buses.get(position);

        holder.busNo.setText(bus.first);
        holder.dest.setText("To "+bus.second);
    }

    @Override
    public int getItemCount() {
        return buses.size();
    }


    public void setData(ArrayList<Pair<String ,String>> results) {
       this.buses = results;
        notifyDataSetChanged();
    }

    public void clearData(){
        buses.clear();
        notifyDataSetChanged();
    }

    class SearchItemView extends RecyclerView.ViewHolder{
        TextView busNo;
        TextView dest;

        public SearchItemView(View itemView) {
            super(itemView);

            busNo = itemView.findViewById(R.id.busNo);
            dest = itemView.findViewById(R.id.dest);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                         busChooseInterface.onBusChoose(buses.get(getAdapterPosition()).first);
                }
            });
        }
    }



    public interface BusChooseInterface {
        void onBusChoose(String bus);
    }
}
