package com.xiaoxin.basic.permissions;

public interface OnPermissionPageCallback {

    /**
     * 权限已经授予
     */
    void onGranted();

    /**
     * 权限已经拒绝
     */
    void onDenied();
}