package net.manbucy.lockwallpaper.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.manbucy.lockwallpaper.R;
import net.manbucy.lockwallpaper.model.Image;

import java.util.List;

/**
 * ImageAdapter
 * Created by yang on 2017/8/3.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context mContext;
    private List<Image> imageList;
    private ImageOnTouchListener listener;

    public ImageAdapter(List<Image> imageList,ImageOnTouchListener listener) {
        this.imageList = imageList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                listener.onTouch(imageList.get(position),v.getX(),v.getY());
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image image = imageList.get(position);
        Glide.with(mContext).load(image.getImagePath()).into(holder.imageView);
        holder.title.setText(image.getTitle());
        holder.content.setText(image.getContent());
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView title;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            imageView = (ImageView) cardView.findViewById(R.id.image);
            title = (TextView) cardView.findViewById(R.id.image_title);
            content = (TextView) cardView.findViewById(R.id.image_content);
        }
    }
}
