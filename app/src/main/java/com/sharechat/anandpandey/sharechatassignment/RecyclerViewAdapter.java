package com.sharechat.anandpandey.sharechatassignment;

import com.sharechat.anandpandey.sharechatassignment.ImageLoadTask;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemHolder> {

    private List<JSONObject> itemsName;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;

    public RecyclerViewAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        itemsName = new ArrayList<JSONObject>();
    }

    @Override
    public RecyclerViewAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.layout_item, parent, false);
        return new ItemHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ItemHolder holder, int position) {
        holder.setItemName(itemsName.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsName.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener(){
        return onItemClickListener;
    }

    public interface OnItemClickListener{
        public void onItemClick(ItemHolder item, int position);
    }

    public void add(int location, JSONObject data){
        itemsName.add(location, data);
        notifyItemInserted(location);
    }

    public void remove(int location){
        if(location >= itemsName.size())
            return;

        itemsName.remove(location);
        notifyItemRemoved(location);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RecyclerViewAdapter parent;
        LinearLayout linearLayout;

        public ItemHolder(View itemView, RecyclerViewAdapter parent) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.parent = parent;
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }

        public void setItemName(JSONObject jsonData){
            try {
                GridLayout userView = (GridLayout) linearLayout.findViewById(R.id.user_view);
                Button button = (Button) linearLayout.findViewById(R.id.load_more);
                LinearLayout imageView = (LinearLayout) linearLayout.findViewById(R.id.image_view);
                if (jsonData.get("type").equals("profile")) {
                    ((ViewManager) button.getParent()).removeView(button);
                    ((ViewManager) imageView.getParent()).removeView(imageView);
                    TextView textView = (TextView) ((LinearLayout)userView.findViewById(R.id.user_details)).findViewById(R.id.profile_name);
                    textView.setText("Author Name : " + jsonData.get("author_name").toString());
                    TextView contactTextView = (TextView) ((LinearLayout)userView.findViewById(R.id.user_details)).findViewById(R.id.author_contact);
                    contactTextView.setText("Author Contact : " + jsonData.get("author_contact").toString());
                    TextView dobTextView = (TextView) userView.findViewById(R.id.user_details).findViewById(R.id.author_dob);
                    dobTextView.setText("Author Dob : " + jsonData.get("author_dob").toString());
                    TextView statusTextView = (TextView) userView.findViewById(R.id.user_details).findViewById(R.id.author_status);
                    statusTextView.setText("Author Status : " + jsonData.get("author_status").toString());
                    TextView genderTextView = (TextView) userView.findViewById(R.id.user_details).findViewById(R.id.author_gender);
                    genderTextView.setText("Author Gender : " + jsonData.get("author_gender").toString());
                    new ImageLoadTask(jsonData.get("profile_url").toString(), (ImageView) userView.findViewById(R.id.profile_image), "profile_image").execute();
                } else if (jsonData.get("type").equals("image")) {
                    TextView textView = (TextView) imageView.findViewById(R.id.image_author_name);
                    textView.setText("Author Name : " + jsonData.get("author_name").toString());
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    TextView dateView = (TextView) imageView.findViewById(R.id.image_created_time);
                    dateView.setText(format1.format((Long) jsonData.get("postedOn")).toString());
                    int width = imageView.getWidth();
                    new ImageLoadTask(jsonData.get("url").toString(), (ImageView) imageView.findViewById(R.id.image), "image").execute();
                    ((ViewManager) button.getParent()).removeView(button);
                    ((ViewManager) userView.getParent()).removeView(userView);
                } else {
                    button.setText("Load More");
                    ((ViewManager) userView.getParent()).removeView(userView);
                    ((ViewManager) imageView.getParent()).removeView(imageView);
                }
            } catch (Exception e) {
            }
        }

        public CharSequence getItemName(){
            return null;
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if(listener != null){
                listener.onItemClick(this, getPosition());
            }
        }
    }
}
