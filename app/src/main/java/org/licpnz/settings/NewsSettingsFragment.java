package org.licpnz.settings;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.licpnz.LicApplication;
import org.licpnz.R;
import org.licpnz.api.Api;
import org.licpnz.dialogs.HostChangeDialogFragment;
import org.licpnz.dialogs.ImageLoadingDialogFragment;

import java.util.ArrayList;

/**
 * Created by Ilya on 21.12.2016.
 */

public class NewsSettingsFragment extends Fragment implements View.OnClickListener {

    FrameLayout mContent;
    RecyclerView mList;
    RecyclerView.LayoutManager mLM;
    NewsSettingsFragment.Adapter mAdapter;
    ArrayList<NewsSettingsFragment.Setting> mSettings = new ArrayList<NewsSettingsFragment.Setting>();
    Toolbar mToolbar;

    public NewsSettingsFragment() {
        super();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = (FrameLayout) getActivity().getLayoutInflater().inflate(R.layout.settings_fragment_layout,null);
        mToolbar = (Toolbar) mContent.findViewById(R.id.settings_toolbar);
        mToolbar.setTitle(R.string.settings_news_title);
        mList = (RecyclerView) mContent.findViewById(R.id.settings_list);
        mAdapter = new NewsSettingsFragment.Adapter();
        mLM = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mList.setLayoutManager(mLM);
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setAdapter(mAdapter);
        addSetting(222,null,"Загрузка изображений",(LicApplication.getInstance().getImageLoadingEnabled() ? "включено" : "выключено"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContent;
    }

    public void addSetting(int id, Drawable icon, String title, String subtitle){
        NewsSettingsFragment.Setting s = new NewsSettingsFragment.Setting();
        s.mIcon = icon;
        s.mId = id;
        s.mTitle = title;
        s.mSubtitle = subtitle;
        mSettings.add(s);
        mAdapter.notifyItemInserted(mSettings.size()-1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case 0:{
                new ImageLoadingDialogFragment()
                        .show(getFragmentManager(),"HostChangeDialog");
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }


    public class ItemHolder extends RecyclerView.ViewHolder{

        LinearLayout mContainer;
        TextView mTitle;
        TextView mSubtitle;
        ImageView mImage;

        public ItemHolder(View v){
            super(v);
            mContainer = (LinearLayout) v;
            mTitle = (TextView) mContainer.findViewById(R.id.element_text);
            mSubtitle = (TextView) mContainer.findViewById(R.id.element_subtext);
            mImage = (ImageView) mContainer.findViewById(R.id.element_image);
        }
    }

    public class Setting{
        public String mTitle="";
        public Drawable mIcon=null;
        public String mSubtitle=null;
        public int mId=0;
    }

    public class Adapter extends RecyclerView.Adapter<NewsSettingsFragment.ItemHolder>{

        @Override
        public NewsSettingsFragment.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.settings_menu_element,null);
            v.setOnClickListener(NewsSettingsFragment.this);
            v.setLayoutParams(new RecyclerView.LayoutParams(-1,-2));
            NewsSettingsFragment.ItemHolder ih = new NewsSettingsFragment.ItemHolder(v);
            return ih;
        }

        @Override
        public void onBindViewHolder(NewsSettingsFragment.ItemHolder holder, int position) {
            NewsSettingsFragment.Setting s = mSettings.get(position);
            holder.mTitle.setText(s.mTitle);
            holder.mContainer.setId(position);
            if (s.mSubtitle!=null)
                holder.mSubtitle.setText(s.mSubtitle);
            else
                holder.mSubtitle.setText("");
            if (s.mIcon!=null)
                holder.mImage.setImageDrawable(s.mIcon);
            else
                holder.mImage.setImageDrawable(null);
        }

        @Override
        public int getItemCount() {
            return mSettings.size();
        }
    }

}
