/*
 * Copyright 2017 Yan Zhenjie
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

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import mml.com.class_design.Model.RecyclerViewItem;
import mml.com.class_design.R;
import mml.com.class_design.adapter.recyclerView.BaseAdapter;
import mml.com.class_design.adapter.recyclerView.MainAdapter;
import mml.com.class_design.utils.LogUtils;

/**
 * Created by YanZhenjie on 2017/7/21.
 */
public class BaseActivity extends mml.com.class_design.activity.BaseActivity implements SwipeItemClickListener {

    protected Toolbar mToolbar;
    protected AppBarLayout mAppBarLayout;
    protected ActionBar mActionBar;
    protected SwipeMenuRecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;

    protected BaseAdapter mAdapter;
    protected List<RecyclerViewItem> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mAppBarLayout=findViewById(R.id.appBar);

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (displayHomeAsUpEnabled()) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

//        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
//            @Override
//            public void onStateChanged(AppBarLayout appBarLayout, State state) {
//                LogUtils.i("STATE", state.name());
//                if( state == State.EXPANDED ) {
//
//                    //展开状态
//
//                }else if(state == State.COLLAPSED){
//
//                    //折叠状态
//
//                }else {
//
//                    //中间状态
//
//                }
//            }
//        });
        mLayoutManager = createLayoutManager();
        mItemDecoration = createItemDecoration();
        mDataList = createDataList();
        mAdapter = createAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setSwipeItemClickListener(this);
    }
    public  int getmDataListSize(){
        return mDataList.size();
    }
    protected int getContentView() {
        return R.layout.activity_recycler_base;
    }

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected RecyclerView.ItemDecoration createItemDecoration() {
        return new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color));
    }

    protected List<RecyclerViewItem> createDataList() {
       List<RecyclerViewItem> dataList = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            dataList.add(new RecyclerViewItem("第"+i+"个item","2018-1-1"));
//        }
      return dataList;
    }

    protected BaseAdapter createAdapter() {
        return new MainAdapter(this);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Toast.makeText(this, "第" + position + "个", Toast.LENGTH_SHORT).show();
    }

    protected boolean displayHomeAsUpEnabled() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public abstract static class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

        public enum State {
            EXPANDED,
            COLLAPSED,
            IDLE
        }

        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    }

}