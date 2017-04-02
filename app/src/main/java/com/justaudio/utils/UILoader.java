package com.justaudio.utils;

import android.graphics.Bitmap;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import com.justaudio.R;
import com.justaudio.activities.HomeActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by ${VIDYA}
 */

public class UILoader {


    public static void UILMoviePicLoading(ImageView ivImageView, String ImageUrl, final ProgressBar progressBar, int placeholder) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeholder)
                .showImageForEmptyUri(placeholder)
                .showImageOnFail(placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        if (progressBar != null) {
            ImageLoader.getInstance().displayImage(ImageUrl, ivImageView, options, new SimpleImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }


            });
        } else {
            ImageLoader.getInstance().displayImage(ImageUrl, ivImageView, options);
        }
    }

    public static void UILCateListLoading(ImageView ivImageView, String ImageUrl,
                                          final ProgressBar progressBar, int placeholder) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(placeholder)
                .showImageOnLoading(placeholder)
                .showImageOnFail(placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        if (progressBar != null) {
            ImageLoader.getInstance().displayImage(ImageUrl, ivImageView, options, new SimpleImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }


            });
        } else {
            ImageLoader.getInstance().displayImage(ImageUrl, ivImageView, options);
        }
    }


    public static void UILPicLoading(ImageView ivImageView, String ImageUrl, final ProgressBar progressBar, int placeholder) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(placeholder)
                .showImageOnFail(placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        if (progressBar != null) {
            ImageLoader.getInstance().displayImage(ImageUrl, ivImageView, options, new SimpleImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }


            });
        } else {
            ImageLoader.getInstance().displayImage(ImageUrl, ivImageView, options);
        }
    }

    public static void UILDetailPicLoading(ImageView ivImageView, String ImageUrl, int placeholder,
                                           HomeActivity parent) {

        int screenWidth = Utils.getDeviceWidth(parent);
        android.support.design.widget.CollapsingToolbarLayout.LayoutParams layoutParams = new CollapsingToolbarLayout.
                LayoutParams(new ViewGroup.LayoutParams(100, 100));
        if (screenWidth <= 320) {
            layoutParams = new CollapsingToolbarLayout.LayoutParams(new ViewGroup.LayoutParams(screenWidth, 200));
        } else if (screenWidth > 320) {
            layoutParams = new CollapsingToolbarLayout.LayoutParams(new ViewGroup.LayoutParams(
                    screenWidth, screenWidth * 200 / 320));
        }
        ivImageView.setLayoutParams(layoutParams);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeholder)
                .showImageForEmptyUri(placeholder)
                .showImageOnFail(placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoader.getInstance().displayImage(ImageUrl, ivImageView, options);
    }

    public static void UILNotificationPicLoading(final RemoteViews ivImageView, final int resource, String ImageUrl) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().loadImage(ImageUrl, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                ivImageView.setImageViewResource(resource, R.drawable.icon_list_holder);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                ivImageView.setImageViewResource(resource, R.drawable.icon_list_holder);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                ivImageView.setImageViewBitmap(resource, bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                ivImageView.setImageViewResource(resource, R.drawable.icon_list_holder);
            }
        });
    }
}
