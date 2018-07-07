package mml.com.class_design.adapter.recyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import mml.com.class_design.Model.RecyclerViewItem;
import mml.com.class_design.R;
import mml.com.class_design.activity.recyclerView.RecyclerDragActivity;

/**
 * 项目名称：Class_Design
 * Created by Long on 2018/7/4.
 * 修改时间：2018/7/4 1:29
 */
public class RecyclerDragAdapter extends BaseAdapter<RecyclerDragAdapter.ViewHolder> {
    private List<RecyclerViewItem> dataList;
    public RecyclerDragAdapter(Context context,List<RecyclerViewItem> dataList) {

        super(context,dataList);
    }
    public RecyclerDragAdapter(Context context) {
        super(context);
    }
    @Override
    public void notifyDataSetChanged(List<RecyclerViewItem> dataList) {
        this.dataList=dataList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new  ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewItem item=dataList.get(position);
        if(item.getTitle()!="") {
            String title = item.getTitle();
            title=title.trim();
            holder.time.setText(item.getTime());
            holder.title.setText(title);
            holder.uuId.setText(item.getUuId());
        }
        else
        {
            holder.time.setText(item.getTime());
            holder.title.setText("图片便签");
            holder.uuId.setText(item.getUuId());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,time,uuId;
        ViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            time=itemView.findViewById(R.id.time);
            uuId=itemView.findViewById(R.id.uuId);
        }


    }

}

/**
 * 就是这个适配器的Item的Layout需要处理，其实和CardView的方式一模一样。
 */


