package org.licpnz.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.FrameLayout;

import org.licpnz.R;
import org.licpnz.fragments.SettingsFragment;

public class SettingsActivity extends Activity {

    FrameLayout mContainer;
    SettingsFragment mFragment;
    @IdRes
    int mContainerId = 345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer = new FrameLayout(this);
        mContainer.setId(mContainerId);
        setContentView(mContainer);
        if (savedInstanceState==null){
            mFragment = new SettingsFragment();
            mFragment.setRetainInstance(false);
            getFragmentManager().beginTransaction()
                    .add(mContainerId,mFragment,"SettingsFragment")
                    .commit();
        }else{
            mFragment = (SettingsFragment) getFragmentManager().findFragmentByTag("SettingsFragment");
        }
    }
}
