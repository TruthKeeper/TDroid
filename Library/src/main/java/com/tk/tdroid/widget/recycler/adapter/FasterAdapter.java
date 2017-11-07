package com.tk.tdroid.widget.recycler.adapter;

import android.content.res.Resources;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tk.tdroid.utils.CollectionUtils;
import com.tk.tdroid.utils.EmptyUtils;
import com.tk.tdroid.utils.ViewUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/6
 *      desc : <ul>
 *          <li>支持占位(空、错误)视图</li>
 *          <li>支持追加(头部、足部)视图</li>
 *          <li>支持占位视图和追加视图的优先级调整(例如无数据时不显示头部、底部)</li>
 *          <li>支持上拉加载视图以及监听、NestedScroll场景下的配置</li>
 *          <li>支持上拉加载内置异常处理：点击LoadMoreView重试，回调onLoad方法</li>
 *          <li>支持数据类型与布局类型，一对多，多对多的方式</li>
 *          <li>封装通用的点击、长按事件</li>
 *          <li>封装常用的集合 API</li>
 *          <li>封装常用的ViewHolder API</li>
 *          <li>类似选中业务场景下，封装SparseArray记录对FasterHolder的数据保存</li>
 *          <li>final 不允许重写，暂不支持Adapter变长模式</li>
 *          </ul>
 * </pre>
 */

