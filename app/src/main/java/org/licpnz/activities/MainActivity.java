package org.licpnz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.licpnz.R;
import org.licpnz.fragments.MenuFragment;
import org.licpnz.fragments.NewsListFragment;

/**
 * Created by Ilya on 17.11.2016.
 */

public class MainActivity extends Activity {


    public NewsListFragment mNewsListFragment;
    public MenuFragment mMenuFragment;
    public static final String mMenuFragmentTag = "MenuFragment";
    public static final String mNewsListFragmentTag = "NewsListFragment";
    public static final String mNewsDetailsFragmentTag = "NewsDetailsFragment";
    public FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mFragmentContainer = (FrameLayout) findViewById(R.id.main_fragment_container);

        if (savedInstanceState==null){
            mNewsListFragment = new NewsListFragment();
            mNewsListFragment.setRetainInstance(false);
            mMenuFragment = new MenuFragment();
            mMenuFragment.setRetainInstance(false);
            getFragmentManager().beginTransaction()
                    .add(R.id.main_fragment_container,mNewsListFragment,mNewsListFragmentTag)
                    .add(R.id.drawer_frame,mMenuFragment,mMenuFragmentTag)
                    .commit();
        }else{
            mNewsListFragment = (NewsListFragment)getFragmentManager().findFragmentByTag(mNewsListFragmentTag);
            mMenuFragment = (MenuFragment) getFragmentManager().findFragmentByTag(mMenuFragmentTag);
        }
    }


}
