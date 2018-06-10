package com.atodogas.brainycar;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atodogas.brainycar.DTOs.AchievementDTO;
import com.atodogas.brainycar.DTOs.TripDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by belenvb on 21/04/2018.
 */

public class ProfileFragmentAchievementAdapter extends RecyclerView.Adapter<ProfileFragmentAchievementAdapter.MyViewHolder> {

    public ArrayList<AchievementDTO> achievements;
    private final ProfileFragmentAchievementAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AchievementDTO item);
    }

    public ProfileFragmentAchievementAdapter(ArrayList<AchievementDTO> achievements, ProfileFragmentAchievementAdapter.OnItemClickListener listener){
        this.achievements= achievements;
        this.listener = listener;
    }

    @Override
    public ProfileFragmentAchievementAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_profile_achievement_cardview, parent, false);
        return new ProfileFragmentAchievementAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ProfileFragmentAchievementAdapter.MyViewHolder holder, int position) {
        holder.achievementTitle.setText(achievements.get(position).getTitle());
        holder.achievementDescription.setText(achievements.get(position).getDescription());
        Integer achievementLevel = achievements.get(position).getLevel();
        Boolean isAchieved = achievements.get(position).getAchieve();
        if (isAchieved) {
            holder.achievementIcon.setImageResource(R.drawable.ic_check);
        } else {
            holder.achievementIcon.setImageResource(R.drawable.ic_uncheck);
        }
        switch (achievementLevel) {
            case 1:
                holder.achievementLevel.setText(R.string.gold);
                break;
            case 2:
                holder.achievementLevel.setText(R.string.silver);
                break;
            case 3:
                holder.achievementLevel.setText(R.string.bronze);
                break;
        }

        holder.bind(achievements.get(position), listener);
    }


    @Override
    public int getItemCount() {
        return achievements.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView achievementCardView;
        private TextView achievementTitle, achievementDescription, achievementLevel;
        private ImageView achievementIcon;
        private AchievementDTO achievement;

        public MyViewHolder(View itemView) {
            super(itemView);
            achievementCardView = itemView.findViewById(R.id.achievementCardView);
            achievementTitle = itemView.findViewById(R.id.achievementTitle);
            achievementDescription = itemView.findViewById(R.id.achievementDescription);
            achievementLevel = itemView.findViewById(R.id.achievementLevel);
            achievementIcon = itemView.findViewById(R.id.achievedIcon);
        }

        public void bind(final AchievementDTO item, final ProfileFragmentAchievementAdapter.OnItemClickListener listener) {
            achievement = item;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(achievement);
                }
            });
        }
    }
}
