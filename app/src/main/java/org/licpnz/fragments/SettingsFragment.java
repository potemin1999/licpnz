package org.licpnz.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.licpnz.R;

import java.util.ArrayList;

/**
 * Created by Ilya on 17.11.2016.
 */

public class SettingsFragment extends Fragment {

    FrameLayout mContainer;
    RecyclerView mList;
    SettingsAdapter mAdapter;
    RecyclerView.LayoutManager mLM;
    ArrayList<Setting> mSettings = new ArrayList<Setting>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer = (FrameLayout) getActivity().getLayoutInflater().inflate(R.layout.settings_fragment_layout,null);
        mList = (RecyclerView) mContainer.findViewById(R.id.settings_list);
        mAdapter = new SettingsAdapter();
        mLM = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mList.setLayoutManager(mLM);
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setAdapter(mAdapter);
        addSetting(0,getResources().getDrawable(R.mipmap.ic_launcher),getString(R.string.settings_category_news),null);
        addSetting(1,getResources().getDrawable(R.mipmap.ic_launcher),getString(R.string.settings_category_about),null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContainer;
    }

    public void addSetting(int id,Drawable icon,String title,String subtitle){
        Setting s = new Setting();
        s.mIcon = icon;
        s.mId = id;
        s.mTitle = title;
        s.mSubtitle = subtitle;
        mSettings.add(s);
        mAdapter.notifyItemInserted(mSettings.size()-1);
    }






    public class SettingsAdapter extends RecyclerView.Adapter<SettingHolder>{

        @Override
        public SettingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout ll = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.settings_menu_element,null);
            ll.setLayoutParams(new RecyclerView.LayoutParams(-1,-2));
            SettingHolder sh = new SettingHolder(ll);
            return sh;
        }

        @Override
        public void onBindViewHolder(SettingHolder holder, int position) {
            Setting s = mSettings.get(position);
            holder.mTitle.setText(s.mTitle);
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

    public class SettingHolder extends RecyclerView.ViewHolder{

        LinearLayout mContainer;
        TextView mTitle;
        ImageView mImage;

        public SettingHolder(View v){
            super(v);
            mContainer = (LinearLayout) v;
            mTitle = (TextView) mContainer.findViewById(R.id.element_text);
            mImage = (ImageView) mContainer.findViewById(R.id.element_image);
        }
    }


    public class Setting{
        public String mTitle="";
        public Drawable mIcon=null;
        public String mSubtitle=null;
        public int mId=0;
    }




}
