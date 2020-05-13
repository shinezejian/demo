package com.mopub;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.MediaViewBinder;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.MoPubVideoNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.PangolinAdRenderer;
import com.mopub.nativeads.PangolinNativeAdapter;
import com.mopub.nativeads.PangolinViewBinder;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;
import com.union_test.toutiao.R;
import com.union_test.toutiao.dialog.DislikeDialog;
import com.union_test.toutiao.view.LoadMoreListView;

import java.util.EnumSet;
import java.util.List;

/**
 * created by wuzejian on 2020/5/12
 */
public class MopubNativeListActivity extends Activity {

    private String mAdUnitId = "20a543a976104da2b775fd919f497f24";
    private MoPubNative mMoPubNative;

    private LinearLayout mAdContainer;
    @Nullable
    private RequestParameters mRequestParameters;


    private Context getActivity() {
        return MopubNativeListActivity.this;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.native_manual_fragment);

        mAdContainer = findViewById(R.id.parent_view);

        findViewById(R.id.load_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                updateRequestParameters();

                if (mAdContainer != null) {
                    mAdContainer.removeAllViews();
                }

                if (mMoPubNative != null) {
                    mMoPubNative.makeRequest(mRequestParameters);
                } else {
                    Utils.logToast(getActivity(), getName() + " failed to load. MoPubNative instance is null.");
                }
            }


        });
        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(mAdUnitId)
                .withLogLevel(MoPubLog.LogLevel.DEBUG)//Log级别
                .build();

        //init MoPub SDK
        MoPub.initializeSdk(this, sdkConfiguration, initSdkListener());
    }

    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
