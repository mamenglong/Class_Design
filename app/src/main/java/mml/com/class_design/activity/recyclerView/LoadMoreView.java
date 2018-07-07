package mml.com.class_design.activity.recyclerView;

/**
 * 项目名称：Class_Design
 * Created by Long on 2018/7/5.
 * 修改时间：2018/7/5 12:12
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.loading.LoadingView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import mml.com.class_design.R;

/**
 * 这是这个类的主角，如何自定义LoadMoreView。
 */
 public class LoadMoreView extends LinearLayout implements SwipeMenuRecyclerView.LoadMoreView, View.OnClickListener {

    private LoadingView mLoadingView;
    private TextView mTvMessage;

    private SwipeMenuRecyclerView.LoadMoreListener mLoadMoreListener;

    public LoadMoreView(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        setGravity(Gravity.CENTER);
        setVisibility(GONE);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int minHeight = (int) (displayMetrics.density * 60 + 0.5);
        setMinimumHeight(minHeight);

        inflate(context, R.layout.layout_fotter_loadmore, this);
        mLoadingView = (LoadingView) findViewById(R.id.loading_view);
        mTvMessage = (TextView) findViewById(R.id.tv_message);

        int color1 = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        int color2 = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
        int color3 = ContextCompat.getColor(getContext(), R.color.colorAccent);

        mLoadingView.setCircleColors(color1, color2, color3);
        setOnClickListener(this);
    }

    /**
     * 马上开始回调加载更多了，这里应该显示进度条。
     */
    @Override
    public void onLoading() {
        setVisibility(VISIBLE);
        mLoadingView.setVisibility(VISIBLE);
        mTvMessage.setVisibility(VISIBLE);
        mTvMessage.setText("正在努力加载，请稍后");
    }

    /**
     * 加载更多完成了。
     *
     * @param dataEmpty 是否请求到空数据。
     * @param hasMore   是否还有更多数据等待请求。
     */
    @Override
    public void onLoadFinish(boolean dataEmpty, boolean hasMore) {
        if (!hasMore) {
            setVisibility(VISIBLE);

            if (dataEmpty) {
                mLoadingView.setVisibility(GONE);
                mTvMessage.setVisibility(VISIBLE);
                mTvMessage.setText("暂时没有数据");
            } else {
                mLoadingView.setVisibility(GONE);
                mTvMessage.setVisibility(VISIBLE);
                mTvMessage.setText("没有更多数据啦");
            }
        } else {
            setVisibility(INVISIBLE);
        }
    }

    /**
     * 调用了setAutoLoadMore(false)后，在需要加载更多的时候，这个方法会被调用，并传入加载更多的listener。
     */
    @Override
    public void onWaitToLoadMore(SwipeMenuRecyclerView.LoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;

        setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mTvMessage.setVisibility(VISIBLE);
        mTvMessage.setText("点我加载更多");
    }

    /**
     * 加载出错啦，下面的错误码和错误信息二选一。
     *
     * @param errorCode    错误码。
     * @param errorMessage 错误信息。
     */
    @Override
    public void onLoadError(int errorCode, String errorMessage) {
        setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mTvMessage.setVisibility(VISIBLE);

        // 这里要不直接设置错误信息，要不根据errorCode动态设置错误数据。
        mTvMessage.setText(errorMessage);
    }

    /**
     * 非自动加载更多时mLoadMoreListener才不为空。
     */
    @Override
    public void onClick(View v) {
        if (mLoadMoreListener != null) mLoadMoreListener.onLoadMore();
    }
}
