package com.tim.pollution.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by 张晓宁 on 2017/6/10.
 */

/**
 * Created by lzq on 2016/6/30.
 */
public class PermissionsCheckerUtils {
    static Activity context;

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 200;
    /**
     * 单例对象实例
     */
    private static PermissionsCheckerUtils instance = null;

    public static PermissionsCheckerUtils getInstance(Activity context) {
        if (instance == null) {
            instance = new PermissionsCheckerUtils(context);
        }
        return instance;
    }

    private PermissionsCheckerUtils(Activity context) {
        this.context = context;
    }

    public void needPermission(int requestCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        requestAllPermissions(requestCode);
    }

    /*
    *
    * 申请授予权限
    * CALL_PHONE  READ_EXTERNAL_STORAGE CAMERA  READ_CONTACTS GET_ACCOUNTS ACCESS_FINE_LOCATION
    * */
    public void requestAllPermissions(int requestCode) {
        ActivityCompat.requestPermissions(context,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_CALL_PHONE);
    }

    public boolean requesCallPhonePermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return false;
        } else {
            return true;
        }
    }

    public boolean requestReadSDCardPermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return false;
        } else {
            return true;
        }
    }

    public boolean requestCamerPermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return false;
        } else {
            return true;
        }
    }

    public boolean requestReadConstantPermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return false;
        } else {
            return true;
        }
    }

    public boolean requestGET_ACCOUNTSPermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return false;
        } else {
            return true;
        }
    }

    public boolean requestLocationPermissions(int requestCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return false;
        } else {
            return true;
        }
    }

    /*
    工具类使用方法如下：
    我是在app第一次进入的时候，先判断android的版本，如果大于6.0，不管用户是否给与权限，都调用申请常用权限，防止因为用户不给于权限而导致崩溃，如:
       存储，电话，相机，通讯录，位置等。防止用户


       if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
        new PermissionUtils(this).needPermission(200);
    }




    重写请求权限后的回调方法：


        @Override
        public void onRequestPermissionsResult(int requestCode,
                                      String permissions[], int[] grantResults) {
           switch (requestCode) {
              case 200: {
                 // If request is cancelled, the result arrays are empty.
                 if (grantResults.length > 0
                       && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                //同意给与权限  可以再此处调用拍照
                    Log.i("用户同意权限", "user granted the permission!");

                 } else {

                    // permission denied, boo! Disable the
                    // f用户不同意 可以给一些友好的提示
                    Log.i("用户不同意权限", "user denied the permission!");
                 }
                 return;
              }

              // other 'case' lines to check for other
              // permissions this app might request
           }
        }


    *
    * */
// 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (lacksPermission(permissions[i])) {//无权限
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限  无权限返回true
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_DENIED;
    }




/*
*

 PermissionCheckerUtils checker=PermissionCheckerUtils.getInstance(MainActivity.this);



 findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    //拍照的时候要进行权限判断
                    if (checker.lacksPermissions(PERMISSIONS)){//true 代表无权限
                        Toast.makeText(MainActivity.this,"无权限了",Toast.LENGTH_SHORT).show();
                        //申请权限
                        ActivityCompat.requestPermissions(MainActivity.this,
                                PERMISSIONS,
                                200);
                    }else{
                        Toast.makeText(MainActivity.this,getDeviceInfo(MainActivity.this),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    // 如果是6.0以下 不需要权限判断检查版本 直接调用拍照
                    Toast.makeText(MainActivity.this,"6.0以下不需要主动申请权限哦",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }




 @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200:
                if (grantResults.length > 0) {
                    for (int i=0;i<grantResults.length;i++){
                        if (grantResults[i]== PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(MainActivity.this,getDeviceInfo(MainActivity.this),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }
    public static String getDeviceInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm!=null) {
            return tm.getDeviceId();
        }else{
            return null;
        }

    }
}











*
* */

}