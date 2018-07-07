package mml.com.class_design.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.litepal.LitePal;

import mml.com.class_design.activity.LoginActivity;
import mml.com.class_design.activity.MainActivity;
import mml.com.class_design.activity.RegisterActivity;
import mml.com.class_design.activity.recyclerView.RecyclerDragActivity;
import mml.com.class_design.utils.ActivityManager;

/**
 * 项目名称：Class_Design
 * Created by Long on 2018/6/25.
 * 修改时间：2018/6/25 8:42
 */
public class MyApplication extends Application {
    private static int height,width;
    private static MyApplication application;
    private  LoginActivity loginActivity;
    private  RegisterActivity registerActivity;
    private MainActivity mainActivity;
    private RecyclerDragActivity recyclerDragActivity;
    public static MyApplication getApplication() {
        return application;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    public LoginActivity getLoginActivity() {
        return loginActivity;
    }

    public void setLoginActivity(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
        WindowManager manager = loginActivity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        this.width = outMetrics.widthPixels;
        this.height = outMetrics.heightPixels;
    }

    public RegisterActivity getRegisterActivity() {
        return registerActivity;
    }

    public void setRegisterActivity(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(application==null)
            application=this;
        LitePal.initialize(this);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public RecyclerDragActivity getRecyclerDragActivity() {
        return recyclerDragActivity;
    }

    public void setRecyclerDragActivity(RecyclerDragActivity recyclerDragActivity) {
        this.recyclerDragActivity = recyclerDragActivity;
    }
}
