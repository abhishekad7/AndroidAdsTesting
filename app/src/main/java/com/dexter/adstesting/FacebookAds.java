package com.dexter.adstesting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;

import java.util.ArrayList;
import java.util.List;

import static com.dexter.adstesting.LoadAds.FACEBOOK_AD;
import static com.dexter.adstesting.LoadAds.GOOGLE_AD;

public class FacebookAds {

    final String TAG = "LOADADS";

    String googleAppId;

    Context mContext;

    InterstitialAd facebookInterstitialAd;
    private NativeAd facebookNativeAd;
    private NativeBannerAd facebookNativeBannerAd;

    boolean canRequestBannerAd = true, canRequestNativeBannerAd = true;
    boolean canRequestNativeAd = true;
    boolean canRequestInterstitialAd = true;


    public FacebookAds(Context context, String googleAppId){
        AudienceNetworkAds.initialize(context);
        this.mContext = context;
        this.googleAppId = googleAppId;

        canRequestBannerAd = true;
        canRequestNativeBannerAd = true;
        canRequestNativeAd = true;
        canRequestInterstitialAd = true;

    }

    public void loadFacebookBannerAd(final View adViewContainer, final String facebookBannerId, final AdSize adSize, final int onError, final View googleAdView){

        if(!canRequestBannerAd){
            return;
        }

        canRequestBannerAd = false;

        AdView bannerFbAdView = new com.facebook.ads.AdView(mContext, facebookBannerId, adSize);

        final LinearLayout adContainer = (LinearLayout) adViewContainer;
        adContainer.removeAllViews();
        adContainer.addView(bannerFbAdView);

        bannerFbAdView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

                canRequestBannerAd = true;

                if(adContainer != null) {
                    adContainer.setVisibility(View.GONE);
                }

                if(onError == GOOGLE_AD){
                    new GoogleAds(mContext, googleAppId).loadGoogleBannerAd(googleAdView);
                }

                if(onError == FACEBOOK_AD){
                    loadFacebookBannerAd(adViewContainer, facebookBannerId, adSize);
                }

            }

