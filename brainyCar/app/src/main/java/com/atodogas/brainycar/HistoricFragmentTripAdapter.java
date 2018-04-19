package com.atodogas.brainycar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by belenvb on 18/04/2018.
 */

public class HistoricFragmentTripAdapter extends RecyclerView.Adapter<HistoricFragmentTripAdapter.MyViewHolder> {
    public ArrayList<TripEntity> trips;

    public HistoricFragmentTripAdapter(ArrayList<TripEntity> trips){
        this.trips= trips;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_historic_trip_cardview, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("H:mm");
        holder.date.setText(DATE_FORMAT.format(trips.get(position).getDay()).toString());
        holder.startTime.setText(TIME_FORMAT.format(trips.get(position).getStartTime()).toString()+"h");
        holder.endTime.setText(TIME_FORMAT.format(trips.get(position).getEndTime()).toString()+"h");
        holder.startPlace.setText(trips.get(position).getStartPlace());
        holder.endPlace.setText(trips.get(position).getEndPlace());
        holder.km.setText(trips.get(position).getKm().toString()+"km");
        Time duration = new Time(trips.get(position).getEndTime().getTime() - trips.get(position).getStartTime().getTime());
        holder.duration.setText(TIME_FORMAT.format(duration).toString()+"h");
        holder.gasoline.setText(trips.get(position).getGasoline().toString()+"l");
        holder.points.setText(trips.get(position).getPoints().toString()+"p");
    }


    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date, startTime, endTime, startPlace, endPlace, km, duration, gasoline, points;
        public MyViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            startPlace = itemView.findViewById(R.id.startPlace);
            endPlace = itemView.findViewById(R.id.endPlace);
            km = itemView.findViewById(R.id.kmText);
            duration = itemView.findViewById(R.id.durationText);
            gasoline = itemView.findViewById(R.id.gasolineText);
            points = itemView.findViewById(R.id.pointsText);
        }
    }
}
