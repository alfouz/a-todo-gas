package com.atodogas.brainycar.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.atodogas.brainycar.DTOs.AchievementDTO;
import com.atodogas.brainycar.Database.AppDatabase;
import com.atodogas.brainycar.Database.DAOs.AchievementDao;
import com.atodogas.brainycar.Database.Entities.AchievementEntity;
import com.atodogas.brainycar.Database.Entities.ChallengeEntity;

import java.util.ArrayList;
import java.util.List;

public class GetAllAchievementsBD extends AsyncTask<Integer, Void, List<AchievementDTO>> {
    CallbackInterface callback;
    AppDatabase db;

    public GetAllAchievementsBD(CallbackInterface callback, Context context) {
        this.callback = callback;
        this.db = AppDatabase.getInstance(context);
    }

    @Override
    protected List<AchievementDTO> doInBackground(Integer... integers) {
        int userId = integers[0];

        List<AchievementDTO> achievementsDTO = new ArrayList<>();
        List<ChallengeEntity> challengeEntities = db.challengeDao().getAllChallenge();
        List<AchievementEntity> achievementEntities = db.achievementDao().getUserAchievements(userId);

        for( ChallengeEntity challengeEntity : challengeEntities){
            AchievementDTO achievementDTO = new AchievementDTO();
            achievementDTO.setTitle(challengeEntity.getTitle());
            achievementDTO.setDescription(challengeEntity.getDescription());
            achievementDTO.setLevel(challengeEntity.getLevel());

            for(AchievementEntity achievementEntity : achievementEntities){
                if(achievementEntity.getIdChallenge() == challengeEntity.getId()){
                    achievementDTO.setAchieve(true);
                    break;
                }
            }

            achievementsDTO.add(achievementDTO);
        }

        return achievementsDTO;
    }

    @Override
    protected void onPostExecute(List<AchievementDTO> achievementsDTO) {
        super.onPostExecute(achievementsDTO);

        callback.doCallback(achievementsDTO);
    }
}