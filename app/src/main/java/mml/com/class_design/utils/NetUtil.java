package mml.com.class_design.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

/**
 * 文 件 名： NetUtil
 * 创 建 人：
 * 创建时间：
 * 备    注： 网络检查工具类
 */

public class NetUtil {
    /**
     * 判断网络情况
     *
     * @param context 上下文
     * @return false 表示没有网络 true 表示有网络
     */
    public static boolean isNetworkAvalible(Context context) {
        // 获得网络状态管理器  
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组  
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();
            if (net_info != null) {
                for (int i = 0; i < net_info.length; i++) {
                    // 判断获得的网络状态是否是处于连接状态  
                    if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断网络是否连接
     **/
    public static boolean iSNetStateConnect(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取代表联网状态的NetWorkInfo对象  
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        // 获取当前的网络连接是否可用  
        boolean available = false;
        try {
            available = networkInfo.isAvailable();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (available) {
            if(isNetworkAvalible(context))
                 Log.i("net", "当前的网络连接可用且为sim链接");
            Log.i("net", "当前的网络连接可用");
            return true;
        } else {
            Log.i("net", "当前的网络连接不可用");
            return false;
        }
    }

    // 如果没有网络，则弹出网络设置对话框  
    public static void checkNetwork(final Activity activity) {
        if (!NetUtil.isNetworkAvalible(activity)) {
            TextView msg = new TextView(activity);
            msg.setText("当前没有可以使用的网络，请设置网络！");
            new AlertDialog.Builder(activity)
                    .setTitle("网络状态提示")
                    .setView(msg)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 跳转到设置界面
                                    activity.startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 1);
                                }
                            })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        return;
    }

    /**
     * 在连接到网络基础之上,判断设备是否是SIM网络连接
     *
     * @param context
     * @return
     */
    public static boolean IsMobileNetConnect(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if (NetworkInfo.State.CONNECTED == state)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
