package org.licpnz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.licpnz.R;
import org.licpnz.fragments.NewsListFragment;

/**
 * Created by Ilya on 17.11.2016.
 */

public class MainActivity extends Activity {


    public NewsListFragment mNewsListFragment;
    public String mNewsListFragmentTag = "NewsListFragment";
    public FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mFragmentContainer = (FrameLayout) findViewById(R.id.main_fragment_container);

        if (savedInstanceState==null){
            mNewsListFragment = new NewsListFragment();
            mNewsListFragment.setRetainInstance(false);
            getFragmentManager().beginTransaction()
                    .add(R.id.main_fragment_container,mNewsListFragment,mNewsListFragmentTag)
                    .commit();
        }else{
            mNewsListFragment = (NewsListFragment)getFragmentManager().findFragmentByTag(mNewsListFragmentTag);
        }
    }


}
