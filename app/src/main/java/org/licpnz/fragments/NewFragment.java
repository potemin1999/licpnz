package org.licpnz.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.licpnz.activities.MainActivity;
import org.licpnz.api.news.New;
import org.licpnz.ui.adapter.NewsAdapter;

/**
 * Created by Ilya on 05.12.2016.
 */

public class NewFragment extends Fragment {

    LinearLayout mContainer;
    TextView mTitleTextView;
    TextView mContentTextView;
    New mNew;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNew = (New) savedInstanceState.getSerializable("new");
        mContainer = (LinearLayout) getActivity().getLayoutInflater().inflate(org.licpnz.ui.R.layout.new_detailed_layout,null);
        mTitleTextView = (TextView) mContainer.findViewById(org.licpnz.ui.R.id.new_detailed_title_text_view);
        mContentTextView = (TextView) mContainer.findViewById(org.licpnz.ui.R.id.new_detailed_content_text_view);
        mTitleTextView.setText(mNew.mT.mText);
        mContentTextView.setText(mNew.mM.mText);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContainer;
    }

    public static void newInstance(NewsListFragment nlf, New n, NewsAdapter.NewsHolder nh){
        NewFragment nf = new NewFragment();
        Bundle b = new Bundle();
        b.putSerializable("new",n);
        nf.setRetainInstance(false);
        nf.setArguments(b);
        FragmentTransaction ft = nlf.getFragmentManager().beginTransaction();
        ft.replace(org.licpnz.ui.R.id.main_fragment_container,nf, MainActivity.mNewsDetailsFragmentTag);
        if (true){

        }
        ft.commit();
    }
}
