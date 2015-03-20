package com.src.adrien.testproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Adrien on 1/9/2015.
 */
public class MyListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListObject> objects;
    DisplayImageOptions options;

    public MyListAdapter(Context context) {
        this.context = context;
        objects = new ArrayList<ListObject>();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(null)
                .showImageForEmptyUri(null)
                .showImageOnFail(null)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(0))
                .build();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title_txt);
            holder.txtContent = (TextView) convertView.findViewById(R.id.content_txt);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress);
            holder.imageLayout = (FrameLayout) convertView.findViewById(R.id.image_layout);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ListObject object = objects.get(position);

        if (object.imageUrl != null && !object.imageUrl.isEmpty()) {
            ImageLoader.getInstance().displayImage(object.imageUrl, holder.imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.progressBar.setProgress(0);
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.imageLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.imageLayout.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.progressBar.setVisibility(View.GONE);
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                    holder.progressBar.setProgress(Math.round(100.0f * current / total));
                }
            });
        }

        holder.txtTitle.setText(object.title);
        holder.txtContent.setText(object.content);

        convertView.setTag(holder);
        return convertView;
    }

    public void setData(ArrayList<ListObject> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtContent;
        ProgressBar progressBar;
        FrameLayout imageLayout;
    }
}
