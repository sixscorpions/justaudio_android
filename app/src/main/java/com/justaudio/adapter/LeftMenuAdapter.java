package com.justaudio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.dto.LeftMenuModel;
import com.justaudio.utils.FontFamily;

import java.util.ArrayList;

/**
 * Created by VIDYA
 */

public class LeftMenuAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<LeftMenuModel> list;
    private LayoutInflater mInflater;

    public LeftMenuAdapter(Context mContext, ArrayList<LeftMenuModel> mLeftMenuItems) {
        this.context = mContext;
        this.list = mLeftMenuItems;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_leftmenu_item, null);
            holder = new ViewHolder();

            holder.tv_left_menu = (TextView) convertView.findViewById(R.id.tv_left_menu);
            holder.tv_left_menu.setTypeface(FontFamily.setHelveticaTypeface(context));

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        LeftMenuModel model = list.get(position);

        holder.tv_left_menu.setText(model.getMenuTitle());

        return convertView;
    }

    private class ViewHolder {
        TextView tv_left_menu;
    }


}
