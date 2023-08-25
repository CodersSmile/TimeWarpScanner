package com.timewarpscanner.funnyfacemaker.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.timewarpscanner.funnyfacemaker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.timewarpscanner.funnyfacemaker.AdsUtils.Interfaces.AppInterfaces;
import com.timewarpscanner.funnyfacemaker.AdsUtils.Utils.Constants;
import com.timewarpscanner.funnyfacemaker.R;

public class OnboardingActivity extends AppCompatActivity {

    ImageView imageView, nextbtn;
    int i = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_onboarding);
        imageView = findViewById(R.id.onBoarding_iv);
        nextbtn = findViewById(R.id.nextbtn);
        AdUtils.showNativeAd(OnboardingActivity.this, findViewById(R.id.native_ads), false);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdUtils.showInterstitialAd(OnboardingActivity.this, state_load -> {
                    if (i == 0) {
                        i = 1;
                        imageView.setImageResource(R.drawable.onboarding_two);
                        nextbtn.setImageResource(R.drawable.next);
                    } else {
                        startActivity(new Intent(getApplicationContext(), TermsOfUse.class));

                    }

                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (i == 0) {
            exitDialog();
        } else {
            i = 0;
            imageView.setImageResource(R.drawable.onboarding_one);
            nextbtn.setImageResource(R.drawable.half_next);
        }
    }

    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }


    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OnboardingActivity.this);
        builder.setTitle(getResources().getString(R.string.app_name));
//        builder.setMessage(getResources().getString(R.string.if_you_like_this_app_than_don_t_forget_to_rate_this_app_it_won_t_take_more_than_minutes));
        builder.setMessage("Are you sure to exit?");

//        builder.setIcon(getResources().getDrawable(R.drawable.logo));

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                dialog.dismiss();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNeutralButton("Rate App", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rateApp();
                dialog.dismiss();
            }
        });

        /*builder.setNegativeButtonIcon(getResources().getDrawable(R.drawable.btn_share));
        builder.setPositiveButtonIcon(getResources().getDrawable(R.drawable.btn_rate));
        builder.setNeutralButtonIcon(getResources().getDrawable(R.drawable.btn_privacy));*/

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));


    }
    @Override
    protected void onResume() {
        super.onResume();
        AdUtils.loadInitialInterstitialAds(this);
        AdUtils.loadAppOpenAds(this);
        AdUtils.loadInitialNativeList(this);
    }
}