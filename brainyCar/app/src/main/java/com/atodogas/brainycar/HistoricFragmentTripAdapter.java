package com.atodogas.brainycar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atodogas.brainycar.DTOs.TripDTO;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by belenvb on 18/04/2018.
 */

public class HistoricFragmentTripAdapter extends RecyclerView.Adapter<HistoricFragmentTripAdapter.MyViewHolder> {

    public List<TripDTO> trips;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TripDTO item);
    }

    public HistoricFragmentTripAdapter(List<TripDTO> trips,  OnItemClickListener listener){
        this.trips= trips;
        this.listener = listener;
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
        holder.date.setText(DATE_FORMAT.format(trips.get(position).getStartDate()).toString());
        holder.startTime.setText(TIME_FORMAT.format(trips.get(position).getStartDate()).toString()+" h");
        holder.endTime.setText(TIME_FORMAT.format(trips.get(position).getEndDate()).toString()+" h");
        holder.startPlace.setText(trips.get(position).getStartPlace());
        holder.endPlace.setText(trips.get(position).getEndPlace());
        holder.km.setText(String.format("%.2f", trips.get(position).getKms())+" km");

        String duration = trips.get(position).getHours() + ":";
        duration += trips.get(position).getMinutes() > 9 ? trips.get(position).getMinutes() : "0" + trips.get(position).getMinutes() ;

        holder.duration.setText(duration + " h");
        holder.gasoline.setText(String.format("%.2f", trips.get(position).getFuelConsumptionAVG())+" l/km");
        holder.points.setText(String.format("%.2f", trips.get(position).getSpeedAVG())+" km/h");
        holder.bind(trips.get(position), listener);
    }


    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView date, startTime, endTime, startPlace, endPlace, km, duration, gasoline, points;
        private TripDTO tripEntity;

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

        public void bind(final TripDTO item, final OnItemClickListener listener) {
            tripEntity = item;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
