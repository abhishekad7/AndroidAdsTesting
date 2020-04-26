package com.dexter.adstesting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.facebook.ads.AdSize;
import com.facebook.ads.NativeAdLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import static com.dexter.adstesting.LoadAds.GOOGLE_AD;
import static com.dexter.adstesting.LoadAds.FACEBOOK_AD;

public class GoogleAds {

    final  String TAG = "LOADADS";

    Context mContext;
    String appId;

    InterstitialAd googleInterstitialAd;
    private UnifiedNativeAd googleNativeAd;

    public GoogleAds(Context context, String appId){

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        this.appId = appId;
        this.mContext = context;
    }



    public void loadGoogleBannerAd(final View adView, final int onError, final View facebookAdViewContainer, final String facebookBannerId, final AdSize adSize){

        final AdView bannerGoogleAdView = (AdView) adView;
        bannerGoogleAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if(bannerGoogleAdView != null) {
                    bannerGoogleAdView.setVisibility(View.VISIBLE);
                }

                if(facebookAdViewContainer != null){
                    facebookAdViewContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if(bannerGoogleAdView != null) {
                    bannerGoogleAdView.setVisibility(View.GONE);
                }
                if(onError == FACEBOOK_AD){
                    new FacebookAds(mContext, appId).loadFacebookBannerAd(facebookAdViewContainer, facebookBannerId, adSize);
                }

                if(onError == GOOGLE_AD){
                    loadGoogleBannerAd(adView);
                }
            }

            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdClosed() {

            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        bannerGoogleAdView.loadAd(adRequest);

    }

    public void loadGoogleBannerAd(final View adView){

        final AdView bannerGoogleAdView = (AdView) adView;
        bannerGoogleAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if(bannerGoogleAdView != null) {
                    bannerGoogleAdView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if(bannerGoogleAdView != null) {
                    bannerGoogleAdView.setVisibility(View.GONE);
                }
            }


            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdClosed() {

            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        bannerGoogleAdView.loadAd(adRequest);

    }


    public boolean isGoogleInterstitialAdLoaded(){
        if(googleInterstitialAd != null && googleInterstitialAd.isLoaded()){
            return true;
        }

        return false;
    }

    public boolean showGoogleInterstialAd(){
        if(isGoogleInterstitialAdLoaded()){
            googleInterstitialAd.show();
            return true;
        }

        return false;
    }

    public void loadGoogleInterstitialAd(final String googleInterstitialId, final boolean showOnLoad, final boolean showFacebookAdAlso, final FacebookAds facebookAds){


        googleInterstitialAd = new com.google.android.gms.ads.InterstitialAd(mContext);
        googleInterstitialAd.setAdUnitId(googleInterstitialId);

        googleInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

                if(showOnLoad && googleInterstitialId != null && googleInterstitialAd.isLoaded()){
                    googleInterstitialAd.show();
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdClosed() {
                if(showFacebookAdAlso && facebookAds != null){
                    facebookAds.showFacbookInterstialAd();
                }
            }
        });


        googleInterstitialAd.loadAd(new AdRequest.Builder().build());

    }



    public void loadGoogleNativeAd(final View googleNativeAdPlaceholder, final String googleNativeAdId) {

        AdLoader.Builder builder = new AdLoader.Builder(mContext, googleNativeAdId);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {


                if(googleNativeAdPlaceholder != null) {
                    googleNativeAdPlaceholder.setVisibility(View.VISIBLE);

                    if (googleNativeAd != null) {
                        googleNativeAd.destroy();
                    }
                    googleNativeAd = unifiedNativeAd;
                    FrameLayout frameLayout = (FrameLayout) googleNativeAdPlaceholder;
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    UnifiedNativeAdView adView = (UnifiedNativeAdView) inflater.inflate(R.layout.google_native_ad_unified, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                if(googleNativeAdPlaceholder != null){
                    googleNativeAdPlaceholder.setVisibility(View.GONE);
                }

            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }



    public void loadGoogleNativeAd(final View googleNativeAdPlaceholder, final String googleNativeAdId, final int onError, final View facebookNativeAdLayout, final String facebookNativeAdId) {


        AdLoader.Builder builder = new AdLoader.Builder(mContext, googleNativeAdId);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {


                if(facebookNativeAdLayout != null){
                    facebookNativeAdLayout.setVisibility(View.GONE);
                }

                if(googleNativeAdPlaceholder != null) {
                    googleNativeAdPlaceholder.setVisibility(View.VISIBLE);

                    if (googleNativeAd != null) {
                        googleNativeAd.destroy();
                    }
                    googleNativeAd = unifiedNativeAd;
                    FrameLayout frameLayout = (FrameLayout) googleNativeAdPlaceholder;
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    UnifiedNativeAdView adView = (UnifiedNativeAdView) inflater.inflate(R.layout.google_native_ad_unified, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                if(googleNativeAdPlaceholder != null){
                    googleNativeAdPlaceholder.setVisibility(View.GONE);
                }

                if(onError == FACEBOOK_AD) {
                    new FacebookAds(mContext, appId).loadFacebookNativeAd(facebookNativeAdLayout, facebookNativeAdId);
                }

                if(onError == GOOGLE_AD){
                    loadGoogleNativeAd(googleNativeAdPlaceholder, googleNativeAdId);
                }

            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }


    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        com.google.android.gms.ads.formats.MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

        VideoController vc = nativeAd.getVideoController();

        if (vc.hasVideoContent()) {

            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        } else {

        }
    }


}
