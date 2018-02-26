package com.tk.tdroid.permission;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * <pre>
 *      author : TK
 *      time : 2017/11/7
 *      desc :
 * </pre>
 */

public class RxPermissionFragment extends Fragment {
    private static final int REQUEST = 0x01;
    private final Map<String, Subject<Permission>> mSubjects = new ArrayMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    Observable<Permission> requestEach(@NonNull String... permissions) {
        final List<Observable<Permission>> observableList = new ArrayList<>(permissions.length);
        final List<String> requestList = new LinkedList<>();
        for (String s : permissions) {
            boolean granted = isGranted(s);
            boolean revoked = isRevoked(s);
            if (granted) {
                //已允许
                observableList.add(Observable.just(new Permission(s, true, false)));
            } else {
                if (revoked) {
                    //已被勾选拒绝
                    observableList.add(Observable.just(new Permission(s, false, false)));
                } else {
                    //需要唤起请求
                    observableList.add(generateSubject(s));
                    requestList.add(s);
                }
            }
        }
        if (!requestList.isEmpty() && Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //发起请求
            requestPermissions(requestList.toArray(new String[requestList.size()]), REQUEST);
        }
        return Observable.concat(Observable.fromIterable(observableList))
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    Single<Boolean> request(@NonNull String... permissions) {
        return requestEach(permissions)
                .toList()
                .map(new Function<List<Permission>, Boolean>() {
                    @Override
                    public Boolean apply(List<Permission> permissions) throws Exception {
                        for (Permission permission : permissions) {
                            if (!permission.granted) {
                                return false;
                            }
                        }
                        return true;
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            Subject<Permission> subject = mSubjects.get(permissions[i]);
            if (subject != null) {
                boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                //发送
                subject.onNext(new Permission(permissions[i], granted, shouldShowRequestPermissionRationale(permissions[i])));
                subject.onComplete();
            }
        }
    }

    private Subject<Permission> generateSubject(String permission) {
        Subject<Permission> subject = PublishSubject.create();
        mSubjects.put(permission, subject);
        return subject;
    }

    private boolean isGranted(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isRevoked(String permission) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().getPackageManager().isPermissionRevokedByPolicy(permission, getActivity().getPackageName());
    }
}