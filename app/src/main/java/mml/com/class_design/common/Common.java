package mml.com.class_design.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import mml.com.class_design.R;
import mml.com.class_design.application.MyApplication;

/**
 * Created by Long on 2018/3/23.
 */

public class Common {


    /**
     * 判断支付宝客户端是否已安装，建议调用转账前检查
     * @return 支付宝客户端是否已安装
     */
    public static   boolean hasInstalledAlipayClient() {
        String ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone";
        PackageManager pm = MyApplication.getApplication().getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(ALIPAY_PACKAGE_NAME, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /***
     * 支付宝转账
     * @param activity
     * **/
    public static void openALiPay(Activity activity){
         String url1="intent://platformapi/startapp?saId=10000007&" +
                "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2Fa6x076306bxhk8outhwdr67%3F_s" +
                "%3Dweb-other&_t=1472443966571#Intent;" +
                "scheme=alipayqr;package=com.eg.android.AlipayGphone;end";
        //String url1=activity.getResources().getString(R.string.alipay);
        Intent intent = null;
        Toast.makeText(MyApplication.getApplication(),"感谢您的捐赠！٩(๑❛ᴗ❛๑)۶",Toast.LENGTH_SHORT).show();
        if(hasInstalledAlipayClient()){
            try {
                intent = Intent.parseUri(url1 ,Intent.URI_INTENT_SCHEME );
                activity.startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Toast.makeText(MyApplication.getApplication(),"出错啦",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(MyApplication.getApplication(),"您未安装支付宝哦！(>ω･* )ﾉ",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 版本信息提示
     * @param mContext
     * **/
//    public static void showNoticeDialog(final Context mContext) {
//        // 构造对话框
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        AlertDialog dialog = builder.create();
//        View view = View.inflate(mContext, R.layout.dialog_about_infor, null);
//        Button button=view.findViewById(R.id.check_update);
//        TextView tips=view.findViewById(R.id.tips);
//        tips.setText("测试测试手册！ヾ(=･ω･=)oヾ(=･ω･=)o");
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent=new Intent(mContext, UpdateService.class);
////                mContext.startService(intent);
//
//            new  DownloadUtils(mContext).downloadAPK("http://118.89.112.146:8008//update.apk","update.apk");
//            //    Toast.makeText(mContext,"已是最新！(*/ω＼*)",Toast.LENGTH_SHORT).show();
//                //虽然这里的参数是AlertDialog.Builder(Context context)但我们不能使用getApplicationContext()获得的Context,而必须使用Activity.this,因为只有一个Activity才能添加一个窗体。
//            }
//        });
//        dialog.setView(view,0,0,0,0);// 设置边距为0,保证在2.x的版本上运行没问题
//
//        dialog.show();
//    }

    /***
     * qq联系我
     * @param
     * **/
    public static void contactMe() {
        Context context=MyApplication.getApplication().getMainActivity();
        if (checkApkExist(context, "com.tencent.mobileqq")) {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + context.getResources().getString(R.string.qq_number);
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } else {
            Toast.makeText(context, "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 通过url获取json字符串
     * @param urlString
     */
    public static String getJsonString(String urlString) {


        BufferedReader bufferedReader;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL url = new URL(urlString);//json地址
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");//使用get方法接收
            InputStream inputStream = connection.getInputStream();//得到一个输入流
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTf-8"));
            String sread = null;
            while ((sread = bufferedReader.readLine()) != null) {
                stringBuffer.append(sread);
                // stringBuffer.append("\r\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stringBuffer.length() == 0)
            ;
         //   Toast.makeText(new MainActivity(), "网络连接失败！请检查网络设置或稍后再试！", Toast.LENGTH_SHORT).show();

        return stringBuffer.toString();
    }
}
