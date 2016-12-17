package org.licpnz.settings;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import org.licpnz.LicApplication;
import org.licpnz.R;
import org.licpnz.api.Api;
import org.licpnz.dialogs.HostChangeDialogFragment;
import org.licpnz.fragments.SettingsFragment;

import java.util.ArrayList;

/**
 * Created by Ilya on 14.12.2016.
 */

public class ConnectionSettingsFragment extends Fragment implements View.OnClickListener{

    FrameLayout mContent;
    RecyclerView mList;
    RecyclerView.LayoutManager mLM;
    Adapter mAdapter;
    ArrayList<Setting> mSettings = new ArrayList<Setting>();

    public ConnectionSettingsFragment() {
        super();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = (FrameLayout) getActivity().getLayoutInflater().inflate(R.layout.settings_fragment_layout,null);
        mList = (RecyclerView) mContent.findViewById(R.id.settings_list);
        mAdapter = new Adapter();
        mLM = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mList.setLayoutManager(mLM);
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setAdapter(mAdapter);
        addSetting(222,getResources().getDrawable(R.mipmap.ic_launcher),"Change host ", Api.getHost());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContent;
    }

    public void addSetting(int id, Drawable icon, String title, String subtitle){
        Setting s = new Setting();
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
                new HostChangeDialogFragment()
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

    public class Adapter extends RecyclerView.Adapter<ItemHolder>{

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.settings_menu_element,null);
            v.setOnClickListener(ConnectionSettingsFragment.this);
            v.setLayoutParams(new RecyclerView.LayoutParams(-1,-2));
            ItemHolder ih = new ItemHolder(v);
            return ih;
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Setting s = mSettings.get(position);
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
