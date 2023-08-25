package com.timewarpscanner.funnyfacemaker.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.timewarpscanner.funnyfacemaker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.timewarpscanner.funnyfacemaker.AdsUtils.Utils.Constants;
import com.timewarpscanner.funnyfacemaker.R;
import com.timewarpscanner.funnyfacemaker.databinding.ActivityImageOpenBinding;

public class ImageOpenActivity extends AppCompatActivity {

    ActivityImageOpenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        binding = ActivityImageOpenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AdUtils.showNativeAd(ImageOpenActivity.this, binding.nativeAds, false);

        initItems();
        clickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdUtils.loadInitialInterstitialAds(this);
        AdUtils.loadAppOpenAds(this);
        AdUtils.loadInitialNativeList(this);
    }

    private void initItems() {
        binding.ivIcon.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra("STR_IMAGE")));
    }

    private void clickListeners() {
        binding.back.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        AdUtils.showInterstitialAd(ImageOpenActivity.this, state_load -> {
            ImageOpenActivity.super.onBackPressed();
        });
    }


}