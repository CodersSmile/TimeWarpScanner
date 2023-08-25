package com.timewarpscanner.funnyfacemaker;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;
import com.timewarpscanner.funnyfacemaker.AdsUtils.FirebaseADHandlers.AdUtils;
import com.timewarpscanner.funnyfacemaker.activities.PrivacyPolicy;
import com.timewarpscanner.funnyfacemaker.activities.SavedImagesActivity;
import com.timewarpscanner.funnyfacemaker.activities.ScanActivity;
import com.timewarpscanner.funnyfacemaker.databinding.ActivityMainBinding;
import com.timewarpscanner.funnyfacemaker.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE_STORAGE_PERMISSION = 101;
    private TextView llRateUs, llShareApp, llMoreApps, llPrivacyPolicy;
    ActivityMainBinding binding;

    private ImageView app_logo, closeDrawer;

    @Override
    protected void onResume() {
        super.onResume();
        AdUtils.loadInitialInterstitialAds(MainActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        clickListeners();
        mCheckPermission();
    }

    public boolean isLatestVersion() {
        return SDK_INT >= Build.VERSION_CODES.R;
    }

    private void mCheckPermission() {

        if (isLatestVersion()) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                permissionDialog();
            } else {
                // Permission already granted, you can proceed with reading and writing external storage
                System.out.println("Permission Already Granted");
            }
        } else {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
                permissionDialog();
            } else {
                // Permission already granted, you can proceed with reading and writing external storage
                System.out.println("Permission Already Granted");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with reading and writing external storage
//                OpenImageChooser();
                System.out.println("Permission Already Granted");
            } else {
                // Permission denied, you cannot proceed with reading and writing external storage
                Toast.makeText(this, "Storage permission required", Toast.LENGTH_SHORT).show();
//                DetailsDialog.showDetailsDialog(EditProfileActivity.this);
            }
        }
    }


    @Override
    public void onBackPressed() {
        exitDialog();
    }

    private void exitDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
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

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));


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
        flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        intent.addFlags(flags);
        return intent;
    }


    private void clickListeners() {
        binding.scanView.setOnClickListener(view -> AdUtils.showInterstitialAd(MainActivity.this, isLoaded -> startActivity(new Intent(MainActivity.this, ScanActivity.class))));
        binding.tw.setOnClickListener(view -> AdUtils.showInterstitialAd(MainActivity.this, isLoaded -> startActivity(new Intent(MainActivity.this, ScanActivity.class))));
        binding.ss.setOnClickListener(view -> AdUtils.showInterstitialAd(MainActivity.this, isLoaded -> startActivity(new Intent(MainActivity.this, ScanActivity.class))));
        binding.splitScan.setOnClickListener(view -> AdUtils.showInterstitialAd(MainActivity.this, isLoaded -> startActivity(new Intent(MainActivity.this, ScanActivity.class))));
        binding.timeWrap.setOnClickListener(view -> AdUtils.showInterstitialAd(MainActivity.this, isLoaded -> startActivity(new Intent(MainActivity.this, ScanActivity.class))));
        binding.savedFilesView.setOnClickListener(view -> AdUtils.showInterstitialAd(MainActivity.this, isLoaded -> startActivity(new Intent(MainActivity.this, SavedImagesActivity.class))));

        Glide.with(this).load(R.drawable.logotwo).circleCrop().into(binding.appLogo);

        binding.toolbar.toggleDrawer.setOnClickListener(view -> {
                    toggleDrawable();
                });
        binding.closeBtn.setOnClickListener(view -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });
        binding.rateApp.setOnClickListener(view -> {
//            showRateUsDialog();
            rateApp();
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });
        binding.addReview.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(/*"https://play.google.com/store/apps/developer?id=Peek+International"*/"market://details?id=" + getPackageName())));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });
        binding.policy.setOnClickListener(view -> {
//            Utils.openPrivacy_TWS(MainActivity.this, getString(R.string.privacy_policy_link));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            gotoUrl("https://encoremediainc.blogspot.com/p/privacy-policy.html");
//            startActivity(new Intent(getApplicationContext(), PrivacyPolicy.class));
        });
        binding.share.setOnClickListener(view -> {
            Utils.shareApp_TWS(MainActivity.this);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

    }
    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void showRateUsDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                new ContextThemeWrapper(MainActivity.this, R.style.CustomAlertDialog));

        alert.setTitle("Rate Us?");
        alert.setMessage("Are you sure you want rate our app?");
        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()))));
        alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));

        alertDialog.show();
    }

    public void toggleDrawable() {
        try {
            if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);

            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START);

            }

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void askPermissions() {

        if (isLatestVersion()) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
//                permissionDialog();
            } else {
                // Permission already granted, you can proceed with reading and writing external storage
                System.out.println("Permission Already Granted");
            }
        } else {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
//                permissionDialog();
            } else {
                // Permission already granted, you can proceed with reading and writing external storage
                System.out.println("Permission Already Granted");
            }
        }
    }

    private void permissionDialog() {
        Dialog dialogOnBackPressed = new Dialog(MainActivity.this);
        dialogOnBackPressed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnBackPressed.setCancelable(true);
        dialogOnBackPressed.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialogOnBackPressed.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        dialogOnBackPressed.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialogOnBackPressed.setContentView(R.layout.dialog_permission);
        TextView textViewDeny, textViewAllow;
        textViewDeny = dialogOnBackPressed.findViewById(R.id.textViewDeny);
        textViewAllow = dialogOnBackPressed.findViewById(R.id.textViewAllow);
        LinearGradient shader = new /*LinearGradient(0, 0, 0, 20,
                new int[]{Color.BLUE, Color.CYAN},
                new float[]{0, 1}, Shader.TileMode.CLAMP);*/
                LinearGradient(0f, 0f, 0f, textViewDeny.getTextSize(), Color.parseColor("#1D6CE2"), Color.parseColor("#FE55E3"), Shader.TileMode.CLAMP);
        textViewDeny.getPaint().setShader(shader);
        textViewDeny.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
        });

        textViewAllow.setOnClickListener(view -> {
            askPermissions();
            dialogOnBackPressed.dismiss();
        });

        dialogOnBackPressed.show();
    }
}