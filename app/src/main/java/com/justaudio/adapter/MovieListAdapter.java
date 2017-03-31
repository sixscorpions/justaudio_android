package com.justaudio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.justaudio.R;
import com.justaudio.activities.BaseActivity;
import com.justaudio.dto.MovieListModel;
import com.justaudio.utils.FontFamily;
import com.justaudio.utils.UILoader;
import com.justaudio.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Pavan
 */

public class MovieListAdapter extends BaseAdapter {

    private LayoutInflater vi;
    private BaseActivity mContext;
    private ArrayList<MovieListModel> dataList;
    private boolean isClicked = false;


    public MovieListAdapter(BaseActivity context, ArrayList<MovieListModel> results) {
        mContext = context;
        dataList = results;
        vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup view) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = vi.inflate(R.layout.row_movie_list_items, null);
            holder = new ViewHolder();

            holder.iv_movie = (ImageView) convertView.findViewById(R.id.iv_movie);
            holder.iv_movie_fev = (ImageView) convertView.findViewById(R.id.iv_movie_fev);
            holder.spinner = (ProgressBar) convertView.findViewById(R.id.progress);

            holder.tv_movie_name = (TextView) convertView.findViewById(R.id.tv_movie_name);
            holder.tv_movie_name.setTypeface(FontFamily.setHelveticaTypeface(mContext));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        MovieListModel model = dataList.get(position);

        holder.tv_movie_name.setText(model.getMovie_name());

        /*FEV. ICON*/
        if (model.isFav())
            holder.iv_movie_fev.setImageDrawable(Utils.getDrawable(mContext, R.drawable.icon_star_select));
        else
            holder.iv_movie_fev.setImageDrawable(Utils.getDrawable(mContext, R.drawable.icon_star_unselect));
        holder.iv_movie_fev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked) {
                    isClicked = false;
                    holder.iv_movie_fev.setImageDrawable(Utils.getDrawable
                            (mContext, R.drawable.icon_star_unselect));
                } else {
                    isClicked = true;
                    holder.iv_movie_fev.setImageDrawable(Utils.getDrawable
                            (mContext, R.drawable.icon_star_select));
                }
            }
        });


        UILoader.UILMoviePicLoading(holder.iv_movie, dataList.get(position).getMovie_thumbnail_image(),
                holder.spinner, R.drawable.ic_gallery_placeholder);

        return convertView;
    }


    private class ViewHolder {
        ImageView iv_movie;
        ImageView iv_movie_fev;
        TextView tv_movie_name;
        ProgressBar spinner;
    }

    public void updateAdapter(ArrayList<MovieListModel> result) {
        this.dataList = result;
        notifyDataSetChanged();
    }
}

