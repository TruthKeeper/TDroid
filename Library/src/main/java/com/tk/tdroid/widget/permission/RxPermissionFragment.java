package com.tk.tdroid.widget.permission;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.ReplaySubject;
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
    private Subject<PermissionResult> subject;
    private List<Permission> permissionList = new ArrayList<>();

    void request(@NonNull String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            subject = ReplaySubject.create(2);
            permissionList.clear();
            //需要申请
            List<String> preRequest = new ArrayList<>();
            //被拒绝的
            List<String> refuse = new ArrayList<>();
            Permission permission;

            for (String s : permissions) {
                boolean granted = ContextCompat.checkSelfPermission(getContext(), s) == PackageManager.PERMISSION_GRANTED;
                boolean showRequest = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), s);

                permission = new Permission(s, granted, showRequest);
                permissionList.add(permission);

                if (!granted) {
                    //权限未申请
                    preRequest.add(s);
                    if (!showRequest) {
                        //被用户勾选不再询问
                        refuse.add(s);
                    }
                }
            }
            if (preRequest.isEmpty()) {
                //不需要请求
                if (refuse.isEmpty()) {
                    subject.onNext(new PermissionResult(true, false, permissionList));
                } else {
                    subject.onNext(new PermissionResult(false, true, permissionList));
                }
                subject.onComplete();
                return;
            }
            //发起请求
            requestPermissions(preRequest.toArray(new String[preRequest.size()]), REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean successful = true;
        boolean disable = false;
        Permission p;
        for (int i = 0; i < permissions.length; i++) {
            for (int j = 0, length = permissionList.size(); j < length; j++) {
                p = permissionList.get(j);
                if (p.getName().equals(permissions[i])) {
                    boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    p.setGranted(granted);
                    if (!granted) {
                        //失败了
                        successful = false;
                    }
                    boolean e = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), p.getName());
                    p.setShouldShowRequestPermissionRationale(e);
                    if (!e) {
                        //拒绝了
                        disable = true;
                    }
                }
            }
        }
        subject.onNext(new PermissionResult(successful, disable, permissionList));
        subject.onComplete();
    }

    Subject<PermissionResult> getSubject() {
        return subject;
    }
}
