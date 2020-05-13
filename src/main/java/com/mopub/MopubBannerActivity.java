package com.mopub;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.mopub.common.AdapterConfigurationManager;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.PangolinAudienceAdBannerAdapter;
import com.union_test.toutiao.R;

import java.util.HashMap;
import java.util.Map;


/**
 * created by wuzejian on 2019/11/29
 */
@SuppressLint("LongLogTag")
public class MopubBannerActivity extends Activity implements MoPubView.BannerAdListener {
    private static final String TAG = "MopubBannerActivity";

    private String mAdUnitId = "f1c07b4e194040e7b172b051ca3cb233";

    private MoPubView mMoPubView;
    private EditText mEtWidth;
    private EditText mEtHeight;

    private boolean mHasInit = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mopub_activity_banner_express);

        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(mAdUnitId)
                .withLogLevel(MoPubLog.LogLevel.DEBUG)
                .build();

        //init MoPub SDK
        MoPub.initializeSdk(this, sdkConfiguration, initSdkListener());

        initView();

    }

    private AdapterConfigurationManager subject;

    private void initView() {
        mMoPubView = findViewById(R.id.adview);
        mEtHeight = findViewById(R.id.express_height);
        mEtWidth = findViewById(R.id.express_width);
        float expressViewWidth = 600;
        float expressViewHeight = 400;
        try {
            expressViewWidth = Float.parseFloat(mEtWidth.getText().toString());
            expressViewHeight = Float.parseFloat(mEtHeight.getText().toString());
        } catch (Exception e) {
        }

        if (expressViewWidth == 0) expressViewWidth = 600;
        if (expressViewHeight == 0) expressViewHeight = 400;


        final Map<String, Object> localExtras = new HashMap<>();
        localExtras.put(PangolinAudienceAdBannerAdapter.GDPR_RESULT, 1);
        localExtras.put(PangolinAudienceAdBannerAdapter.AD_BANNER_WIDTH, expressViewWidth);
        localExtras.put(PangolinAudienceAdBannerAdapter.AD_BANNER_HEIGHT, expressViewHeight);


        findViewById(R.id.btn_express_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHasInit) {
                    Utils.logToast(MopubBannerActivity.this, "init not finish, wait");
                    return;
                }
                localExtras.put(PangolinAudienceAdBannerAdapter.KEY_EXTRA_AD_UNIT_ID, "945113149");
                mMoPubView.setAdUnitId(mAdUnitId); // Enter your Ad Unit ID from www.mopub.com
                mMoPubView.setLocalExtras(localExtras);
                mMoPubView.loadAd();
            }
        });


        findViewById(R.id.btn_native_load_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHasInit) {
                    Utils.logToast(MopubBannerActivity.this, "init not finish, wait");
                    return;
                }
                localExtras.put(PangolinAudienceAdBannerAdapter.KEY_EXTRA_AD_UNIT_ID, "945071432");
                mMoPubView.setAdUnitId(mAdUnitId); // Enter your Ad Unit ID from www.mopub.com
                mMoPubView.setLocalExtras(localExtras);
                mMoPubView.loadAd();
            }
        });


        mMoPubView.setBannerAdListener(this);

    }

    @Override
    public void onBannerLoaded(MoPubView banner) {
        Utils.logToast(MopubBannerActivity.this, "MoPubView onBannerLoaded.");
    }

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        Utils.logToast(MopubBannerActivity.this, "MoPubView onBannerFailed.-" + errorCode.toString());
    }

    @Override
    public void onBannerClicked(MoPubView banner) {
        Utils.logToast(MopubBannerActivity.this, "MoPubView onBannerClicked.");

    }

    @Override
    public void onBannerExpanded(MoPubView banner) {
    }

    @Override
    public void onBannerCollapsed(MoPubView banner) {

    }


    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                mHasInit = true;
                Log.d("TiktokAdapter", "onInitializationFinished////");
           /* MoPub SDK initialized.
           Check if you should show the consent dialog here, and make your ad requests. */
            }
        };
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMoPubView != null) {
            mMoPubView.destroy();
            mMoPubView = null;
        }
    }
}
