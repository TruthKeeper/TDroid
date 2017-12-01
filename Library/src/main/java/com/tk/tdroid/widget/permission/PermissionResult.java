package com.tk.tdroid.widget.permission;

import com.tk.tdroid.utils.IntentUtils;

import java.util.List;

/**
 * <pre>
 *     author : TK
 *     time   : 2017/05/04
 *     desc   : 申请权限结果
 *     if(isSuccessful()):成功GET
 *        if(!isDisable()):用户拒绝了权限
 *        if(isDisable()):用户勾选了不再提示，并且拒绝了权限,需要手动开启{@link {@link IntentUtils#toSetting()}}
 * </pre>
 */
public class PermissionResult {
    private final boolean successful;
    private final boolean disable;
    private final List<Permission> permissions;

    PermissionResult(boolean successful, boolean disable, List<Permission> permissions) {
        this.successful = successful;
        this.disable = disable;
        this.permissions = permissions;
    }

    /**
     * 是否获取权限成功
     *
     * @return
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * 是否存在用户勾选拒绝权限并且不再询问（需要引用设置中心手动开启）的情况
     *
     * @return
     */
    public boolean isDisable() {
        return disable;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }
}