package com.tk.tdroid.saverestore;

import android.os.Bundle;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/16
 *     desc   : xxxx描述
 * </pre>
 */
public interface ISaveRestore<T> {
    void save(T object, Bundle outState);

    void restore(T object, Bundle savedInstanceState);
}
