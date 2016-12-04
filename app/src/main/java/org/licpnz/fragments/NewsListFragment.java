package org.licpnz.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.licpnz.R;
import org.licpnz.ui.adapter.NewsAdapter;

/**
 * Created by Ilya on 17.11.2016.
 */

public class NewsListFragment extends Fragment {

    public ViewGroup mContainer;
    public RecyclerView mRecyclerView;
    public NewsAdapter mListAdapter;
    public RecyclerView.LayoutManager mListLayoutManager;
    public RecyclerView.ItemAnimator mListItemAnimator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new NewsAdapter(getActivity());
        mListLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mListItemAnimator = new DefaultItemAnimator();
        mContainer = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.news_list_fragment_layout,null);
        mRecyclerView = (RecyclerView) mContainer.findViewById(R.id.news_fragment_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mListLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setItemAnimator(mListItemAnimator);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContainer;
    }


}