            @Override
            public void onAdLoaded(Ad ad) {

                if(adContainer != null) {
                    adContainer.setVisibility(View.VISIBLE);
                }

                if(googleAdView != null){
                    googleAdView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                canRequestBannerAd = true;
            }
        });

        bannerFbAdView.loadAd();
    }

    public void loadFacebookBannerAd(View adViewContainer, String facebookBannerId, AdSize adSize){

        if(!canRequestBannerAd){
            return;
        }

        canRequestBannerAd = false;


        AdView bannerFbAdView = new com.facebook.ads.AdView(mContext, facebookBannerId, adSize);

        final LinearLayout adContainer = (LinearLayout) adViewContainer;
        adContainer.removeAllViews();
        adContainer.addView(bannerFbAdView);

        bannerFbAdView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

                canRequestBannerAd = true;

                if(adContainer != null) {
                    adContainer.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAdLoaded(Ad ad) {
                if(adContainer != null) {
                    adContainer.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                canRequestBannerAd = true;
            }
        });

        bannerFbAdView.loadAd();
    }


    public void loadFacebookNativeBannerAd(final View nativeBannerAdLayout, String facebookNativeBannerAdId) {

        if(!canRequestNativeBannerAd){
            return;
        }

        canRequestNativeBannerAd = false;

        facebookNativeBannerAd = new NativeBannerAd(mContext, facebookNativeBannerAdId);

        facebookNativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

                canRequestNativeBannerAd = true;

            }

            @Override
            public void onAdLoaded(Ad ad) {


                if (facebookNativeBannerAd == null || facebookNativeBannerAd != ad) {
                    return;
                }

                inflateNativeBannerAd((NativeAdLayout) nativeBannerAdLayout, facebookNativeBannerAd);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                //Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                canRequestNativeBannerAd = true;
            }
        });
        facebookNativeBannerAd.loadAd();


    }

    public void loadFacebookNativeBannerAd(final View nativeBannerAdLayout, final String facebookNativeBannerAdId, final int onError, final View googleAdView ) {

        if(!canRequestNativeBannerAd){
            return;
        }

        canRequestNativeBannerAd = false;

        facebookNativeBannerAd = new NativeBannerAd(mContext, facebookNativeBannerAdId);

        facebookNativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

                canRequestNativeBannerAd = true;

                if(nativeBannerAdLayout != null){
                    nativeBannerAdLayout.setVisibility(View.GONE);
                }

                if(onError == GOOGLE_AD){
                    new GoogleAds(mContext, googleAppId).loadGoogleBannerAd(googleAdView);
                }

                if(onError == FACEBOOK_AD){
                    loadFacebookNativeBannerAd(nativeBannerAdLayout, facebookNativeBannerAdId);
                }


            }

            @Override
            public void onAdLoaded(Ad ad) {



                if (facebookNativeBannerAd == null || facebookNativeBannerAd != ad) {
                    return;
                }

                if(nativeBannerAdLayout != null){
                    nativeBannerAdLayout.setVisibility(View.VISIBLE);
                }

                if(googleAdView != null){
                    googleAdView.setVisibility(View.GONE);
                }

                inflateNativeBannerAd((NativeAdLayout) nativeBannerAdLayout, facebookNativeBannerAd);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                //Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                canRequestNativeBannerAd = true;
            }
        });
        facebookNativeBannerAd.loadAd();


    }

    private void inflateNativeBannerAd(NativeAdLayout nativeBannerAdLayout, NativeBannerAd nativeBannerAd) {
        // Unregister last ad
        try {
            nativeBannerAd.unregisterView();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            // Inflate the Ad view.  The layout referenced is the one you created in the last step.
            LinearLayout fbadView = (LinearLayout) inflater.inflate(R.layout.fb_native_banner_ad_layout, nativeBannerAdLayout, false);
            nativeBannerAdLayout.addView(fbadView);

            // Add the AdChoices icon
            RelativeLayout adChoicesContainer = fbadView.findViewById(R.id.ad_choices_container);
            AdOptionsView adOptionsView = new AdOptionsView(mContext, nativeBannerAd, nativeBannerAdLayout);
            adChoicesContainer.removeAllViews();
            adChoicesContainer.addView(adOptionsView, 0);

            // Create native UI using the ad metadata.
            TextView nativeAdTitle = fbadView.findViewById(R.id.native_ad_title);
            TextView nativeAdSocialContext = fbadView.findViewById(R.id.native_ad_social_context);
            TextView sponsoredLabel = fbadView.findViewById(R.id.native_ad_sponsored_label);
            AdIconView nativeAdIconView = fbadView.findViewById(R.id.native_icon_view);
            Button nativeAdCallToAction = fbadView.findViewById(R.id.native_ad_call_to_action);

            // Set the Text.
            nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
            nativeAdCallToAction.setVisibility(
                    nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
            nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
            sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

            // Register the Title and CTA button to listen for clicks.
            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);
            nativeBannerAd.registerViewForInteraction(fbadView, nativeAdIconView, clickableViews);

        } catch (Exception e){
            e.printStackTrace();
        }
    }



    public boolean isFacebookInterstitialAdLoaded(){
        if(facebookInterstitialAd != null && facebookInterstitialAd.isAdLoaded()){
            return true;
        }

        return false;
    }

    public boolean showFacbookInterstialAd(){
        if(isFacebookInterstitialAdLoaded()){
            facebookInterstitialAd.show();
            return true;
        }
        return false;
    }


    public void loadFacebookInterstitialAd(String facebookInterstitialId, boolean showOnLoad, final boolean showGoogleAdAlso, final GoogleAds googleAds){

        if(!canRequestInterstitialAd){
            return;
        }

        canRequestInterstitialAd = false;

        facebookInterstitialAd = new InterstitialAd(mContext, facebookInterstitialId);
        facebookInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

                canRequestInterstitialAd = true;

                if(showGoogleAdAlso && googleAds != null){
                    googleAds.showGoogleInterstialAd();
                }
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                canRequestInterstitialAd = true;

            }

            @Override
            public void onAdLoaded(Ad ad) {


            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

                canRequestInterstitialAd = true;
            }
        });

        facebookInterstitialAd.loadAd();
    }


    public void loadFacebookNativeAd(final View facebookNativeAdLayout, final String facebookNativeAdId, final int onError, final View googleNativeAdPlaceholder, final String googleNativeAdId) {


        if(!canRequestNativeAd){
            return;
        }

        canRequestNativeAd = false;

        facebookNativeAd = new NativeAd(mContext, facebookNativeAdId);

        facebookNativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

                canRequestNativeAd = true;


                if(facebookNativeAdLayout != null) {
                    facebookNativeAdLayout.setVisibility(View.GONE);
                }
                if(onError == GOOGLE_AD) {
                    new GoogleAds(mContext, googleAppId).loadGoogleNativeAd(googleNativeAdPlaceholder, googleNativeAdId);
                }

                if(onError == FACEBOOK_AD){
                    loadFacebookNativeAd( facebookNativeAdLayout, facebookNativeAdId);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {

                if (facebookNativeAd == null || facebookNativeAd != ad) {
                    return;
                }

                if(googleNativeAdPlaceholder != null){
                    googleNativeAdPlaceholder.setVisibility(View.GONE);
                }

                // Inflate Native Ad into Container
                if(facebookNativeAdLayout != null) {
                    facebookNativeAdLayout.setVisibility(View.VISIBLE);
                    inflatefacebookNativeAd((NativeAdLayout) facebookNativeAdLayout, facebookNativeAd);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression

                canRequestNativeAd = true;

            }
        });

        // Request an ad
        facebookNativeAd.loadAd();
    }


    public void loadFacebookNativeAd(final View facebookNativeAdLayout, String facebookNativeAdId) {

        if(!canRequestNativeAd){
            return;
        }

        canRequestNativeAd = false;

        facebookNativeAd = new NativeAd(mContext, facebookNativeAdId);

        facebookNativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

                canRequestNativeAd = true;

                if(facebookNativeAdLayout != null) {
                    facebookNativeAdLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {

                if (facebookNativeAd == null || facebookNativeAd != ad) {
                    return;
                }

                if(facebookNativeAdLayout != null) {
                    facebookNativeAdLayout.setVisibility(View.VISIBLE);
                    inflatefacebookNativeAd((NativeAdLayout) facebookNativeAdLayout, facebookNativeAd);
                }

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                canRequestNativeAd = true;
            }
        });

        // Request an ad
        facebookNativeAd.loadAd();
    }


    private void inflatefacebookNativeAd(NativeAdLayout facebookNativeAdLayout, NativeAd nativeAd) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        facebookNativeAdLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        LinearLayout facebookNativeAdView = (LinearLayout) inflater.inflate(R.layout.fb_native_ad_layout, facebookNativeAdLayout, false);
        facebookNativeAdLayout.addView(facebookNativeAdView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = facebookNativeAdView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(mContext, nativeAd, facebookNativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = facebookNativeAdView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = facebookNativeAdView.findViewById(R.id.native_ad_title);
        com.facebook.ads.MediaView nativeAdMedia = facebookNativeAdView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = facebookNativeAdView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = facebookNativeAdView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = facebookNativeAdView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = facebookNativeAdView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                facebookNativeAdView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }




}
