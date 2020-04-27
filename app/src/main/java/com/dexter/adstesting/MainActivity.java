package com.dexter.adstesting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.NativeAdLayout;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import static com.dexter.adstesting.LoadAds.FACEBOOK_AD;
import static com.dexter.adstesting.LoadAds.FACEBOOK_FIRST;
import static com.dexter.adstesting.LoadAds.GOOGLE_AD;
import static com.dexter.adstesting.LoadAds.GOOGLE_FIRST;
import static com.dexter.adstesting.LoadAds.NO_AD;

public class MainActivity extends AppCompatActivity {


   LoadAds loadAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdSettings.setTestMode(true);
        AudienceNetworkAds.initialize(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadAds = new LoadAds(this, getString(R.string.google_app_id));

    }


    public void loadInterstitialAds(View view){

        loadAds.loadBannerAds(findViewById(R.id.google_banner), findViewById(R.id.fb_banner), getString(R.string.fb_banner), AdSize.BANNER_HEIGHT_90, GOOGLE_FIRST, NO_AD);

    }

    public void showFacebookNativeBanner(View view){
        loadAds.loadNativeBannerAds(findViewById(R.id.google_banner), findViewById(R.id.fb_native_ad_container), getString(R.string.fb_native_banner), NO_AD);
    }


    public void showInterstitial(View v){
        loadAds.loadBannerAds(findViewById(R.id.google_banner), findViewById(R.id.fb_banner), getString(R.string.fb_banner), AdSize.BANNER_HEIGHT_90, FACEBOOK_FIRST, NO_AD);
    }

    public void showGoogleNative(View v){
        loadAds.loadNativeAds(findViewById(R.id.fl_adplaceholder), findViewById(R.id.fb_native_ad_container), getString(R.string.google_native_ad), getString(R.string.fb_native_ad), GOOGLE_FIRST, NO_AD);

    }

}