public final class FasterAdapter<T> extends RecyclerView.Adapter<FasterHolder> {
    private static final String TAG = "FasterAdapter";
    /**
     * 开发时注意不要占用
     */
    public static final int TYPE_EMPTY = -10000;
    public static final int TYPE_ERROR = -10001;
    public static final int TYPE_HEADER = -10002;
    public static final int TYPE_FOOTER = -10003;
    public static final int TYPE_LOAD = -10004;
    public static final int[] INNER_TYPE = new int[]{TYPE_EMPTY, TYPE_ERROR, TYPE_HEADER, TYPE_FOOTER, TYPE_LOAD};

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Status.LOAD_IDLE, Status.LOAD_ERROR, Status.LOAD_END, Status.LOAD_ING})
    public @interface Status {
        /**
         * 待命
         */
        int LOAD_IDLE = 0x00;
        /**
         * 加载失败
         */
        int LOAD_ERROR = 0x01;
        /**
         * 加载完毕
         */
        int LOAD_END = 0x02;
        /**
         * 加载中
         */
        int LOAD_ING = 0x03;
    }

    /**
     * 上拉加载的状态
     */
    private int mLoadStatus = Status.LOAD_IDLE;

    private OnItemClickListener mOnItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;
    private OnLoadListener mOnLoadListener = null;

    private FrameLayout mEmptyContainer = null;
    private FrameLayout mErrorContainer = null;
    private LinearLayout mHeaderContainer = null;
    private LinearLayout mFooterContainer = null;
    private FrameLayout mLoadMoreContainer = null;
    /**
     * ILoadMore接口
     */
    private ILoadMore mILoadMore = null;
    /**
     * 空视图的包裹容器高度是否是{@link ViewGroup.LayoutParams#MATCH_PARENT},充满RecyclerView
     * <br>
     * 一般不用对其设置，只有在有头、足视图时需要注意
     */
    private boolean isEmptyMatchParent;
    /**
     * 错误视图的包裹容器高度是否是{@link ViewGroup.LayoutParams#MATCH_PARENT},充满RecyclerView
     * <br>
     * 一般不用对其设置，只有在有头、足视图时需要注意
     */
    private boolean isErrorMatchParent;
    /**
     * 是否启用空视图
     */
    private boolean isEmptyEnabled;
    /**
     * 头、足视图优先级是否大于占位图（空视图、错误视图）,true && 无数据时既显示头、足又显示空视图
     */
    private boolean headerFooterFront;

    /**
     * 是否启用上拉加载
     */
    private boolean isLoadMoreEnabled = false;
    /**
     * 数据封装集合
     */
    private List<Entry<T>> mList = null;
    /**
     * 数据类型 - 视图策略的绑定关系
     */
    private ArrayMap<Class<?>, Strategy<?>> mBindMap;
    /**
     * 是否显示错误视图，未设置errorView时无效
     */
    private boolean isDisplayError = false;
    /**
     * 存放对FasterHolder额外数据保存的Array
     */
    private LongSparseArray<Object> array = null;
    /**
     * 上拉加载的滚动监听
     */
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
            if (null == mLayoutManager) {
                return;
            }
            int lastVisibleItemPosition = 0;
            int firstCompletelyVisibleItemPosition = 0;
            if (mLayoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager manager = (LinearLayoutManager) mLayoutManager;
                lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                firstCompletelyVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();

            } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) mLayoutManager;
                int[] pos = new int[manager.getSpanCount()];
                manager.findLastVisibleItemPositions(pos);
                Arrays.sort(pos);
                lastVisibleItemPosition = pos[manager.getSpanCount() - 1];
                manager.findFirstCompletelyVisibleItemPositions(pos);
                Arrays.sort(pos);
                firstCompletelyVisibleItemPosition = pos[0];
            }
            /*
             * 满足条件
             */
            if (SCROLL_STATE_IDLE == newState
                    && 1 == getLoadMoreViewSpace()
                    && 0 == Math.max(getEmptyViewSpace(), getErrorViewSpace())
                    && (mLoadStatus == Status.LOAD_IDLE)
                    && (lastVisibleItemPosition == getItemCount() - 1
                    && 0 != firstCompletelyVisibleItemPosition)) {
                //开始加载
                mLoadStatus = Status.LOAD_ING;
                mLoadMoreContainer.setVisibility(VISIBLE);
                mILoadMore.onShow();
                if (null != mOnLoadListener) {
                    mOnLoadListener.onLoad();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
    /**
     * 布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    private FasterAdapter(Builder<T> builder) {
        mOnItemClickListener = builder.itemClickListener;
        mOnItemLongClickListener = builder.itemLongClickListener;
        mOnLoadListener = builder.loadListener;
        isEmptyMatchParent = builder.emptyMatchParent;
        isErrorMatchParent = builder.errorMatchParent;
        if (null != builder.emptyView) {
            mEmptyContainer = new FrameLayout(builder.emptyView.getContext());
            mEmptyContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, isEmptyMatchParent ? MATCH_PARENT : WRAP_CONTENT));
            safeAddView(mEmptyContainer, builder.emptyView);
        }
        if (null != builder.errorView) {
            mErrorContainer = new FrameLayout(builder.errorView.getContext());
            mErrorContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, isErrorMatchParent ? MATCH_PARENT : WRAP_CONTENT));
            safeAddView(mErrorContainer, builder.errorView);
        }
        if (null != builder.headerViews && (!builder.headerViews.isEmpty())) {
            mHeaderContainer = new LinearLayout(builder.headerViews.get(0).getContext());
            mHeaderContainer.setOrientation(LinearLayout.VERTICAL);
            mHeaderContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            for (View child : builder.headerViews) {
                safeAddView(mHeaderContainer, child);
            }
        }
        if (null != builder.footerViews && (!builder.footerViews.isEmpty())) {
            mFooterContainer = new LinearLayout(builder.footerViews.get(0).getContext());
            mFooterContainer.setOrientation(LinearLayout.VERTICAL);
            mFooterContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            for (View child : builder.footerViews) {
                safeAddView(mFooterContainer, child);
            }
        }
        if (null != builder.loadMoreView) {
            mLoadMoreContainer = new FrameLayout(builder.loadMoreView.getContext());
            mLoadMoreContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            safeAddView(mLoadMoreContainer, builder.loadMoreView);
            mLoadMoreContainer.setVisibility(View.GONE);
            builder.loadMoreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMoreWhileFailure();
                }
            });
            mILoadMore = (ILoadMore) builder.loadMoreView;
        }

        isEmptyEnabled = builder.emptyEnabled;
        headerFooterFront = builder.headerFooterFront;
        isLoadMoreEnabled = builder.loadMoreEnabled;
        if (null == builder.list) {
            mList = new ArrayList<>();
        } else {
            mList = builder.list;
        }
        mBindMap = builder.bindMap;
        array = new LongSparseArray<>(2);
    }

    private void safeAddView(ViewGroup viewGroup, View child) {
        safeAddView(viewGroup, child, -1);
    }

    /**
     * 安全添加，防止Margin丢失
     *
     * @param viewGroup
     * @param child
     * @param index
     */
    private void safeAddView(ViewGroup viewGroup, View child, int index) {
        if (child.getLayoutParams() != null) {
            if (child.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = null;
                ViewGroup.MarginLayoutParams childP = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                if (viewGroup instanceof LinearLayout) {
                    params = new LinearLayout.LayoutParams(childP.width, childP.height);
                } else if (viewGroup instanceof FrameLayout) {
                    params = new FrameLayout.LayoutParams(childP.width, childP.height);
                } else {
                    params = new ViewGroup.MarginLayoutParams(childP.width, childP.height);
                }
                params.setMargins(childP.leftMargin, childP.topMargin, childP.rightMargin, childP.bottomMargin);
                viewGroup.addView(child, index, params);
            } else {
                viewGroup.addView(child, index);
            }
        } else {
            viewGroup.addView(child, index);
        }
    }

    /**
     * 设置空视图（占位视图）
     *
     * @param emptyView
     */
    public void setEmptyView(@Nullable View emptyView) {
        //当前是否需要显示空视图
        boolean needShowEmpty = isEmptyEnabled && (!isDisplayError) && EmptyUtils.isEmpty(mList);
        if (null == emptyView) {
            //移除空视图操作
            if (!ViewUtils.isEmpty(mEmptyContainer)) {
                mEmptyContainer.removeAllViews();
                if (needShowEmpty) {
                    //立即刷新
                    notifyDataSetChanged();
                }
            }
        } else {
            if (null == mEmptyContainer) {
                //添加
                mEmptyContainer = new FrameLayout(emptyView.getContext());
                mEmptyContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, isEmptyMatchParent ? MATCH_PARENT : WRAP_CONTENT));
                safeAddView(mEmptyContainer, emptyView);
                if (needShowEmpty) {
                    notifyDataSetChanged();
                }
            } else {
                mEmptyContainer.removeAllViews();
                safeAddView(mEmptyContainer, emptyView);
            }
        }
    }

    /**
     * 启用空视图（占位视图）
     *
     * @param emptyEnabled
     */
    public void setEmptyEnabled(boolean emptyEnabled) {
        if (this.isEmptyEnabled != emptyEnabled) {
            //当前是否已经显示空视图
            boolean emptyShow = 1 == getEmptyViewSpace();
            isEmptyEnabled = emptyEnabled;
            if (emptyShow) {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 是否启用空视图
     *
     * @return
     */
    public boolean isEmptyEnabled() {
        return isEmptyEnabled;
    }

    /**
     * 设置错误视图（占位视图）
     *
     * @param errorView
     */
    public void setErrorView(@Nullable View errorView) {
        if (null == errorView) {
            //移除错误视图
            if (!ViewUtils.isEmpty(mErrorContainer)) {
                mErrorContainer.removeAllViews();
                if (isDisplayError) {
                    isDisplayError = false;
                    notifyDataSetChanged();
                }
            }
        } else {
            if (null == mErrorContainer) {
                mErrorContainer = new FrameLayout(errorView.getContext());
                mErrorContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, isErrorMatchParent ? MATCH_PARENT : WRAP_CONTENT));
                safeAddView(mErrorContainer, errorView);
                if (isDisplayError) {
                    notifyDataSetChanged();
                }
            } else {
                mErrorContainer.removeAllViews();
                safeAddView(mErrorContainer, errorView);
            }
        }
    }

    /**
     * 显示错误视图(占位视图)，立即生效
     *
     * @param displayError
     */
    public void setDisplayError(boolean displayError) {
        if (this.isDisplayError != displayError) {
            if (ViewUtils.isEmpty(mErrorContainer)) {
                return;
            }
            isDisplayError = displayError;
            notifyDataSetChanged();
        }
    }

    /**
     * 是否显示错误视图
     *
     * @return
     */
    public boolean isDisplayError() {
        return isDisplayError;
    }

    /**
     * 设置占位视图优先级
     *
     * @param headerFooterFront
     */
    public void setHeaderFooterFront(boolean headerFooterFront) {
        if (this.headerFooterFront != headerFooterFront) {
            this.headerFooterFront = headerFooterFront;
            if (1 == Math.max(getHeaderViewSpace(), getFooterViewSpace())
                    && 1 == Math.max(getEmptyViewSpace(), getErrorViewSpace())) {
                //刷新
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 获取占位视图的优先级
     *
     * @return
     */
    public boolean isHeaderFooterFront() {
        return headerFooterFront;
    }

    /**
     * 添加头视图
     *
     * @param headerView
     */
    public void addHeaderView(@NonNull View headerView) {
        addHeaderView(0 == getHeaderViewSpace() ? 0 : mHeaderContainer.getChildCount() - 1, headerView);
    }

    /**
     * 添加头视图
     *
     * @param index
     * @param headerView
     */
    public void addHeaderView(int index, @NonNull View headerView) {
        boolean init = 0 == getHeaderViewSpace();
        boolean emptyShow = 1 == getEmptyViewSpace();
        boolean errorShow = 1 == getErrorViewSpace();
        if (null == mHeaderContainer) {
            mHeaderContainer = new LinearLayout(headerView.getContext());
            mHeaderContainer.setOrientation(LinearLayout.VERTICAL);
            mHeaderContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        safeAddView(mHeaderContainer, headerView, index);
        if (init) {
            if (emptyShow || errorShow) {
                if (headerFooterFront) {
                    notifyItemInserted(0);
                } else {
                    //避免占位视图ItemAnimator
                    notifyDataSetChanged();
                }
            } else {
                notifyItemInserted(0);
            }
        }
    }

    /**
     * 移除头视图
     *
     * @param headerView
     */
    public void removeHeaderView(@NonNull View headerView) {
        if (0 == getHeaderViewSpace()) {
            return;
        }
        removeHeaderView(mHeaderContainer.indexOfChild(headerView));
    }

    /**
     * 移除头视图
     *
     * @param index
     */
    public void removeHeaderView(int index) {
        if (0 > index || 0 == getHeaderViewSpace()) {
            return;
        }
        boolean emptyShow = 1 == getEmptyViewSpace();
        boolean errorShow = 1 == getErrorViewSpace();
        mHeaderContainer.removeViewAt(index);
        if (0 == mHeaderContainer.getChildCount()) {
            if (emptyShow || errorShow) {
                if (headerFooterFront) {
                    notifyItemRemoved(0);
                }
            } else {
                notifyItemRemoved(0);
            }
        }
    }

    /**
     * 移除所有头视图
     */
    public void removeAllHeaderView() {
        if (0 == getHeaderViewSpace()) {
            return;
        }
        boolean emptyShow = 1 == getEmptyViewSpace();
        boolean errorShow = 1 == getErrorViewSpace();
        mHeaderContainer.removeAllViews();
        if (emptyShow || errorShow) {
            if (headerFooterFront) {
                notifyItemRemoved(0);
            }
        } else {
            notifyItemRemoved(0);
        }
    }

    /**
     * 获取头视图数量
     *
     * @return
     */
    public int getHeaderViewSize() {
        if (1 == getHeaderViewSpace()) {
            return mHeaderContainer.getChildCount();
        }
        return 0;
    }

    /**
     * 添加足视图
     *
     * @param footerView
     */
    public void addFooterView(@NonNull View footerView) {
        addFooterView(0 == getFooterViewSpace() ? 0 : mFooterContainer.getChildCount() - 1, footerView);
    }

    /**
     * 添加足视图
     *
     * @param index
     * @param footerView
     */
    public void addFooterView(int index, @NonNull View footerView) {
        boolean init = 0 == getFooterViewSpace();
        boolean emptyShow = 1 == getEmptyViewSpace();
        boolean errorShow = 1 == getErrorViewSpace();
        if (null == mFooterContainer) {
            mFooterContainer = new LinearLayout(footerView.getContext());
            mFooterContainer.setOrientation(LinearLayout.VERTICAL);
            mFooterContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        safeAddView(mFooterContainer, footerView, index);
        if (init) {
            if (emptyShow || errorShow) {
                if (headerFooterFront) {
                    notifyItemInserted(getHeaderViewSpace() + 1);
                }
            } else {
                notifyItemInserted(getHeaderViewSpace() + mList.size());
            }
        }
    }

    /**
     * 移除足视图
     *
     * @param footerView
     */
    public void removeFooterView(@NonNull View footerView) {
        if (0 == getFooterViewSpace()) {
            return;
        }
        removeFooterView(mFooterContainer.indexOfChild(footerView));
    }

    /**
     * 移除足视图
     *
     * @param index
     */
    public void removeFooterView(int index) {
        if (0 > index || 0 == getFooterViewSpace()) {
            return;
        }
        boolean emptyShow = 1 == getEmptyViewSpace();
        boolean errorShow = 1 == getErrorViewSpace();
        mFooterContainer.removeViewAt(index);
        if (0 == mFooterContainer.getChildCount()) {
            if (emptyShow || errorShow) {
                if (headerFooterFront) {
                    notifyItemRemoved(getHeaderViewSpace() + 1);
                }
            } else {
                notifyItemRemoved(getHeaderViewSpace() + mList.size());
            }
        }
    }

    /**
     * 移除所有足视图
     */
    public void removeFooterView() {
        if (0 == getFooterViewSpace()) {
            return;
        }
        boolean emptyShow = 1 == getEmptyViewSpace();
        boolean errorShow = 1 == getErrorViewSpace();
        mFooterContainer.removeAllViews();
        if (emptyShow || errorShow) {
            if (headerFooterFront) {
                notifyItemRemoved(0);
            }
        } else {
            notifyItemRemoved(getHeaderViewSpace() + mList.size());
        }
    }

    /**
     * 获取足视图数量
     *
     * @return
     */
    public int getFooterViewSize() {
        if (1 == getFooterViewSpace()) {
            return mFooterContainer.getChildCount();
        }
        return 0;
    }

    /**
     * 加载结束
     */
    public void loadMoreDismiss() {
        if (1 == getLoadMoreViewSpace()) {
            mILoadMore.onDismiss();
            mLoadMoreContainer.setVisibility(View.GONE);
            mLoadStatus = Status.LOAD_IDLE;
        }
    }

    /**
     * 加载失败
     */
    public void loadMoreFailure() {
        if (1 == getLoadMoreViewSpace()) {
            mILoadMore.onFailure();
            mLoadStatus = Status.LOAD_ERROR;
        }
    }

    /**
     * 加载到底了
     */
    public void loadMoreEnd() {
        if (1 == getLoadMoreViewSpace()) {
            mILoadMore.onLoadEnd();
            mLoadStatus = Status.LOAD_END;
        }
    }

    /**
     * 重新加载
     */
    public void loadMoreWhileFailure() {
        if (1 == getLoadMoreViewSpace() && Status.LOAD_ERROR == mLoadStatus) {
            mLoadStatus = Status.LOAD_ING;
            mILoadMore.onShow();
            if (null != mOnLoadListener) {
                mOnLoadListener.onLoad();
            }
        }
    }

    /**
     * 用于嵌套Nested场景
     *
     * @param scrollState {@link android.support.v4.widget.NestedScrollView}的滚动状态
     */
    public void applyInNestedScroll(int scrollState) {
        if (null == mLayoutManager) {
            return;
        }

        /*
         * 满足条件
         */
        if (SCROLL_STATE_IDLE == scrollState
                && 1 == getLoadMoreViewSpace()
                && 0 == Math.max(getEmptyViewSpace(), getErrorViewSpace())
                && (mLoadStatus == Status.LOAD_IDLE)) {
            //上拉加载条目在屏幕上可见
            int[] location = new int[2];
            mLoadMoreContainer.getLocationInWindow(location);
            if (location[1] <= Resources.getSystem().getDisplayMetrics().heightPixels) {
                //开始加载
                mLoadStatus = Status.LOAD_ING;
                mLoadMoreContainer.setVisibility(VISIBLE);
                mILoadMore.onShow();
                if (null != mOnLoadListener) {
                    mOnLoadListener.onLoad();
                }
            }
        }
    }

    /**
     * 设置上拉加载视图
     *
     * @param loadMoreView
     */
    public void setLoadMoreView(@Nullable View loadMoreView) {
        boolean loadShow = 1 == getLoadMoreViewSpace() && 0 == Math.max(getEmptyViewSpace(), getErrorViewSpace());
        if (null == loadMoreView) {
            //移除原有视图
            if (null != mLoadMoreContainer && 0 != mLoadMoreContainer.getChildCount()) {
                mLoadMoreContainer.removeAllViews();
                if (loadShow) {
                    notifyItemRemoved(getItemCount() - 1);
                }
            }
        } else {
            if (!(loadMoreView instanceof ILoadMore)) {
                throw new IllegalStateException("LoadMoreView must implements ILoadMore !");
            }
            mILoadMore = (ILoadMore) loadMoreView;
            if (null == mLoadMoreContainer) {
                mLoadMoreContainer = new FrameLayout(loadMoreView.getContext());
                mLoadMoreContainer.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mLoadMoreContainer.removeAllViews();
            }
            safeAddView(mLoadMoreContainer, loadMoreView);
            loadMoreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMoreWhileFailure();
                }
            });
            if (0 == Math.max(getEmptyViewSpace(), getErrorViewSpace())) {
                notifyItemInserted(getItemCount() - 1);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mLayoutManager = recyclerView.getLayoutManager();
        if (null == mLayoutManager) {
            return;
        }
        if (mLayoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) mLayoutManager;
            final GridLayoutManager.SpanSizeLookup lookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (getItemViewType(position)) {
                        case TYPE_EMPTY:
                        case TYPE_ERROR:
                        case TYPE_HEADER:
                        case TYPE_FOOTER:
                        case TYPE_LOAD:
                            //独占一行
                            return gridLayoutManager.getSpanCount();
                        default:
                            if (null != lookup) {
                                return lookup.getSpanSize(position - getHeaderViewSpace());
                            }
                            return 1;
                    }
                }
            });
        }
        recyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        recyclerView.removeOnScrollListener(onScrollListener);
    }

    @Override
    public void onViewAttachedToWindow(FasterHolder holder) {
        //支持瀑布流
        LayoutParams params = holder.itemView.getLayoutParams();
        if (null != params && params instanceof StaggeredGridLayoutManager.LayoutParams) {
            switch (holder.getItemViewType()) {
                case TYPE_EMPTY:
                case TYPE_ERROR:
                case TYPE_HEADER:
                case TYPE_FOOTER:
                case TYPE_LOAD:
                    StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) params;
                    //独占一行、列
                    p.setFullSpan(true);
                default:
                    break;
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(FasterHolder holder) {
        holder.onDetach();
    }

    @Override
    public void onViewRecycled(FasterHolder holder) {
        holder.onRecycle();
    }

    @Override
    public FasterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_EMPTY:
                return createHolder(mEmptyContainer);
            case TYPE_ERROR:
                return createHolder(mErrorContainer);
            case TYPE_HEADER:
                return createHolder(mHeaderContainer);
            case TYPE_FOOTER:
                return createHolder(mFooterContainer);
            case TYPE_LOAD:
                return createHolder(mLoadMoreContainer);
            default:
                for (final Entry<T> entry : mList) {
                    if (null != entry.getStrategy()) {
                        if (entry.getStrategy().getItemViewType() == viewType) {
                            //创建FasterHolder
                            final FasterHolder holder = entry.getStrategy().createHolder(parent);
                            //依附Adapter
                            holder.attach(this);
                            //设置监听
                            if (null != mOnItemClickListener) {
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mOnItemClickListener.onItemClick(FasterAdapter.this, v, holder.getAdapterPosition() - getHeaderViewSpace());
                                    }
                                });
                            }
                            if (null != mOnItemLongClickListener) {
                                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        mOnItemLongClickListener.onItemLongClick(FasterAdapter.this, v, holder.getAdapterPosition() - getHeaderViewSpace());
                                        return false;
                                    }
                                });
                            }
                            holder.onCreate();
                            return holder;
                        }
                    } else {
                        throw new NullPointerException("Entry has not strategy !");
                    }
                }
                throw new NullPointerException("no FasterHolder found !");
        }
    }

    /**
     * 创建Holder
     *
     * @param view
     * @return
     */
    private FasterHolder createHolder(View view) {
        FasterHolder holder = new FasterHolder(view);
        holder.attach(this);
        holder.onCreate();
        return holder;
    }

    @Override
    public void onBindViewHolder(FasterHolder holder, int position, List<Object> payloads) {
        switch (holder.getItemViewType()) {
            case TYPE_EMPTY:
                break;
            case TYPE_ERROR:
                break;
            case TYPE_HEADER:
                break;
            case TYPE_FOOTER:
                break;
            case TYPE_LOAD:
                break;
            default:
                final int listPosition = position - getHeaderViewSpace();
                Strategy<T> strategy = mList.get(listPosition).getStrategy();
                if (null == strategy) {
                    throw new NullPointerException("Entry has not strategy !");
                } else {
                    strategy.onBindViewHolder(holder, mList.get(listPosition).getData(), payloads);
                }
        }
    }

    @Override
    public void onBindViewHolder(FasterHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        boolean hasEmpty = 1 == getEmptyViewSpace();
        boolean hasError = 1 == getErrorViewSpace();
        boolean hasHeader = 1 == getHeaderViewSpace();
        if (headerFooterFront) {
            if (hasError) {
                switch (position) {
                    case 0:
                        return hasHeader ? TYPE_HEADER : TYPE_ERROR;
                    case 1:
                        return hasHeader ? TYPE_ERROR : TYPE_FOOTER;
                    case 2:
                        return TYPE_FOOTER;
                    default:
                        //理论不出现
                        return TYPE_ERROR;
                }
            }
            if (hasEmpty) {
                switch (position) {
                    case 0:
                        return hasHeader ? TYPE_HEADER : TYPE_EMPTY;
                    case 1:
                        return hasHeader ? TYPE_EMPTY : TYPE_FOOTER;
                    case 2:
                        return TYPE_FOOTER;
                    default:
                        //理论不出现
                        return TYPE_EMPTY;
                }
            }
            return getRealItemViewType(position);
        } else {
            if (hasError) {
                //错误视图
                return TYPE_ERROR;
            }
            if (hasEmpty) {
                //空视图
                return TYPE_EMPTY;
            }
            return getRealItemViewType(position);
        }
    }

    /**
     * 排除headerFooterFront后的获取Type逻辑
     *
     * @param position
     * @return
     */
    private int getRealItemViewType(int position) {
        if (0 == position && 1 == getHeaderViewSpace()) {
            //头视图
            return TYPE_HEADER;
        }
        //足视图位置
        int footerPosition = 1 == getFooterViewSpace() ? getHeaderViewSpace() + mList.size() : -1;
        if (-1 != footerPosition) {
            //存在足视图
            if (position < footerPosition) {
                //数据视图的type类型
                return getItemViewTypeFromList(position);
            } else if (position == footerPosition) {
                //足视图
                return TYPE_FOOTER;
            } else {
                //上拉加载视图
                return TYPE_LOAD;
            }
        } else {
            //不存在足视图
            if (1 == getLoadMoreViewSpace() && position == getItemCount() - 1) {
                //加载视图
                return TYPE_LOAD;
            } else {
                //数据视图的type类型
                return getItemViewTypeFromList(position);
            }
        }
    }

    /**
     * 从数据集合中获取类型
     *
     * @param position
     * @return
     */
    private int getItemViewTypeFromList(int position) {
        int listPosition = position - getHeaderViewSpace();
        Entry<T> entry = mList.get(listPosition);
        if (null == entry.getStrategy()) {
            throw new IllegalStateException("must has strategy !");
        } else {
            return entry.getStrategy().getItemViewType();
        }
    }

    @Override
    public int getItemCount() {
        if (headerFooterFront) {
            //头、足、占位视图可以同时显示
            if (1 == Math.max(getErrorViewSpace(), getEmptyViewSpace())) {
                //占位视图(最多显示一个)+头+足
                return getHeaderViewSpace() + 1 + getFooterViewSpace();
            }
            //头+体+足+加载视图
            return getHeaderViewSpace() + mList.size() + getFooterViewSpace() + getLoadMoreViewSpace();
        } else {
            if (1 == Math.max(getErrorViewSpace(), getEmptyViewSpace())) {
                //占位视图
                return 1;
            }
            //头+体+足+加载视图
            return getHeaderViewSpace() + mList.size() + getFooterViewSpace() + getLoadMoreViewSpace();
        }
    }

    /**
     * 获取存放额外类型的Array
     *
     * @return
     */
    public LongSparseArray<Object> getObjectArray() {
        return array;
    }


    /**
     * 获取空视图的占用position
     *
     * @return
     */
    public int getEmptyViewSpace() {
        if (ViewUtils.isEmpty(mEmptyContainer) || (!isEmptyEnabled) || isDisplayError) {
            return 0;
        }
        return EmptyUtils.isEmpty(mList) ? 1 : 0;
    }

    /**
     * 获取错误视图的占用position
     *
     * @return
     */
    public int getErrorViewSpace() {
        if (ViewUtils.isEmpty(mErrorContainer) || (!isDisplayError)) {
            return 0;
        }
        return 1;
    }

    /**
     * 获取头View的占用position
     *
     * @return
     */
    public int getHeaderViewSpace() {
        return ViewUtils.isEmpty(mHeaderContainer) ? 0 : 1;
    }

    /**
     * 获取足View的占用position
     *
     * @return
     */
    public int getFooterViewSpace() {
        return ViewUtils.isEmpty(mFooterContainer) ? 0 : 1;
    }

    /**
     * 获取上拉加载View的占用position
     *
     * @return
     */
    public int getLoadMoreViewSpace() {
        return ViewUtils.isEmpty(mLoadMoreContainer) || (!isLoadMoreEnabled) ? 0 : 1;
    }

    /**
     * 设置数据源
     *
     * @param list
     */
    public void setData(@Nullable List<Entry<T>> list) {
        if (null == list) {
            clear(false);
        } else {
            mList = list;
            notifyDataSetChanged();
        }
    }

    /**
     * 设置数据源
     *
     * @param list
     */
    public void setDataByDiff(@Nullable final List<Entry<T>> list) {
        if (null == list) {
            clear(false);
        } else {
            if (1 == getErrorViewSpace()) {
                mList = list;
                return;
            }
            if (1 == getEmptyViewSpace()) {
                //避免占位视图ItemAnimator
                mList = list;
                notifyDataSetChanged();
                return;
            }
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mList.size();
                }

                @Override
                public int getNewListSize() {
                    return list.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    //equals比较
                    return mList.get(oldItemPosition).getData().equals(list.get(newItemPosition).getData());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return false;
                }

            }, true);
            mList = list;
            result.dispatchUpdatesTo(new ListUpdateCallback() {
                @Override
                public void onInserted(int position, int count) {
                    notifyItemRangeInserted(position + getHeaderViewSpace(), count);
                }

                @Override
                public void onRemoved(int position, int count) {
                    notifyItemRangeRemoved(position + getHeaderViewSpace(), count);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    notifyItemMoved(fromPosition + getHeaderViewSpace(), toPosition);
                }

                @Override
                public void onChanged(int position, int count, Object payload) {
                    notifyItemRangeChanged(position + getHeaderViewSpace(), count, payload);
                }
            });
        }
    }

    /**
     * 添加数据
     *
     * @param entry
     */
    public void add(@Nullable Entry<T> entry) {
        add(mList.size(), entry, false);
    }

    /**
     * 添加数据
     *
     * @param entry
     * @param immediately
     */
    public void add(@Nullable Entry<T> entry, boolean immediately) {
        add(mList.size(), entry, immediately);
    }

    /**
     * 添加数据
     *
     * @param index
     * @param entry
     */
    public void add(int index, @Nullable Entry<T> entry) {
        add(index, entry, false);
    }

    /**
     * 添加数据
     *
     * @param index
     * @param entry
     * @param immediately
     */
    public void add(int index, @Nullable Entry<T> entry, boolean immediately) {
        if (entry == null) {
            return;
        }
        int emptySpace = getEmptyViewSpace();
        mList.add(index, entry);

        if (immediately || 1 == emptySpace || 1 == getErrorViewSpace()) {
            //避免占位视图ItemAnimator
            notifyDataSetChanged();
        } else {
            notifyItemInserted(getHeaderViewSpace() + index);
        }
    }

    /**
     * 添加数据集
     *
     * @param collection
     */
    public void addAll(@Nullable Collection<? extends Entry<T>> collection) {
        addAll(mList.size(), collection, false);
    }

    /**
     * 添加数据集
     *
     * @param collection
     * @param immediately
     */
    public void addAll(@Nullable Collection<? extends Entry<T>> collection, boolean immediately) {
        addAll(mList.size(), collection, immediately);
    }

    /**
     * 添加数据集
     *
     * @param index
     * @param collection
     */
    public void addAll(int index, @Nullable Collection<? extends Entry<T>> collection) {
        addAll(index, collection, false);
    }

    /**
     * 添加数据集
     *
     * @param index
     * @param collection
     * @param immediately
     */
    public void addAll(int index, @Nullable Collection<? extends Entry<T>> collection, boolean immediately) {
        if (EmptyUtils.isEmpty(collection)) {
            return;
        }
        int emptySpace = getEmptyViewSpace();
        mList.addAll(index, collection);

        if (immediately || 1 == emptySpace || 1 == getErrorViewSpace()) {
            //避免占位视图ItemAnimator
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(getHeaderViewSpace() + index, collection.size());
        }
    }

    /**
     * 移除
     *
     * @param position
     */
    public Entry<T> remove(int position) {
        return remove(position, false);
    }

    /**
     * 移除
     *
     * @param position
     * @param immediately
     * @return
     */
    public Entry<T> remove(int position, boolean immediately) {
        Entry<T> t = mList.remove(position);

        if (immediately || 1 == getEmptyViewSpace() || 1 == getErrorViewSpace()) {
            //避免占位视图ItemAnimator
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(getHeaderViewSpace() + position);
        }
        return t;
    }

    /**
     * equals 移除
     *
     * @param t
     * @return
     */
    public boolean remove(@Nullable T t) {
        return remove(t, false);
    }

    /**
     * equals 移除
     *
     * @param t
     * @param immediately
     * @return
     */
    public boolean remove(@Nullable T t, boolean immediately) {
        if (t == null) {
            return true;
        }
        Entry<T> entry;
        int index = -1;
        for (int i = 0; i < mList.size(); i++) {
            entry = mList.get(i);
            if (entry.getData().equals(t)) {
                index = i;
                break;
            }
        }
        if (-1 != index) {
            remove(index);
            return true;
        }
        return false;
    }

    /**
     * 条件移除
     *
     * @param predicate
     * @return
     */
    public boolean removeIf(@NonNull CollectionUtils.Predicate<T> predicate) {
        return removeIf(predicate, false);
    }

    /**
     * 条件移除
     *
     * @param predicate
     * @param immediately
     * @return
     */
    public boolean removeIf(@NonNull CollectionUtils.Predicate<T> predicate, boolean immediately) {
        boolean removed = false;
        final ListIterator<Entry<T>> listIterator = mList.listIterator();
        int nextIndex;
        while (listIterator.hasNext()) {
            nextIndex = listIterator.nextIndex();
            if (predicate.process(listIterator.next().getData())) {
                listIterator.remove();
                if (!immediately && 0 == getErrorViewSpace()) {
                    notifyItemRemoved(nextIndex);
                }
                removed = true;
            }
        }

        if (immediately || 1 == getEmptyViewSpace() || 1 == getErrorViewSpace()) {
            //避免占位视图ItemAnimator
            notifyDataSetChanged();
        }
        return removed;
    }

    /**
     * 清理数据集合
     */
    public void clear() {
        clear(false);
    }

    /**
     * 清理数据集合
     *
     * @param immediately
     */
    public void clear(boolean immediately) {
        if (immediately) {
            mList.clear();
            notifyDataSetChanged();
        } else {
            int size = mList.size();
            mList.clear();
            if (1 == getEmptyViewSpace() || 1 == getErrorViewSpace()) {
                //避免占位视图ItemAnimator
                notifyDataSetChanged();
            } else {
                notifyItemRangeRemoved(getHeaderViewSpace(), size);
            }
        }
    }

    /**
     * 获取数据封装集合
     *
     * @return
     */
    public List<Entry<T>> getEntryList() {
        return mList;
    }

    /**
     * 获取浅拷贝的数据实体集合
     *
     * @return
     */
    public List<T> getSnapList() {
        List<T> result = new ArrayList<>();
        for (Entry<T> entry : mList) {
            result.add(entry.getData());
        }
        return result;
    }

    /**
     * 获取数据实体集合的浅拷贝
     *
     * @return
     */
    public T getListItem(int listPosition) {
        return mList.get(listPosition).getData();
    }

    /**
     * 获取数据实体的数量
     *
     * @return
     */
    public int getListSize() {
        return mList.size();
    }

    /**
     * 根据绑定关系来填充数据集合
     *
     * @param ts
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Entry<T>> fillByBindStrategy(@Nullable T[] ts) {
        if (null == mBindMap || mBindMap.isEmpty()) {
            throw new NullPointerException("FasterAdapter Builder no bind Class of Strategy !");
        }
        List<Entry<T>> result = new ArrayList<>();
        if (null != ts) {
            Entry<T> entry;
            Strategy<T> strategy;
            for (T t : ts) {
                strategy = (Strategy<T>) mBindMap.get(t.getClass());
                if (null == strategy) {
                    throw new NullPointerException("no Class of Strategy bind found!");
                }
                entry = new Entry<>(t, strategy);
                result.add(entry);
            }
        }
        return result;
    }

    /**
     * 根据绑定关系来填充数据集合
     *
     * @param list
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Entry<T>> fillByBindStrategy(@Nullable List<T> list) {
        if (null == mBindMap || mBindMap.isEmpty()) {
            throw new NullPointerException("FasterAdapter Builder no bind Class of Strategy !");
        }
        List<Entry<T>> result = new ArrayList<>();
        if (null != list) {
            Entry<T> entry;
            Strategy<T> strategy;
            for (T t : list) {
                strategy = (Strategy<T>) mBindMap.get(t.getClass());
                if (null == strategy) {
                    throw new NullPointerException("no Class of Strategy bind found!");
                }
                entry = new Entry<>(t, strategy);
                result.add(entry);
            }
        }
        return result;
    }

    /**
     * 根据绑定关系来填充数据实体
     *
     * @param t
     * @return
     */
    @SuppressWarnings("unchecked")
    public Entry<T> fillByBindStrategy(@NonNull T t) {
        if (null == mBindMap || mBindMap.isEmpty()) {
            throw new NullPointerException("FasterAdapter Builder no bind Class of Strategy !");
        }
        Strategy<T> strategy = (Strategy<T>) mBindMap.get(t.getClass());
        if (null == strategy) {
            throw new NullPointerException("no Class of Strategy bind found!");
        }
        return new Entry<>(t, strategy);
    }

    /**
     * 单类型策略的集合的方式填充
     *
     * @param list
     * @param strategy
     * @param <D>
     * @return
     */
    public static <D> List<Entry<D>> fillBySingleStrategy(@Nullable List<D> list, @NonNull Strategy<D> strategy) {
        List<Entry<D>> result = new ArrayList<>();
        if (null == list) {
            return result;
        }
        Entry<D> entry;
        for (D d : list) {
            entry = new Entry<D>(d, strategy);
            result.add(entry);
        }
        return result;
    }

    /**
     * 单类型策略的集合的方式填充
     *
     * @param ds
     * @param strategy
     * @param <D>
     * @return
     */
    public static <D> List<Entry<D>> fillBySingleStrategy(@Nullable D[] ds, @NonNull Strategy<D> strategy) {
        List<Entry<D>> result = new ArrayList<>();
        if (null == ds) {
            return result;
        }
        Entry<D> entry;
        for (D d : ds) {
            entry = new Entry<D>(d, strategy);
            result.add(entry);
        }
        return result;
    }

    /**
     * 条目点击监听器
     */
    public interface OnItemClickListener {
        /**
         * 单击
         *
         * @param adapter
         * @param view
         * @param listPosition
         */
        void onItemClick(FasterAdapter adapter, View view, int listPosition);
    }

    /**
     * 条目长按点击监听器
     */
    public interface OnItemLongClickListener {
        /**
         * 长按
         *
         * @param adapter
         * @param view
         * @param listPosition
         */
        void onItemLongClick(FasterAdapter adapter, View view, int listPosition);
    }

    /**
     * 上拉加载监听器
     */
    public interface OnLoadListener {
        /**
         * 触发上拉加载
         */
        void onLoad();
    }

    public static final class Builder<D> {
        private OnItemClickListener itemClickListener = null;
        private OnItemLongClickListener itemLongClickListener = null;
        private OnLoadListener loadListener = null;

        private View emptyView = null;
        private View errorView = null;
        private View loadMoreView = null;
        private List<View> headerViews = null;
        private List<View> footerViews = null;
        private boolean emptyMatchParent = true;
        private boolean errorMatchParent = true;
        private boolean emptyEnabled = true;
        private boolean headerFooterFront = false;
        private boolean loadMoreEnabled = true;
        private List<Entry<D>> list = new ArrayList<>();
        private ArrayMap<Class<?>, Strategy<?>> bindMap = null;

        public Builder() {
        }

        /**
         * 设置数据实体的ItemView的点击监听，其他场景下建议通过对Strategy设置监听回调
         *
         * @param listener
         * @return
         */
        public Builder<D> itemClickListener(@Nullable OnItemClickListener listener) {
            this.itemClickListener = listener;
            return this;
        }

        /**
         * 设置数据实体的ItemView的长按点击监听，其他场景下建议通过对Strategy设置监听回调
         *
         * @param listener
         * @return
         */
        public Builder<D> itemClickListener(@Nullable OnItemLongClickListener listener) {
            this.itemLongClickListener = listener;
            return this;
        }

        /**
         * 设置上拉加载的监听，需设置LoadMoreView才有效
         *
         * @param listener
         * @return
         */
        public Builder<D> loadListener(@Nullable OnLoadListener listener) {
            this.loadListener = listener;
            return this;
        }

        /**
         * 空视图（占位图）
         *
         * @param emptyView
         * @return
         */
        public Builder<D> emptyView(@Nullable View emptyView) {
            this.emptyView = emptyView;
            return this;
        }

        /**
         * 错误视图（占位图）
         *
         * @param errorView
         * @return
         */
        public Builder<D> errorView(@Nullable View errorView) {
            this.errorView = errorView;
            return this;
        }

        /**
         * 设置上拉加载视图，不设置isLoadMoreEnabled为true也不生效
         *
         * @param loadMoreView
         * @return
         */
        public Builder<D> loadMoreView(@Nullable View loadMoreView) {
            if (!(loadMoreView instanceof ILoadMore)) {
                throw new IllegalStateException("loadMoreView must implements ILoadMore !");
            }
            this.loadMoreView = loadMoreView;
            return this;
        }

        /**
         * 添加头View
         *
         * @param headerView
         * @return
         */
        public Builder<D> addHeaderView(@NonNull View headerView) {
            if (null == headerViews) {
                headerViews = new ArrayList<>();
            }
            headerViews.add(headerView);
            return this;
        }

        /**
         * 添加足View
         *
         * @param footView
         * @return
         */
        public Builder<D> addFooterView(@NonNull View footView) {
            if (null == footerViews) {
                footerViews = new ArrayList<>();
            }
            footerViews.add(footView);
            return this;
        }

        /**
         * 空视图的包裹容器高度是否是{@link ViewGroup.LayoutParams#MATCH_PARENT},充满RecyclerView
         * <br>
         * 一般不用对其设置，只有在有头、足视图时需要注意
         *
         * @param emptyMatchParent default true
         * @return
         */
        public Builder<D> emptyMatchParent(boolean emptyMatchParent) {
            this.emptyMatchParent = emptyMatchParent;
            return this;
        }

        /**
         * 错误视图的包裹容器高度是否是{@link ViewGroup.LayoutParams#MATCH_PARENT},充满RecyclerView
         * <br>
         * 一般不用对其设置，只有在有头、足视图时需要注意
         *
         * @param errorMatchParent default true
         * @return
         */
        public Builder<D> errorMatchParent(boolean errorMatchParent) {
            this.errorMatchParent = errorMatchParent;
            return this;
        }

        /**
         * 在初始化数据之前可以先设置false来不显示空视图
         *
         * @param emptyEnabled default true
         * @return
         */
        public Builder<D> emptyEnabled(boolean emptyEnabled) {
            this.emptyEnabled = emptyEnabled;
            return this;
        }

        /**
         * 头、足视图优先级是否大于占位视图，即在无数据或者错误视图时是否显示头、足视图
         *
         * @param headerFooterFront default false
         * @return
         */
        public Builder<D> headerFooterFront(boolean headerFooterFront) {
            this.headerFooterFront = headerFooterFront;
            return this;
        }

        /**
         * 是否启用上拉加载，需设置LoadMoreView才生效
         *
         * @param loadMoreEnabled default false
         * @return
         */
        public Builder<D> loadMoreEnabled(boolean loadMoreEnabled) {
            this.loadMoreEnabled = loadMoreEnabled;
            return this;
        }

        /**
         * 默认数据
         *
         * @param es
         * @return
         */
        public Builder<D> data(@Nullable Entry<D>... es) {
            if (null == es) {
                return this;
            }
            this.list = new ArrayList<>(Arrays.asList(es));
            return this;
        }

        /**
         * 默认数据
         *
         * @param list
         * @return
         */
        public Builder<D> data(@Nullable List<Entry<D>> list) {
            if (null == list) {
                return this;
            }
            this.list = list;
            return this;
        }

        /**
         * 单类型策略的集合的方式填充
         *
         * @param list
         * @param strategy
         * @return
         */
        public Builder<D> fillBySingleStrategy(@Nullable List<D> list, @NonNull Strategy<D> strategy) {
            if (null == list) {
                return this;
            }
            this.list = FasterAdapter.fillBySingleStrategy(list, strategy);
            return this;
        }

        /**
         * 单类型策略的集合的方式填充
         *
         * @param ds
         * @param strategy
         * @return
         */
        public Builder<D> fillBySingleStrategy(@Nullable D[] ds, @NonNull Strategy<D> strategy) {
            if (null == ds) {
                return this;
            }
            this.list = FasterAdapter.fillBySingleStrategy(ds, strategy);
            return this;
        }

        /**
         * 通过数据类型 - 视图策略 来绑定
         *
         * @param cls
         * @param strategy
         * @param <T>
         * @return
         */
        public <T> Builder<D> bind(@NonNull Class<T> cls, @NonNull Strategy<T> strategy) {
            if (null == bindMap) {
                bindMap = new ArrayMap<>();
            }
            bindMap.put(cls, strategy);
            return this;
        }

        /**
         * 构建
         *
         * @return
         */
        public FasterAdapter<D> build() {
            return new FasterAdapter<D>(this);
        }
    }
}

