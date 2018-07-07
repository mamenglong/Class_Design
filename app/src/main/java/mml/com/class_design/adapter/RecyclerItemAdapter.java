package mml.com.class_design.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mml.com.class_design.Model.RecyclerViewItem;
import mml.com.class_design.R;

/**
 * 项目名称：Class_Design
 * Created by Long on 2018/7/2.
 * 修改时间：2018/7/2 23:57
 */
public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerItemAdapter.ViewHolder> {
    private List<RecyclerViewItem> list;
    private Context mContext;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewItem item=list.get(position);
        holder.time.setText(item.getTime());
        holder.title.setText(item.getTitle());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public RecyclerItemAdapter(List<RecyclerViewItem> items){
        list=items;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,time;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            time = (TextView) view.findViewById(R.id.time);

        }


    }
}
