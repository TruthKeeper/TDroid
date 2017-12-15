package com.tk.tdroid.widget.viewloader;

/**
 * <pre>
 *      author : TK
 *      time : 2017/12/15
 *      desc :
 * </pre>
 */

public interface IViewLoader {

    void showEmptyView();

    void showErrorView();

    void showLoadingView();

    void showNetworkInvalidView();

    void showContentView();
}
