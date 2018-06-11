package com.atodogas.brainycar;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;

import android.content.Intent;
import android.widget.TextView;

import com.atodogas.brainycar.AsyncTasks.CallbackInterface;
import com.atodogas.brainycar.AsyncTasks.GetAllAchievementsBD;
import com.atodogas.brainycar.AsyncTasks.GetImageFromUrl;
import com.atodogas.brainycar.DTOs.AchievementDTO;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private View root;
    private ArrayList<AchievementDTO> achievements;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_profile, container, false);

        Resources res = getResources();

        TabHost tabs= root.findViewById(R.id.tabHostProfile);
        tabs.setup();

        //Tab 1
        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.Resumen);
        spec.setIndicator("Resumen",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        //Tab 2
        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.Logros);
        spec.setIndicator("Logros",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        summaryTab();

        // Inflate the layout for this fragment
        return root;
    }

    private void summaryTab(){

        final TextView numberAchievementsText = root.findViewById(R.id.numberAchievementsText);
        final TextView goldTextView = root.findViewById(R.id.goldTextView);
        final TextView silverTextView = root.findViewById(R.id.silverTextView);
        final TextView bronzeTextView = root.findViewById(R.id.bronzeTextView);


        // Nombre de perfil obtenido de la cuenta del usuario
        String name = getActivity().getIntent().getStringExtra("personName");
        TextView accountName = root.findViewById(R.id.accountName);
        accountName.setText(name);

        new GetAllAchievementsBD(new CallbackInterface<List<AchievementDTO>>() {
            @Override
            public void doCallback(List<AchievementDTO> achievementsDTO) {
                achievements = (ArrayList<AchievementDTO>) achievementsDTO;

                int count = 0;
                int goldCount = 0;
                int silverCount = 0;
                int bronzeCount = 0;
                for(AchievementDTO achievementDTO : achievementsDTO){
                    if(achievementDTO.getAchieve()){
                        count++;

                        if(achievementDTO.getLevel() == 1){
                            goldCount++;
                        }
                        else if(achievementDTO.getLevel() == 2){
                            silverCount++;
                        }
                        else if(achievementDTO.getLevel() == 3){
                            bronzeCount++;
                        }
                    }
                }

                numberAchievementsText.setText(count + " de " + achievements.size());


                goldTextView.setText(Integer.toString(goldCount));
                silverTextView.setText(Integer.toString(silverCount));
                bronzeTextView.setText(Integer.toString(bronzeCount));

                achievementsTab();
            }
        }, getActivity().getApplicationContext()).execute(getActivity().getIntent().getIntExtra("idUser", - 1));

        final ImageView accountProfileImage = root.findViewById(R.id.accountProfileImage);
        new GetImageFromUrl(new CallbackInterface<Bitmap>() {
            @Override
            public void doCallback(Bitmap image) {
                if(image != null) {
                    Bitmap output = Bitmap.createBitmap(image.getWidth(), image
                            .getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(output);

                    final int color = 0xff424242;
                    final Paint paint = new Paint();
                    final Rect rect = new Rect(0, 0, image.getWidth(), image.getHeight());
                    final RectF rectF = new RectF(rect);
                    final float roundPx = 250;

                    paint.setAntiAlias(true);
                    canvas.drawARGB(0, 0, 0, 0);
                    paint.setColor(color);
                    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    canvas.drawBitmap(image, rect, rect, paint);

                    accountProfileImage.setImageBitmap(output);
                }
            }
        }).execute(getActivity().getIntent().getStringExtra("personPhotoUrl"));
    }

    private void achievementsTab(){
        ProfileFragmentAchievementAdapter adapter = new ProfileFragmentAchievementAdapter(achievements, new ProfileFragmentAchievementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AchievementDTO item) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                getView().performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Logro: " + item.getTitle());
                startActivity(sharingIntent);
            }
        });

        RecyclerView myView =  root.findViewById(R.id.logrosLayout);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);
    }
}
