package com.timewarpscanner.funnyfacemaker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.timewarpscanner.funnyfacemaker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.timewarpscanner.funnyfacemaker.AdsUtils.FirebaseADHandlers.AdsJsonPOJO;
import com.timewarpscanner.funnyfacemaker.AdsUtils.FirebaseADHandlers.FirebaseUtils;
import com.timewarpscanner.funnyfacemaker.AdsUtils.Interfaces.AppInterfaces;
import com.timewarpscanner.funnyfacemaker.AdsUtils.PreferencesManager.AppPreferences;
import com.timewarpscanner.funnyfacemaker.AdsUtils.Utils.Constants;
import com.timewarpscanner.funnyfacemaker.AdsUtils.Utils.Global;
import com.timewarpscanner.funnyfacemaker.AdsUtils.Utils.StringUtils;
import com.timewarpscanner.funnyfacemaker.MainActivity;
import com.timewarpscanner.funnyfacemaker.R;

public class SplashActivity extends AppCompatActivity {

    Activity splashActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash);
        splashActivity = SplashActivity.this;
        AppPreferences appPreferences = new AppPreferences(this);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.ADSJSON);
        Constants.adsJsonPOJO = Global.getAdsData(appPreferences.getAdsModel());

        if (Constants.adsJsonPOJO != null && !Constants.isNull(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getValue())) {
            Constants.adsJsonPOJO = Global.getAdsData(appPreferences.getAdsModel());
            Constants.hitCounter = Integer.parseInt(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getHits());
//            Constants.adsJsonPOJO.getParameters().getShowAd().getDefaultValue().setValue("false");

            nextActivity();
        } else {
            FirebaseUtils.initiateAndStoreFirebaseRemoteConfig(this, adsJsonPOJO -> {
                appPreferences.setAdsModel(adsJsonPOJO);
                Constants.adsJsonPOJO = adsJsonPOJO;
                Constants.hitCounter = Integer.parseInt(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getHits());
//                Constants.adsJsonPOJO.getParameters().getShowAd().getDefaultValue().setValue("false");

                nextActivity();

            });
        }


    }


    private void nextActivity() {
        AdUtils.showAdIfAvailable(this, state_load -> {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), OnboardingActivity.class));
                finish();

            }, 1600);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AdUtils.loadInitialInterstitialAds(this);
        AdUtils.loadAppOpenAds(this);
        AdUtils.loadInitialNativeList(this);

    }
   /* private void nextActivity() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {

            Boolean isFirstRun = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE).getBoolean("isFirstRun", true);
            if (isFirstRun) {

                Intent intent = new Intent(SplashActivity.this, OnboardingActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();
                                *//*startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                                finish();*//*

        }, 2000);
    }*/
}