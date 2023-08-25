package com.timewarpscanner.funnyfacemaker.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.timewarpscanner.funnyfacemaker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.timewarpscanner.funnyfacemaker.AdsUtils.Interfaces.AppInterfaces;
import com.timewarpscanner.funnyfacemaker.AdsUtils.Utils.Constants;
import com.timewarpscanner.funnyfacemaker.MainActivity;
import com.timewarpscanner.funnyfacemaker.R;

public class PrivacyPolicy extends AppCompatActivity {

    TextView title, agreebtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_privacy_policy);
        title = findViewById(R.id.title);
        agreebtn = findViewById(R.id.agreebtn);
        AdUtils.showNativeAd(PrivacyPolicy.this, findViewById(R.id.native_ads), false);
        LinearGradient shader = new /*LinearGradient(0, 0, 0, 20,
                new int[]{Color.BLUE, Color.CYAN},
                new float[]{0, 1}, Shader.TileMode.CLAMP);*/
                LinearGradient(0f, 0f, 0f, title.getTextSize(), Color.parseColor("#1D6CE2"), Color.parseColor("#FE55E3"), Shader.TileMode.CLAMP);
        title.getPaint().setShader(shader);


        agreebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdUtils.showInterstitialAd(PrivacyPolicy.this, state_load -> {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                });
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        AdUtils.loadInitialInterstitialAds(this);
        AdUtils.loadAppOpenAds(this);
        AdUtils.loadInitialNativeList(this);
    }
}