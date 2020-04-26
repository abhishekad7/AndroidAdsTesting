package com.dexter.adstesting;

import android.content.Context;
import android.view.View;

import com.facebook.ads.AdSize;
import com.facebook.ads.NativeAdLayout;

public class LoadAds {

    public static final int GOOGLE_FIRST = 1, FACEBOOK_FIRST = 2;
    public static final int NO_AD = 0, GOOGLE_AD = 1, FACEBOOK_AD = 2;

    Context mContext;
    String googleAppId;

    FacebookAds facebookAds;
    GoogleAds googleAds;


    public LoadAds(Context context, String googleAppId){
        this.mContext = context;
        this.googleAppId = googleAppId;

        facebookAds = new FacebookAds(context, googleAppId);
        googleAds = new GoogleAds(context, googleAppId);
    }

    public void loadBannerAds(View googleView, View facebookView, String facebookBannerId, AdSize facebookAdSize, int firstTry, int onError){
        if(firstTry == GOOGLE_FIRST){
            if(onError == NO_AD){
                googleAds.loadGoogleBannerAd(googleView);
            } else {
                googleAds.loadGoogleBannerAd(googleView, onError, facebookView, facebookBannerId, facebookAdSize);
            }
        } else if(firstTry == FACEBOOK_FIRST){
            if(onError == NO_AD){
              facebookAds.loadFacebookBannerAd(facebookView, facebookBannerId, facebookAdSize);
            } else{
                facebookAds.loadFacebookBannerAd(facebookView, facebookBannerId, facebookAdSize, onError, googleView);
            }
        }
    }

    public void loadNativeBannerAds(View googleView, View facebookView, String facebookNativeBannerId, int onError){

        if(onError == NO_AD){
            facebookAds.loadFacebookNativeBannerAd(facebookView, facebookNativeBannerId);
        } else{
            facebookAds.loadFacebookNativeBannerAd(facebookView, facebookNativeBannerId, onError, googleView);
        }
    }

    public void loadNativeAds(View googleView, View facebookView, String googleNativeAdId, String facebookNativeAdId, int firstTry, int onError){
        if(firstTry == GOOGLE_FIRST){
            if(onError == NO_AD){
                googleAds.loadGoogleNativeAd(googleView, googleNativeAdId);
            } else {
                googleAds.loadGoogleNativeAd(googleView, googleNativeAdId, onError, facebookView, facebookNativeAdId);
            }
        } else if(firstTry == FACEBOOK_FIRST){
            if(onError == NO_AD){
                facebookAds.loadFacebookNativeAd(facebookView, facebookNativeAdId);
            } else{
                facebookAds.loadFacebookNativeAd(facebookView, facebookNativeAdId, onError, googleView, googleNativeAdId);
            }
        }
    }

    public void loadInterstitialAds(String googleInterstitialId, boolean googleShowOnLoad, String facebookInterstitialId, boolean facebookShowOnLoad, boolean showBothAds){
        googleAds.loadGoogleInterstitialAd(googleInterstitialId, googleShowOnLoad, showBothAds, facebookAds);
        facebookAds.loadFacebookInterstitialAd(facebookInterstitialId, facebookShowOnLoad, showBothAds, googleAds);
    }

    public boolean showInterstitialAds(int firstTry){
        if(firstTry == FACEBOOK_FIRST) {
            if (!facebookAds.showFacbookInterstialAd()) {
                if(googleAds.showGoogleInterstialAd()){
                    return true;
                }
                return false;

            } else {
                return true;
            }
        } else {
            if (!googleAds.showGoogleInterstialAd()) {
                if(facebookAds.showFacbookInterstialAd()){
                    return true;
                }
                return false;

            } else {
                return true;
            }
        }

    }




}
