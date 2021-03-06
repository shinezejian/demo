package com.union_test.toutiao.activity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mopub.MopubBannerActivity;
import com.mopub.MopubFullVideoActivity;
import com.mopub.MopubInterstitialActivity;
import com.mopub.MopubNativeListActivity;
import com.mopub.MopubRewardedVideoActivity;
import com.union_test.toutiao.R;

/**
 * created by wuzejian on 2019/4/16
 */
public class MopubAdapterActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mopub_adapter);

        findViewById(R.id.btn_mopub_reward_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MopubAdapterActivity.this, MopubRewardedVideoActivity.class);
                MopubAdapterActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.btn_mopub_full_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MopubAdapterActivity.this, MopubFullVideoActivity.class);
                MopubAdapterActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.btn_mopub_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MopubAdapterActivity.this, MopubBannerActivity.class);
                MopubAdapterActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.btn_mopub_interstitial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MopubAdapterActivity.this, MopubInterstitialActivity.class);
                MopubAdapterActivity.this.startActivity(intent);
            }
        });


        findViewById(R.id.btn_mopub_feed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MopubAdapterActivity.this, MopubNativeListActivity.class);
                MopubAdapterActivity.this.startActivity(intent);
            }
        });
    }
}
