package com.atodogas.brainycar.AsyncTasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.atodogas.brainycar.Database.AppDatabase;
import com.atodogas.brainycar.Database.Entities.AchievementEntity;
import com.atodogas.brainycar.Database.Entities.CarEntity;
import com.atodogas.brainycar.Database.Entities.ChallengeEntity;
import com.atodogas.brainycar.Database.Entities.TripDataEntity;
import com.atodogas.brainycar.Database.Entities.TripEntity;
import com.atodogas.brainycar.R;
import com.atodogas.brainycar.Utils.Distances;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UpdateCarTripInfoBD extends AsyncTask<TripEntity, Void, Void> {
    private AppDatabase db;
    private Context context;
    public static final String NOTIFICATION_CHANNEL_ID_NEW_ACHIEVEMENT = "com.atodogas.brainycar.AsyncTasks.UpdateCarTripInfoBD.NEW_ACHIEVEMENT";

    public UpdateCarTripInfoBD(Context context) {
        this.context = context;
        this.db = AppDatabase.getInstance(context);
    }

    @Override
    protected Void doInBackground(TripEntity... tripEntities) {
        TripEntity trip = tripEntities[0];
        List<TripDataEntity> tripDatas = db.tripDataDao().getAllTripData(trip.getId());
        if(tripDatas.size() <= 0){
            db.tripDao().deleteTrip(trip);
            return null;
        }
        
        double latStart = tripDatas.get(0).getLatitude();
        double lonStart = tripDatas.get(0).getLongitude();
        double latEnd = tripDatas.get(tripDatas.size() - 1).getLatitude();
        double lonEnd = tripDatas.get(tripDatas.size() - 1).getLongitude();

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        String startPlace = "No data";
        String endPlace ="No data";
        try {
            List<Address> addresses = geocoder.getFromLocation(latStart, lonStart, 1);
            Address address = null;
            if(addresses.size() > 0){
                address = addresses.get(0);
                startPlace =  address.getThoroughfare() + ", ";

                if(address.getLocality() != null){
                    startPlace += address.getLocality();
                }
                else {
                    startPlace += address.getSubAdminArea();
                }
            }


            addresses = geocoder.getFromLocation(latEnd, lonEnd, 1);
            if(addresses.size() > 0) {
                address = addresses.get(0);
                endPlace =  address.getThoroughfare() + ", ";

                if(address.getLocality() != null){
                    endPlace += address.getLocality();
                }
                else {
                    endPlace += address.getSubAdminArea();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Calculo de velocidad media y consumo medio del viaje
        int speedAVG = 0;
        int speedNot0 = 0;
        float mpg = 0;
        float numberMPGNotEmpty = 0;
        double lastLatitude = 255;
        double lastLongitude = 255;
        float kms = 0;
        for (TripDataEntity t : tripDatas){
            if(t.getSpeed() > 0){
                speedNot0++;
                speedAVG += t.getSpeed();

                if(t.getMAF() > 0){
                    numberMPGNotEmpty++;
                    mpg += (float) (t.getSpeed() * 4.795324606 / t.getMAF());
                }
            }

            if(lastLatitude == 255 && lastLongitude == 255){
                lastLatitude = t.getLatitude();
                lastLongitude = t.getLongitude();
            }
            else {
                kms += Distances.calculateDistance(lastLatitude, lastLongitude, t.getLatitude(), t.getLongitude());
                lastLatitude = t.getLatitude();
                lastLongitude = t.getLongitude();
            }
        }

        float l100kmAVG = 0;
        if(numberMPGNotEmpty > 0){
            l100kmAVG = (float) (235.2137783/(mpg / numberMPGNotEmpty));
        }

        if(speedNot0 > 0){
            speedAVG /= speedNot0;
        }

        //Actualización del viaje y los datos generales del coche
        trip.setStartPlace(startPlace);
        trip.setEndPlace(endPlace);

        CarEntity car = db.carDao().getCarById(trip.getIdCar());
        List<TripEntity> trips = db.tripDao().getAllCarTrips(car.getId());
        if(trips.size() == 1){
            car.setAVGSpeed(speedAVG);
            car.setAVGFuelConsumption(car.getAVGFuelConsumption());
        }
        else {
            car.setAVGSpeed((car.getAVGSpeed() * (trips.size() - 1) + speedAVG) / trips.size());
            car.setAVGFuelConsumption((car.getAVGFuelConsumption() * (trips.size() - 1) + l100kmAVG)/trips.size());
        }

        car.setKms(car.getKms() + kms);

        db.carDao().updateCar(car);
        db.tripDao().updateTrip(trip);

        checkAchievements(car.getIdUser(), kms, speedAVG, tripDatas);

        return null;
    }

    private void checkAchievements(int idUser, float kms, float speedAvg, List<TripDataEntity> tripDataEntities){
        List<ChallengeEntity> challengesNotAchievement = db.challengeDao().getChallengesNotAchievement(idUser);

        boolean newAchievementDetected = false;
        for(ChallengeEntity challenge : challengesNotAchievement){
            boolean isAchieve = false;
            if(challenge.getVariable().equals("kms")){
                isAchieve = false;
                if(challenge.getOperator().equals(">=") && kms >= challenge.getObjective() ){
                    isAchieve = true;
                }
            }
            else if(challenge.getVariable().equals("rpm") && speedAvg > 1){
                isAchieve = true;
                for(TripDataEntity tripData : tripDataEntities){
                    if(challenge.getOperator().equals("<=") && tripData.getRPM() > challenge.getObjective()){
                        isAchieve = false;
                    }
                }
            }
            else if(challenge.getVariable().equals("speedAVG")){
                isAchieve = false;
                if(challenge.getOperator().equals(">=") && speedAvg >= challenge.getObjective()){
                    isAchieve = true;
                }
            }

            if(isAchieve){
                newAchievementDetected = true;
                AchievementEntity achievementEntity = new AchievementEntity();

                achievementEntity.setIdUser(idUser);
                achievementEntity.setIdChallenge(challenge.getId());
                db.achievementDao().insertAchievement(achievementEntity);
            }
        }

        if(newAchievementDetected){
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_NEW_ACHIEVEMENT)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Conseguiste nuevos logros")
                    .setContentText("Has conseguido uno o varios logros con el último viaje")
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_CHANNEL_ID_NEW_ACHIEVEMENT,0, notification);
        }
    }
}
