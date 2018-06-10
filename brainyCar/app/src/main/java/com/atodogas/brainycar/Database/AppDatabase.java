package com.atodogas.brainycar.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.atodogas.brainycar.Database.DAOs.AchievementDao;
import com.atodogas.brainycar.Database.DAOs.CarDao;
import com.atodogas.brainycar.Database.DAOs.ChallengeDao;
import com.atodogas.brainycar.Database.DAOs.TripDao;
import com.atodogas.brainycar.Database.DAOs.TripDataDao;
import com.atodogas.brainycar.Database.DAOs.UserDao;
import com.atodogas.brainycar.Database.Entities.*;

import java.util.concurrent.Executors;

@Database(entities = {UserEntity.class, CarEntity.class, TripEntity.class, TripDataEntity.class, ChallengeEntity.class, AchievementEntity.class }, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract CarDao carDao();
    public abstract TripDao tripDao();
    public abstract TripDataDao tripDataDao();
    public abstract ChallengeDao challengeDao();
    public abstract AchievementDao achievementDao();

    public synchronized static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "brainyCar")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase database) {
                        super.onCreate(database);
                        final SupportSQLiteDatabase db = database;
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                insertInitialData(db);
                            }
                        });
                    }
                })
                .addMigrations(MIGRATION_1_2)
                .build();
    }

    public static final Migration MIGRATION_1_2 =
        new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("CREATE TABLE IF NOT EXISTS Challenges (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "Title TEXT NOT NULL," +
                        "Description TEXT NOT NULL," +
                        "Level INTEGER NOT NULL," +
                        "Objective REAL NOT NULL," +
                        "Operator TEXT NOT NULL," +
                        "Variable TEXT NOT NULL);");

                database.execSQL("CREATE TABLE IF NOT EXISTS Achievements (" +
                        "idUser INTEGER NOT NULL," +
                        "idChallenge INTEGER NOT NULL," +
                        "FOREIGN KEY(idUser) REFERENCES Users(id) ON UPDATE CASCADE ON DELETE CASCADE," +
                        "FOREIGN KEY(idChallenge) REFERENCES Challenges(id) ON UPDATE CASCADE ON DELETE CASCADE," +
                        "PRIMARY KEY(idUser, idChallenge));");

                insertInitialData(database);
            }
        };

    private static void insertInitialData(SupportSQLiteDatabase database){
        //Logros de distancia
        database.execSQL("INSERT INTO Challenges (Title, Description, Level, Objective, Operator, Variable) VALUES ('Hacer un viaje de al menos 10 km', 'Haz un viaje de 10 km de distancia o mas', 3, 10, '>=', 'kms')");
        database.execSQL("INSERT INTO Challenges (Title, Description, Level, Objective, Operator, Variable) VALUES ('Hacer un viaje de al menos 20 km', 'Haz un viaje de 20 km de distancia o mas', 2, 20, '>=', 'kms')");
        database.execSQL("INSERT INTO Challenges (Title, Description, Level, Objective, Operator, Variable) VALUES ('Hacer un viaje de al menos 50 km', 'Haz un viaje de 50 km de distancia o mas', 1, 50, '>=', 'kms')");

        //Logros de eficiencia
        database.execSQL("INSERT INTO Challenges (Title, Description, Level, Objective, Operator, Variable) VALUES ('Hacer un viaje a bajas revoluciones: Menos de 3000 RPM', 'Conducir a altas revoluciones implica un mayor consumo de combustible. Intenta mejorarlo!', 3, 3000, '<=', 'rpm')");
        database.execSQL("INSERT INTO Challenges (Title, Description, Level, Objective, Operator, Variable) VALUES ('Hacer un viaje a bajas revoluciones: Menos de 2800 RPM', 'Conducir a bajas revoluciones ayuda a consumir menos combustible y ser mas eficientes', 2, 2800, '<=', 'rpm')");
        database.execSQL("INSERT INTO Challenges (Title, Description, Level, Objective, Operator, Variable) VALUES ('Hacer un viaje a bajas revoluciones: Menos de 2500 RPM', 'Conducir a 2500 RPM permitira obtener un consumo optimo de combustible', 1, 2500, '<=', 'rpm')");

        database.execSQL("INSERT INTO Challenges (Title, Description, Level, Objective, Operator, Variable) VALUES ('Hacer un viaje con una velocidad media de 15 km/h', 'Cuanto mas baja sea la velocidad media alcanzada el consumo de combustible sera mas elevado. Intenta mejorarlo', 3, 15, '>=', 'speedAVG')");
        database.execSQL("INSERT INTO Challenges (Title, Description, Level, Objective, Operator, Variable) VALUES ('Hacer un viaje con una velocidad media de 30 km/h', 'Una velocidad media mas alta permitira conseguir un consumo de combustible mas eficiente', 2, 30, '>=', 'speedAVG')");
        database.execSQL("INSERT INTO Challenges (Title, Description, Level, Objective, Operator, Variable) VALUES ('Hacer un viaje con una velocidad media de 60 km/h', 'A 60 km/h el consumo de combustible sera mas bajo si lo combinas con uno de los logros relacionado con las RPM', 1, 60, '>=', 'speedAVG')");
    }
}