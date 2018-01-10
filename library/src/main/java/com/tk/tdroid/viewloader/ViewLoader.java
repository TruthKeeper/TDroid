package com.tk.tdroid.viewloader;

import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tk.tdroid.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tk.tdroid.viewloader.ViewLoader.Status.content;
import static com.tk.tdroid.viewloader.ViewLoader.Status.empty;
import static com.tk.tdroid.viewloader.ViewLoader.Status.error;
import static com.tk.tdroid.viewloader.ViewLoader.Status.loading;
import static com.tk.tdroid.viewloader.ViewLoader.Status.networkInvalid;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/7
 *      desc :View 加载器
 *      <ol>
 *          <li>配置空视图</li>
 *          <li>配置错误视图</li>
 *          <li>配置加载中视图</li>
 *          <li>配置无网络视图</li>
 *      </ol>
 * </pre>
 */

public final class ViewLoader implements IViewLoader {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({content, empty, error, loading, networkInvalid})
    public @interface Status {
        int content = 0x01;
        int empty = 0x02;
        int error = 0x03;
        int loading = 0x04;
        int networkInvalid = 0x05;
    }

    private View mEmptyView;
    private int mEmptyViewRes;
    private View mErrorView;
    private int mErrorViewRes;
    private View mLoadingView;
    private int mLoadingViewRes;
    private View mNetworkInvalidView;
    private int mNetworkInvalidViewRes;
    private View mContentView;
    private LoaderViewContainer container;

    private int status = content;

    /**
     * 获取一个ViewLoader构造器
     *
     * @param view
     * @return
     */
    public static ViewLoader.Builder with(@Nullable View view) {
        return new ViewLoader.Builder(view);
    }

    private ViewLoader(Builder builder) {
        mEmptyView = builder.mEmptyView;
        mEmptyViewRes = builder.mEmptyViewRes;
        mErrorView = builder.mErrorView;
        mErrorViewRes = builder.mErrorViewRes;
        mLoadingView = builder.mLoadingView;
        mLoadingViewRes = builder.mLoadingViewRes;
        mNetworkInvalidView = builder.mNetworkInvalidView;
        mNetworkInvalidViewRes = builder.mNetworkInvalidViewRes;
        mContentView = builder.mContentView;
    }

