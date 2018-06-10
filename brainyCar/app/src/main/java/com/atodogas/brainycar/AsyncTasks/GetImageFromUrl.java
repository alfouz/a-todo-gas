package com.atodogas.brainycar.AsyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.atodogas.brainycar.DTOs.AchievementDTO;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
    CallbackInterface callback;

    public GetImageFromUrl(CallbackInterface callback) {
        this.callback = callback;
    }

    @Override
    protected Bitmap doInBackground(String... strs) {
        String imageUrl = strs[0];

        try {

            URL myFileUrl = new URL (imageUrl);
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            return BitmapFactory.decodeStream(is);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        callback.doCallback(bitmap);
    }
}