//                mHasInit = true;
                Log.d("MopubFullVideoActivity", "onInitializationFinished////");
                loadNativeAd();
            }
        };
    }

    private void loadNativeAd() {
        String adUnitId = mAdUnitId;


        mMoPubNative = new MoPubNative(getActivity(), adUnitId, new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(NativeAd nativeAd) {


                NativeAd.MoPubNativeEventListener moPubNativeEventListener = new NativeAd.MoPubNativeEventListener() {
                    @Override
                    public void onImpression(View view) {
                        // The ad has registered an impression. You may call any app logic that
                        // depends on having the ad view shown.
                        Utils.logToast(getActivity(), getName() + " impressed.");
                    }

                    @Override
                    public void onClick(View view) {
                        Utils.logToast(getActivity(), getName() + " clicked.");
                    }
                };


                // In a manual integration, any interval that is at least 2 is acceptable
                final AdapterHelper adapterHelper = new AdapterHelper(getActivity(), 0, 2);
                final View adView;
                adView = adapterHelper.getAdView(null, null, nativeAd, new ViewBinder.Builder(0).build());
                nativeAd.setMoPubNativeEventListener(moPubNativeEventListener);
                //set dislike
                if (nativeAd.getBaseNativeAd() instanceof PangolinNativeAdapter.PangolinNativeAd) {
                    PangolinNativeAdapter.PangolinNativeAd nativePangleAd = (PangolinNativeAdapter.PangolinNativeAd) nativeAd.getBaseNativeAd();
                    //set dislike
                    bindDislikeCustom(adView.findViewById(R.id.iv_listitem_dislike), nativePangleAd);
                }
                if (mAdContainer != null) {
                    mAdContainer.addView(adView);
                } else {
                    Utils.logToast(getActivity(), getName() + " failed to show. Ad container is null.");
                }
            }

            @Override
            public void onNativeFail(NativeErrorCode errorCode) {
                Utils.logToast(getActivity(), getName() + " failed to load: " + errorCode.toString());
                Log.e("onNativeFail", " failed to load: " + errorCode.toString());
            }
        });

        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(
                new ViewBinder.Builder(R.layout.native_ad_list_item)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mainImageId(R.id.native_main_image)
                        .iconImageId(R.id.native_icon_image)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build()
        );

        // Set up a renderer for a video native ad.
        MoPubVideoNativeAdRenderer moPubVideoNativeAdRenderer = new MoPubVideoNativeAdRenderer(
                new MediaViewBinder.Builder(R.layout.video_ad_list_item)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mediaLayoutId(R.id.native_media_layout)
                        .iconImageId(R.id.native_icon_image)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build());

        final PangolinAdRenderer largeImageAdRender = new PangolinAdRenderer(
                new PangolinViewBinder.Builder(R.layout.listitem_ad_large_pic_layout)
                        .layoutType(PangolinViewBinder.IMAGE_MODE_LARGE_IMG)
                        .logoViewId(R.id.tt_ad_logo)
                        .callToActionId(R.id.tt_creative_btn)
                        .decriptionTextId(R.id.tv_listitem_ad_desc)
                        .iconImageId(R.id.iv_listitem_icon)
                        .sourceId(R.id.tv_listitem_ad_source)
                        .titleId(R.id.tv_listitem_ad_title)
                        .dislikeId(R.id.iv_listitem_dislike)
                        .mainImageId(R.id.iv_listitem_large_image)
                        .build()
        );

        final PangolinAdRenderer smallImageAdRender = new PangolinAdRenderer(
                new PangolinViewBinder.Builder(R.layout.listitem_ad_small_pic)
                        .layoutType(PangolinViewBinder.IMAGE_MODE_SMALL_IMG)
                        .logoViewId(R.id.tt_ad_logo)
                        .callToActionId(R.id.tt_creative_btn)
                        .decriptionTextId(R.id.tv_listitem_ad_desc)
                        .iconImageId(R.id.iv_listitem_icon)
                        .sourceId(R.id.tv_listitem_ad_source)
                        .titleId(R.id.tv_listitem_ad_title)
                        .dislikeId(R.id.iv_listitem_dislike)
                        .smallImageId(R.id.iv_listitem_small_image)
                        .build()
        );


        final PangolinAdRenderer groupImageAdRender = new PangolinAdRenderer(
                new PangolinViewBinder.Builder(R.layout.listitem_ad_group_pic)
                        .layoutType(PangolinViewBinder.IMAGE_MODE_GROUP_IMG)
                        .logoViewId(R.id.tt_ad_logo)
                        .titleId(R.id.tv_listitem_ad_title)
                        .sourceId(R.id.tv_listitem_ad_source)
                        .decriptionTextId(R.id.tv_listitem_ad_desc)
                        .iconImageId(R.id.iv_listitem_icon)
                        .dislikeId(R.id.iv_listitem_dislike)
                        .callToActionId(R.id.tt_creative_btn)
                        .groupImage1Id(R.id.iv_listitem_image1)
                        .groupImage2Id(R.id.iv_listitem_image2)
                        .groupImage3Id(R.id.iv_listitem_image3)
                        .build()
        );


        final PangolinAdRenderer verticalImageAdRender = new PangolinAdRenderer(
                new PangolinViewBinder.Builder(R.layout.listitem_ad_vertical_pic)
                        .layoutType(PangolinViewBinder.IMAGE_MODE_VERTICAL_IMG)
                        .logoViewId(R.id.tt_ad_logo)
                        .callToActionId(R.id.tt_creative_btn)
                        .decriptionTextId(R.id.tv_listitem_ad_desc)
                        .iconImageId(R.id.iv_listitem_icon)
                        .sourceId(R.id.tv_listitem_ad_source)
                        .titleId(R.id.tv_listitem_ad_title)
                        .dislikeId(R.id.iv_listitem_dislike)
                        .verticalImageId(R.id.iv_listitem_vertical_image)
                        .build());


        final PangolinAdRenderer videoImageAdRender = new PangolinAdRenderer(
                new PangolinViewBinder.Builder(R.layout.listitem_ad_vertical_pic)
                        .layoutType(PangolinViewBinder.IMAGE_MODE_VIDEO)
                        .logoViewId(R.id.tt_ad_logo)
                        .callToActionId(R.id.tt_creative_btn)
                        .decriptionTextId(R.id.tv_listitem_ad_desc)
                        .iconImageId(R.id.iv_listitem_icon)
                        .sourceId(R.id.tv_listitem_ad_source)
                        .titleId(R.id.tv_listitem_ad_title)
                        .dislikeId(R.id.iv_listitem_dislike)
                        .mediaViewIdId(R.id.iv_listitem_video)
                        .build());

        // The first renderer that can handle a particular native ad gets used.
        // We are prioritizing network renderers.

        mMoPubNative.registerAdRenderer(largeImageAdRender);
        mMoPubNative.registerAdRenderer(smallImageAdRender);
        mMoPubNative.registerAdRenderer(verticalImageAdRender);
        mMoPubNative.registerAdRenderer(videoImageAdRender);
        mMoPubNative.registerAdRenderer(groupImageAdRender);
        mMoPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);
        mMoPubNative.registerAdRenderer(moPubVideoNativeAdRenderer);
        mMoPubNative.makeRequest(mRequestParameters);
    }

    private void bindDislikeCustom(View dislike, final PangolinNativeAdapter.PangolinNativeAd ad) {
        List<FilterWord> words = ad.getFilterWords();
        if (words == null || words.isEmpty()) {
            return;
        }

        final DislikeDialog dislikeDialog = new DislikeDialog(this, words);
        dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
            @Override
            public void onItemClick(FilterWord filterWord) {
                //remove view
                if (mAdContainer != null) {
                    mAdContainer.removeAllViews();
                }
            }
        });
        final TTAdDislike ttAdDislike = ad.getDislikeDialog(dislikeDialog);

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dislike
                dislikeDialog.show();

                //or
                //ttAdDislike.showDislikeDialog();
            }
        });
    }

    private void updateRequestParameters() {
        final String keywords = "";
        final String userDataKeywords = "";

        // Setting desired assets on your request helps native ad networks and bidders
        // provide higher-quality ads.
        final EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.ICON_IMAGE,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT,
                RequestParameters.NativeAdAsset.STAR_RATING);

        mRequestParameters = new RequestParameters.Builder()
                .keywords(keywords)
                .userDataKeywords(userDataKeywords)
                .desiredAssets(desiredAssets)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMoPubNative != null) {
            mMoPubNative.destroy();
            mMoPubNative = null;
        }

        if (mAdContainer != null) {
            mAdContainer.removeAllViews();
            mAdContainer = null;
        }
    }

    private String getName() {

        return "pangolin";
    }
}
