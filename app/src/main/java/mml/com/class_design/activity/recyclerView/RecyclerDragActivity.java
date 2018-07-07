/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mml.com.class_design.activity.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.yanzhenjie.loading.LoadingView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMovementListener;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mml.com.class_design.Model.RecyclerViewItem;
import mml.com.class_design.R;

import mml.com.class_design.activity.createNew.MainActivity;
import mml.com.class_design.adapter.recyclerView.BaseAdapter;
import mml.com.class_design.adapter.recyclerView.RecyclerDragAdapter;
import mml.com.class_design.application.MyApplication;
import mml.com.class_design.common.Common;
import mml.com.class_design.utils.LogUtils;
import mml.com.class_design.utils.SPUtils;

/**
 * <p>
 * 自定义拖拽规则的。
 * </p>
 * Created by Yan Zhenjie on 2016/8/3.
 */
public class RecyclerDragActivity extends BaseDragActivity {
    private DrawerLayout drawerLayout;//侧滑根标签
    private SwipeRefreshLayout mRefreshLayout;//刷新
    @Override
    protected int getContentView() {
        return  R.layout.activity_recycler_drag;
    }
   // 将字符串转成长整型数组
    public static long[] StringtoLong(String str, String regex) {
        if(str.length()!=0||!str.equals("")) {
            String strs[] = str.split(regex);
            long array[] = new long[strs.length];
            for (int i = 0; i < strs.length; i++) {
                array[i] = Long.parseLong(strs[i]);
            }
            return array;
        }
       else
           return new long[]{};
    }
    @Override
    protected BaseAdapter createAdapter() {
        return new RecyclerDragAdapter(this);
    }
/*
双击退出
 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(doubleClick(keyCode,event,drawerLayout))
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.getDatabase();
        MyApplication.getApplication().setRecyclerDragActivity(this);
        View header = getLayoutInflater().inflate(R.layout.layout_header_switch, mRecyclerView, false);
        mRecyclerView.addHeaderView(header);

        SwitchCompat switchCompat = (SwitchCompat) header.findViewById(R.id.switch_compat);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 控制是否可以侧滑删除。
                mRecyclerView.setItemViewSwipeEnabled(isChecked);
            }
        });
        switchCompat.setChecked(true);
        mRecyclerView.setLongPressDragEnabled(true); // 长按拖拽，默认关闭。
        mRecyclerView.setItemViewSwipeEnabled(true); // 滑动删除，默认关闭。

        // 自定义拖拽控制参数。
        mRecyclerView.setOnItemMovementListener(mItemMovementListener);
        //刷新
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(mRefreshListener); // 刷新监听。
        // 自定义的核心就是LoadMoreView类。
        LoadMoreView loadMoreView = new LoadMoreView(this);
        mRecyclerView.addFooterView(loadMoreView); // 添加为Footer。
        mRecyclerView.setLoadMoreView(loadMoreView); // 设置LoadMoreView更新监听。
        mRecyclerView.setLoadMoreListener(mLoadMoreListener); // 加载更多的监听。
        loadData(0);
        //侧滑设置
        drawerLayout=findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        //侧滑布局
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);//侧栏
        setNavigationItemSelected(navView);

        //应用内悬浮窗
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "新建", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createNew();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Create_New:
                navItemId=R.id.nav_list;
                loadData(0);
//                if(resultCode==101) {
//                    loadData(0);
////                    String uuId = data.getStringExtra("uuId");
////                    LogUtils.i("uuId",uuId);
////                    RecyclerViewItem item= LitePal.where("uuId=?",uuId).findFirst(RecyclerViewItem.class);
////                    mDataList.add(0,item);
////                    mAdapter.notifyDataSetChanged(mDataList);
//                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Item移动参数回调监听，这里自定义Item怎样移动。
     */
    private OnItemMovementListener mItemMovementListener = new OnItemMovementListener() {
        @Override
        public int onDragFlags(RecyclerView recyclerView, RecyclerView.ViewHolder targetViewHolder) {
            int adapterPosition = targetViewHolder.getAdapterPosition();
            if (adapterPosition == 0) { // 这里让HeaderView不能拖拽。
                return OnItemMovementListener.INVALID;// 返回无效的方向。
            }
            // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
            int position = adapterPosition - mRecyclerView.getHeaderItemCount();

            // 假如让普通Item的第一个不能拖拽。
            if (position == 0) {
                return OnItemMovementListener.INVALID;// 返回无效的方向。
            }

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                // Grid可以上下左右拖拽。
                return OnItemMovementListener.LEFT |
                        OnItemMovementListener.UP |
                        OnItemMovementListener.RIGHT |
                        OnItemMovementListener.DOWN;
            } else if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

                // 横向List只能左右拖拽。
                if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    return (OnItemMovementListener.LEFT | OnItemMovementListener.RIGHT);
                }
                // 竖向List只能上下拖拽。
                else {
                    return OnItemMovementListener.UP | OnItemMovementListener.DOWN;
                }
            }
            return OnItemMovementListener.INVALID;// 返回无效的方向。
        }

        @Override
        public int onSwipeFlags(RecyclerView recyclerView, RecyclerView.ViewHolder targetViewHolder) {
            int adapterPosition = targetViewHolder.getAdapterPosition();
            if (adapterPosition == 0) { // 这里让HeaderView不能侧滑删除。
                return OnItemMovementListener.INVALID;// 返回无效的方向。
            }

            // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
            int position = adapterPosition - mRecyclerView.getHeaderItemCount();

            // 假如让普通Item的第一个不能侧滑删除。
            if (position == 0) {
                return OnItemMovementListener.INVALID;// 返回无效的方向。
            }

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                // 横向Grid上下侧滑。
                if (manager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    return ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                }
                // 竖向Grid左右侧滑。
                else {
                    return ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                }
            } else if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                // 横向List上下侧滑。
                if (manager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    return OnItemMovementListener.UP | OnItemMovementListener.DOWN;
                }
                // 竖向List左右侧滑。
                else {
                    return OnItemMovementListener.LEFT | OnItemMovementListener.RIGHT;
                }
            }
            return OnItemMovementListener.INVALID;// 其它均返回无效的方向。
        }
    };


    @Override
    protected OnItemMoveListener getItemMoveListener() {
        return new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                // TODO 如何让不同的ViewType之间可以拖拽，就是去掉这个判断。
              //  if (srcHolder.getItemViewType() != targetHolder.getItemViewType()) return false;

                // 添加了HeadView时，通过ViewHolder拿到的position都需要减掉HeadView的数量。
                int fromPosition = srcHolder.getAdapterPosition() - mRecyclerView.getHeaderItemCount();
                int toPosition = targetHolder.getAdapterPosition() - mRecyclerView.getHeaderItemCount();

                if (toPosition == 0) {// 保证第一个不被挤走。
                    return false;
                }
                if (fromPosition < toPosition)
                    for (int i = fromPosition; i < toPosition; i++)
                        Collections.swap(mDataList, i, i + 1);
                else
                    for (int i = fromPosition; i > toPosition; i--)
                        Collections.swap(mDataList, i, i - 1);

                mAdapter.notifyItemMoved(fromPosition, toPosition);

                return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
            }

            @Override
            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
                int position = srcHolder.getAdapterPosition() - mRecyclerView.getHeaderItemCount();

                remove(position);

                // mDataList.remove(position);
                //mAdapter.notifyItemRemoved(position);

            }
        };
    }
    /**
     * 加载toolbarmenu
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    /**
     * 加载toolbarmenu菜单按钮点击事件
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.add:
               createNew();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    /**设置NavigationItem的点击事件
     * */
    private int navItemId;
    public void setNavigationItemSelected(NavigationView navView){
        navView.setCheckedItem(R.id.nav_list);
        navItemId=R.id.nav_list;
        //点击邮箱发邮件
        View headerView=navView.inflateHeaderView(R.layout.nav_header);
        LinearLayout linearLayout=headerView.findViewById(R.id.icon_mail);
        TextView mail=headerView.findViewById(R.id.mail);
        mail.setText(SPUtils.getInstance().getString("userName")!=""?SPUtils.getInstance().getString("userName"):"未登录");

//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Intent.ACTION_SENDTO);
//                it.putExtra(Intent.EXTRA_EMAIL, getResources().getString(R.string.nav_header_email));
//                //   it.setType("text/plain");
//                startActivity(Intent.createChooser(it, "Choose Email Client"));
//            }
//        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_list:
                        navItemId=R.id.nav_list;
                        Toast.makeText(RecyclerDragActivity.this,"nav_list",Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case R.id.nav_new:
                        navItemId=R.id.nav_new;
                        createNew();
//                        Toast.makeText(RecyclerDragActivity.this,"nav_new",Toast.LENGTH_SHORT)
//                                .show();
                        break;
                    case R.id.nav_backup:
                        navItemId=R.id.nav_backup;
                        Toast.makeText(RecyclerDragActivity.this,"nav_backup",Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case R.id.nav_reGet:
                        navItemId=R.id.nav_reGet;
                        shortToast("nav_reGet");
                        break;
                    case R.id.nav_donate:
                        Common.openALiPay(RecyclerDragActivity.this);
                        break;
                    case R.id.contactMe:
                        Common.contactMe();
                        break;
                    case R.id.about:
                        //   Common.showNoticeDialog(MainActivity.this);
                        //虽然这里的参数是AlertDialog.Builder(Context context)但我们不能使用getApplicationContext()获得的Context,而必须使用Activity.this,因为只有一个Activity才能添加一个窗体。
                        break;
                    default:
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }


    /**
     * 刷新。
     */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData(0);
                }
            }, 1000); // 延时模拟请求服务器。
        }
    };

    /**
     * 加载更多。
     */
    private SwipeMenuRecyclerView.LoadMoreListener mLoadMoreListener = new SwipeMenuRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData(mDataList.size());
                    // 如果加载失败调用下面的方法，传入errorCode和errorMessage。
                    // errorCode随便传，你自定义LoadMoreView时可以根据errorCode判断错误类型。
                    // errorMessage是会显示到loadMoreView上的，用户可以看到。
                    // mRecyclerView.loadMoreError(0, "请求网络失败");
                }
            }, 1000);
        }
    };


    /**
     * 第一次加载数据。
     */
    private void   loadData(int offset) {
        if (offset==0)
            mDataList.clear();
        List<RecyclerViewItem> itemList=new ArrayList<>();
        Cursor cursor=LitePal.findBySQL("select distinct  uuId from RecyclerViewItem order by time DESC limit 20 offset "+offset);
        while (cursor.moveToNext()){
            RecyclerViewItem item= LitePal.where("uuId=?",cursor.getString(0)).findFirst(RecyclerViewItem.class);
            itemList.add(item);
        }

        if(itemList.size()< 20)
            mRecyclerView.loadMoreFinish(false, false);
        else
            mRecyclerView.loadMoreFinish(false, true);
        // 第一次加载数据：一定要掉用这个方法。
        // 第一个参数：表示此次数据是否为空，假如你请求到的list为空(== null || list.size == 0)，那么这里就要true。
        // 第二个参数：表示是否还有更多数据，根据服务器返回给你的page等信息判断是否还有更多，这样可以提供性能，如果不能判断则传true。
        mDataList.addAll(itemList);
        mAdapter.notifyDataSetChanged(mDataList);
        // notifyItemRangeInserted()或者notifyDataSetChanged().
        mAdapter.notifyItemRangeInserted(mDataList.size() - itemList.size(), itemList.size());
        mRefreshLayout.setRefreshing(false);
    }
}