    /**
     * 创建
     *
     * @return
     */
    public ViewLoader create() {
        ViewGroup parent = (ViewGroup) mContentView.getParent();
        if (parent == null) {
            throw new NullPointerException("ViewParent Is Null !");
        }
        if (parent instanceof LoaderViewContainer) {
            container = (LoaderViewContainer) parent;
        } else {
            container = new LoaderViewContainer(mContentView.getContext());
            int index = parent.indexOfChild(mContentView);
            ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
            parent.removeView(mContentView);

            container.addView(mContentView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            parent.addView(container, index, layoutParams);
        }
        if (mEmptyView != null) {
            container.addView(mEmptyView);
        } else if (mEmptyViewRes != 0) {
            mEmptyView = LayoutInflater.from(Utils.getApp()).inflate(mEmptyViewRes, container, false);
            mEmptyViewRes = 0;
            container.addView(mEmptyView);
        }
        if (mErrorView != null) {
            container.addView(mErrorView);
        } else if (mErrorViewRes != 0) {
            mErrorView = LayoutInflater.from(Utils.getApp()).inflate(mErrorViewRes, container, false);
            mErrorViewRes = 0;
            container.addView(mErrorView);
        }
        if (mLoadingView != null) {
            container.addView(mLoadingView);
        } else if (mLoadingViewRes != 0) {
            mLoadingView = LayoutInflater.from(Utils.getApp()).inflate(mLoadingViewRes, container, false);
            mLoadingViewRes = 0;
            container.addView(mLoadingView);
        }
        if (mNetworkInvalidView != null) {
            container.addView(mNetworkInvalidView);
        } else if (mNetworkInvalidViewRes != 0) {
            mNetworkInvalidView = LayoutInflater.from(Utils.getApp()).inflate(mNetworkInvalidViewRes, container, false);
            mNetworkInvalidViewRes = 0;
            container.addView(mNetworkInvalidView);
        }

        return this;
    }

    @Status
    public int getStatus() {
        return status;
    }

    /**
     * 显示某个View
     *
     * @param showView
     */
    private boolean showView(View showView) {
        if (container != null && showView != null) {
            for (int i = 0, size = container.getChildCount(); i < size; i++) {
                container.getChildAt(i).setVisibility(container.getChildAt(i) == showView ? View.VISIBLE : View.GONE);
            }
            return true;
        }
        return false;
    }

    @Override
    public void showEmptyView() {
        if (mEmptyViewRes != 0) {
            mEmptyView = LayoutInflater.from(Utils.getApp()).inflate(mEmptyViewRes, container, false);
            container.addView(mEmptyView);
            mEmptyViewRes = 0;
        }
        if (showView(mEmptyView)) {
            status = empty;
        }
    }

    @Override
    public void showErrorView() {
        if (mErrorViewRes != 0) {
            mErrorView = LayoutInflater.from(Utils.getApp()).inflate(mErrorViewRes, container, false);
            container.addView(mErrorView);
            mErrorViewRes = 0;
        }
        if (showView(mErrorView)) {
            status = error;
        }
    }

    @Override
    public void showLoadingView() {
        if (mLoadingViewRes != 0) {
            mLoadingView = LayoutInflater.from(Utils.getApp()).inflate(mLoadingViewRes, container, false);
            container.addView(mLoadingView);
            mLoadingViewRes = 0;
        }
        if (showView(mLoadingView)) {
            status = loading;
        }
    }

    @Override
    public void showNetworkInvalidView() {
        if (mNetworkInvalidViewRes != 0) {
            mNetworkInvalidView = LayoutInflater.from(Utils.getApp()).inflate(mNetworkInvalidViewRes, container, false);
            container.addView(mNetworkInvalidView);
            mNetworkInvalidViewRes = 0;
        }
        if (showView(mNetworkInvalidView)) {
            status = networkInvalid;
        }
    }

    @Override
    public void showContentView() {
        if (showView(mContentView)) {
            status = content;
        }
    }

    public static final class Builder {
        private View mEmptyView = null;
        private int mEmptyViewRes = 0;
        private View mErrorView = null;
        private int mErrorViewRes = 0;
        private View mLoadingView = null;
        private int mLoadingViewRes = 0;
        private View mNetworkInvalidView = null;
        private int mNetworkInvalidViewRes = 0;
        private View mContentView;

        public Builder(@NonNull View view) {
            this.mContentView = view;
        }

        public Builder emptyView(@Nullable View view) {
            mEmptyView = view;
            mEmptyViewRes = 0;
            return this;
        }

        public Builder emptyView(@LayoutRes int layoutId) {
            mEmptyView = null;
            mEmptyViewRes = layoutId;
            return this;
        }

        public Builder errorView(@Nullable View view) {
            mErrorView = view;
            mErrorViewRes = 0;
            return this;
        }

        public Builder errorView(@LayoutRes int layoutId) {
            mErrorView = null;
            mErrorViewRes = layoutId;
            return this;
        }

        public Builder loadingView(@Nullable View view) {
            mLoadingView = view;
            mLoadingViewRes = 0;
            return this;
        }

        public Builder loadingView(@LayoutRes int layoutId) {
            mLoadingView = null;
            mLoadingViewRes = layoutId;
            return this;
        }


        public Builder networkInvalidView(@Nullable View view) {
            mNetworkInvalidView = view;
            mNetworkInvalidViewRes = 0;
            return this;
        }

        public Builder networkInvalidView(@LayoutRes int layoutId) {
            mNetworkInvalidView = null;
            mNetworkInvalidViewRes = layoutId;
            return this;
        }

        /**
         * 创建
         *
         * @return
         */
        public ViewLoader create() {
            return new ViewLoader(this).create();
        }
    }
}
