package com.timewarpscanner.funnyfacemaker.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.ActionMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.timewarpscanner.funnyfacemaker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.timewarpscanner.funnyfacemaker.AdsUtils.Utils.Constants;
import com.timewarpscanner.funnyfacemaker.R;
import com.timewarpscanner.funnyfacemaker.adapters.SavedAdapter;
import com.timewarpscanner.funnyfacemaker.databinding.ActivitySavedImagesBinding;
import com.timewarpscanner.funnyfacemaker.model.SavedModel;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SavedImagesActivity extends AppCompatActivity {

    ActivitySavedImagesBinding binding;
    private final List<SavedModel> mList = new ArrayList<>();
    SavedAdapter savedAdapter;
    Handler handler = new Handler();
    public static ActionMode actionModeTWS;
    private final double divideBy = 1024;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        binding = ActivitySavedImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initItems();
        clickListeners();

/*        LinearLayout adContainer = findViewById(R.id.native_ads);
        AdView adView = new AdView(this);
        AdSize customAdSize2 = new AdSize(AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT);
        adView.setAdSize(customAdSize2);
        adContainer.addView(adView);
        adView.setAdUnitId("ca-app-pub-3940256099942544/2014213617");
        Bundle extras = new Bundle();
        extras.putString("collapsible", "top");
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        adView.loadAd(adRequest);*/

        AdUtils.showNativeAd(SavedImagesActivity.this,findViewById(R.id.native_ads), false);

    }


    private void initItems() {
        try {
            File directory = new File(SavedImagesActivity.this.getExternalFilesDir("Photos"), getString(R.string.app_name));
            File path = directory.getAbsoluteFile();
            startFileScan(path);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void clickListeners() {
        binding.back.setOnClickListener(view -> onBackPressed());
    }


    @SuppressLint("NotifyDataSetChanged")
    private void startFileScan(File path) {
        try {
            if (!path.exists()) {
                return;
            }

            new Thread(() -> {
                File[] imageFiles = path.listFiles();
                mList.clear();

                if (imageFiles != null && imageFiles.length > 0) {
                    List<File> statusFiles = Arrays.asList(imageFiles);

                    try {
                        Collections.sort(statusFiles, new Comparator<File>() {
                            public int compare(File f1, File f2) {
                                try {
                                    return String.valueOf(f2.lastModified()).compareTo(String.valueOf(f1.lastModified()));
                                } catch (Exception e) {
                                    return 1;
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (File mFile : statusFiles) {
                        double l2 = mFile.length() / divideBy;
                        double l3 = l2 / divideBy;
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        double fileSize = Double.parseDouble((decimalFormat.format(l3)));
                        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        SavedModel status = new SavedModel(mFile, mFile.getAbsolutePath(), String.valueOf(fileSize), date);

                        mList.add(status);

                    }

                    handler.post(() -> {
                        GridLayoutManager layoutManager = new GridLayoutManager(SavedImagesActivity.this, 2);
                        binding.rvSave.setLayoutManager(layoutManager);
                        savedAdapter = new SavedAdapter(mList, SavedImagesActivity.this);
                        binding.rvSave.setAdapter(savedAdapter);
                        savedAdapter.notifyDataSetChanged();
                    });
                }
            }).start();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        AdUtils.showInterstitialAd(SavedImagesActivity.this, isLoaded -> super.onBackPressed());
    }
    @Override
    protected void onResume() {
        super.onResume();
        AdUtils.loadInitialInterstitialAds(this);
        AdUtils.loadAppOpenAds(this);
        AdUtils.loadInitialNativeList(this);
    }
}