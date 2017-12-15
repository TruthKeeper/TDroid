package com.tk.tdroid.utils;

import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.tk.tdroid.widget.viewloader.IViewLoader;
import com.tk.tdroid.widget.viewloader.LoaderViewContainer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tk.tdroid.utils.ViewLoader.Status.*;

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
    private ViewStub mEmptyViewStub;

    private View mErrorView;
    private ViewStub mErrorViewStub;

    private View mLoadingView;
    private ViewStub mLoadingViewStub;

    private View mNetworkInvalidView;
    private ViewStub mNetworkInvalidViewStub;

    private View mContentView;
    private LoaderViewContainer container;

    private int status = content;

    public static ViewLoader.Builder with(@Nullable View view) {
        return new ViewLoader.Builder(view);
    }

    private ViewLoader(Builder builder) {
        mEmptyView = builder.mEmptyView;
        mEmptyViewStub = builder.mEmptyViewStub;
        mErrorView = builder.mErrorView;
        mErrorViewStub = builder.mErrorViewStub;
        mLoadingView = builder.mLoadingView;
        mLoadingViewStub = builder.mLoadingViewStub;
        mNetworkInvalidView = builder.mNetworkInvalidView;
        mNetworkInvalidViewStub = builder.mNetworkInvalidViewStub;
        mContentView = builder.mContentView;
    }

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
        } else if (mEmptyViewStub != null) {
            container.addView(mEmptyViewStub);
        }
        if (mErrorView != null) {
            container.addView(mErrorView);
        } else if (mErrorViewStub != null) {
            container.addView(mErrorViewStub);
        }
        if (mLoadingView != null) {
            container.addView(mLoadingView);
        } else if (mLoadingViewStub != null) {
            container.addView(mLoadingViewStub);
        }
        if (mNetworkInvalidView != null) {
            container.addView(mNetworkInvalidView);
        } else if (mNetworkInvalidViewStub != null) {
            container.addView(mNetworkInvalidViewStub);
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
        if (mEmptyViewStub != null) {
            mEmptyView = mEmptyViewStub.inflate();
            mEmptyViewStub = null;
        }
        if (showView(mEmptyView)) {
            status = empty;
        }
    }

    @Override
    public void showErrorView() {
        if (mErrorViewStub != null) {
            mErrorView = mErrorViewStub.inflate();
            mErrorViewStub = null;
        }
        if (showView(mErrorView)) {
            status = error;
        }
    }

    @Override
    public void showLoadingView() {
        if (mLoadingViewStub != null) {
            mLoadingView = mLoadingViewStub.inflate();
            mLoadingViewStub = null;
        }
        if (showView(mLoadingView)) {
            status = loading;
        }
    }

    @Override
    public void showNetworkInvalidView() {
        if (mNetworkInvalidViewStub != null) {
            mNetworkInvalidView = mNetworkInvalidViewStub.inflate();
            mNetworkInvalidViewStub = null;
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
        private View mEmptyView;
        private ViewStub mEmptyViewStub;
        private View mErrorView;
        private ViewStub mErrorViewStub;
        private View mLoadingView;
        private ViewStub mLoadingViewStub;
        private View mNetworkInvalidView;
        private ViewStub mNetworkInvalidViewStub;
        private View mContentView;

        public Builder(@NonNull View view) {
            this.mContentView = view;
        }

        public Builder emptyView(@Nullable View view) {
            mEmptyViewStub = null;
            mEmptyView = view;
            return this;
        }

        public Builder emptyView(@LayoutRes int layoutId) {
            mEmptyView = null;
            if (mEmptyViewStub == null) {
                mEmptyViewStub = new ViewStub(mContentView.getContext(), layoutId);
            } else {
                mEmptyViewStub.setLayoutResource(layoutId);
            }
            return this;
        }

        public Builder errorView(@Nullable View view) {
            mErrorViewStub = null;
            mErrorView = view;
            return this;
        }

        public Builder errorView(@LayoutRes int layoutId) {
            mErrorView = null;
            if (mErrorViewStub == null) {
                mErrorViewStub = new ViewStub(mContentView.getContext(), layoutId);
            } else {
                mErrorViewStub.setLayoutResource(layoutId);
            }
            return this;
        }

        public Builder loadingView(@Nullable View view) {
            mLoadingViewStub = null;
            mLoadingView = view;
            return this;
        }

        public Builder loadingView(@LayoutRes int layoutId) {
            mLoadingView = null;
            if (mLoadingViewStub == null) {
                mLoadingViewStub = new ViewStub(mContentView.getContext(), layoutId);
            } else {
                mLoadingViewStub.setLayoutResource(layoutId);
            }
            return this;
        }


        public Builder networkInvalidView(@Nullable View view) {
            mNetworkInvalidViewStub = null;
            mNetworkInvalidView = view;
            return this;
        }

        public Builder networkInvalidView(@LayoutRes int layoutId) {
            mNetworkInvalidView = null;
            if (mNetworkInvalidViewStub == null) {
                mNetworkInvalidViewStub = new ViewStub(mContentView.getContext(), layoutId);
            } else {
                mNetworkInvalidViewStub.setLayoutResource(layoutId);
            }
            return this;
        }

        public ViewLoader create() {
            return new ViewLoader(this).create();
        }
    }
}
