package org.licpnz.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.licpnz.R;
import org.licpnz.activities.SettingsActivity;

/**
 * Created by Ilya on 09.12.2016.
 */

public class MenuFragment extends Fragment implements View.OnClickListener {

    LinearLayout mContainer;
    LinearLayout mContent;
    ImageView mHeaderImage;
    Bitmap mHeaderBitmap;
    DrawerLayout mMainDrawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.main_menu_layout,null,true);
        mContent = (LinearLayout) mContainer.findViewById(R.id.drawer_menu_linear_container);
        mMainDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.main_drawer_layout);
        mHeaderImage = (ImageView) mContainer.findViewById(R.id.menu_header_image);
        try {
            mHeaderBitmap = BitmapFactory.decodeStream(getResources().getAssets().open("logo_1024.png"));
            mHeaderImage.setImageBitmap(mHeaderBitmap);
        }catch(Throwable t){}
        mContent.addView(getMenuElement(0,getResources().getDrawable(R.drawable.ic_receipt_white_48dp),getString(R.string.drawer_menu_news)));
        mContent.addView(getMenuElement(1,getResources().getDrawable(R.drawable.ic_settings_white_48dp),getString(R.string.drawer_menu_settings)));
        mContainer.setFitsSystemWindows(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContainer;
    }

    public View getMenuElement(int id, Drawable image,String s){
        LinearLayout ll = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.main_menu_element,null);
        ImageView iv = (ImageView) ll.findViewById(R.id.main_menu_element_image);
        TextView tv = (TextView) ll.findViewById(R.id.main_menu_element_title);
        ll.setOnClickListener(this);
        ll.setId(id);
        if (image!=null)
            iv.setImageDrawable(image);
        if (s!=null)
            tv.setText(s);
        return ll;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case 0:{
                break;
            }
            case 1:{
                startActivity(new Intent(getActivity(),SettingsActivity.class));
                break;
            }
        }
        mMainDrawerLayout.closeDrawers();
    }
}
