package com.example.mrye.xiaoyouquan.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mrye.xiaoyouquan.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mr.Ye on 2017/4/19.
 */

public class MenuItemAdapter extends BaseAdapter {

    private static class LvMenuItem {
        int name;
        int icon;

        public LvMenuItem(int icon, int name) {
            this.name = name;
            this.icon = icon;
        }
    }

    private final int mIconSize;
    private LayoutInflater mInflater;
    private Context mContext;


    public MenuItemAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);

        mIconSize = 24;
    }

    private List<LvMenuItem> mItems = new ArrayList<>(
            Arrays.asList(
                    new LvMenuItem(R.drawable.ic_star_border_black_24dp, R.string.nav_menu_wall),
                    new LvMenuItem(R.drawable.ic_star_border_black_24dp, R.string.nav_menu_market),
                    new LvMenuItem(R.drawable.ic_star_border_black_24dp, R.string.nav_menu_lost),
                    new LvMenuItem(R.drawable.ic_star_border_black_24dp, R.string.nav_menu_class),
                    new LvMenuItem(R.drawable.ic_star_border_black_24dp, R.string.nav_menu_delivery)
            )
    );

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LvMenuItem item=mItems.get(position);
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.nav_menu_item, parent,
                    false);
        }
        TextView itemView = (TextView) convertView;
        itemView.setText(item.name);
        itemView.setCompoundDrawablesWithIntrinsicBounds(item.icon,0,0,0);
        return convertView;
    }

}
