package mml.com.class_design.Model;

import android.graphics.Bitmap;

import org.litepal.crud.LitePalSupport;

import java.util.List;
import java.util.UUID;

/**
 * 项目名称：Class_Design
 * Created by Long on 2018/7/3.
 * 修改时间：2018/7/3 0:03
 */
public class RecyclerViewItem  extends LitePalSupport {
    private String title;
    private String time;
    private String bitmapPath;
    private String uuId;
    public String getTitle() {
        return title!=null?title:"";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time!=null?time:"";
    }

    public void setTime(String time) {
        this.time = time;
    }
    public RecyclerViewItem(String title,String time){
        this.time=time;
        this.title=title;
    }
    public RecyclerViewItem(String title,String time,String uuId){
        this.time=time;
        this.title=title;
        this.uuId=uuId;
    }
    public RecyclerViewItem(){}

    public String getUuId() {
        return uuId!=null?uuId:"";
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
        if(uuId=="")
            this.uuId=UUID.randomUUID().toString();
    }

    public String getBitmapPath() {
        return bitmapPath!=null?bitmapPath:"";
    }

    public void setBitmapPath(String bitmapPath) {
        this.bitmapPath = bitmapPath;
    }
}
