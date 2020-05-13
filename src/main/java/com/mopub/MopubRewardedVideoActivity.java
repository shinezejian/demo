package com.mopub;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.mopub.common.MediationSettings;
import com.mopub.common.MoPub;
import com.mopub.common.MoPubReward;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideoManager;
import com.mopub.mobileads.MoPubRewardedVideos;
import com.mopub.mobileads.PangolinAdapterConfiguration;
import com.mopub.mobileads.TiktokAudienceAdRewardedVideoAdapter;
import com.union_test.toutiao.R;
import com.union_test.toutiao.activity.expressad.DrawNativeExpressVideoActivity;
import com.union_test.toutiao.utils.TToast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * created by wuzejian on 2019/4/14
 */
@SuppressLint("LongLogTag")
public class MopubRewardedVideoActivity extends Activity implements MoPubRewardedVideoListener {
    private static final String TAG = "MopubRewardedVideoActivity";
    @Nullable
    private Button mShowButton;
    @Nullable
    private String mAdUnitId = "b864879e52fa4b2c9462fe75a068d497";

    private boolean mHasInit = false;


    // private String mCodeId = "901121365";//TTAdConstant.VERTICAL

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mopub_reward_activity);
        Map<String, String> mediatedNetworkConfiguration1 = new HashMap<>();
        mediatedNetworkConfiguration1.put("AppKey", "000000000000000");

        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(mAdUnitId)
                .withMediationSettings()
                .withMediatedNetworkConfiguration(PangolinAdapterConfiguration.class.toString(), mediatedNetworkConfiguration1)
                .withLogLevel(MoPubLog.LogLevel.DEBUG)
                .withLegitimateInterestAllowed(true)//是否允许收集经纬度信息
                .withMediationSettings(
                        new TiktokAudienceAdRewardedVideoAdapter.PangolinRewardMediationSettings.Builder()
                                .setCodeId("901121365")
                                .setOrientation(TTAdConstant.VERTICAL)
                                .setGdpr(0)//GDRP value if need : 0 close GDRP Privacy protection ，1: open GDRP Privacy protection
                                .setMediaExtra("mediaExtra")
                                .setRewardAmount(3)
                                .setRewardName("gold coin")
                                .setUserID("user123")
                                .builder())
                .build();

        // Set location awareness and precision globally for your app:
        MoPub.setLocationAwareness(MoPub.LocationAwareness.TRUNCATED);
        MoPub.setLocationPrecision(4);
        MoPub.initializeSdk(this, sdkConfiguration, initSdkListener());
        MoPubRewardedVideos.setRewardedVideoListener(this);
        findViewById(R.id.loadRewardAd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHasInit) {
                    Utils.logToast(MopubRewardedVideoActivity.this, "init not finish, wait");
                    return;
                }
                MoPubRewardedVideoManager.updateActivity(MopubRewardedVideoActivity.this);// must updateActivity
                MoPubRewardedVideos.loadRewardedVideo(mAdUnitId);
            }
        });

        mShowButton = findViewById(R.id.showRewardAd);
        mShowButton.setEnabled(false);
        mShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoPubRewardedVideos.showRewardedVideo(mAdUnitId);
            }
        });


    }

    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                mHasInit = true;
                Log.d("MopubRewardedVideoActivity", "onInitializationFinished////");
           /* MoPub SDK initialized.
           Check if you should show the consent dialog here, and make your ad requests. */
            }
        };
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onRewardedVideoLoadSuccess(@NonNull String adUnitId) {
        Log.d(TAG, "onRewardedVideoLoadSuccess.....adUnitId=" + adUnitId);
        if (adUnitId.equals(mAdUnitId)) {
            if (mShowButton != null) {
                TToast.show(MopubRewardedVideoActivity.this, " onRewardedVideoLoadSuccess!");
                mShowButton.setEnabled(true);
            }
        }
        Utils.logToast(this, " onRewardedVideoLoadSuccess....");
    }

    @Override
    public void onRewardedVideoLoadFailure(@NonNull String s, @NonNull MoPubErrorCode moPubErrorCode) {
        Log.d(TAG, "onRewardedVideoLoadFailure....." + moPubErrorCode);
        Utils.logToast(this, " onRewardedVideoLoadFailure....moPubErrorCode=" + moPubErrorCode);

    }

    @Override
    public void onRewardedVideoStarted(@NonNull String s) {
        mShowButton.setEnabled(false);
        Log.d(TAG, "onRewardedVideoStarted.....");
        Utils.logToast(this, " onRewardedVideoStarted....");
    }

    @Override
    public void onRewardedVideoPlaybackError(@NonNull String s, @NonNull MoPubErrorCode moPubErrorCode) {
        Log.d(TAG, "onRewardedVideoPlaybackError.....");
        Utils.logToast(this, " onRewardedVideoPlaybackError....");

    }

    @Override
    public void onRewardedVideoClicked(@NonNull String s) {
        Log.d(TAG, "onRewardedVideoClicked.....");
        TToast.show(MopubRewardedVideoActivity.this, " onRewardedVideoClicked!");

    }

    @Override
    public void onRewardedVideoClosed(@NonNull String s) {
        Log.d(TAG, "onRewardedVideoClosed.....");
        Utils.logToast(this, " onRewardedVideoClosed....");


    }

    @Override
    public void onRewardedVideoCompleted(@NonNull Set<String> set, @NonNull MoPubReward moPubReward) {
        Log.d(TAG, "onRewardedVideoCompleted.....");
        Utils.logToast(this, " onRewardedVideoCompleted....");

    }

    @Override
    public void onDestroy() {
        MoPubRewardedVideos.setRewardedVideoListener(null);
        super.onDestroy();
    }
}
