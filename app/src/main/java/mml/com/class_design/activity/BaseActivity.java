package mml.com.class_design.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.security.PublicKey;

import mml.com.class_design.R;
import mml.com.class_design.application.MyApplication;
import mml.com.class_design.utils.ActivityManager;
import mml.com.class_design.utils.LogUtils;
import mml.com.class_design.utils.SPUtils;

/**
 * 项目名称：Class_Design
 * Created by Long on 2018/7/2.
 * 修改时间：2018/7/2 0:03
 */
public class BaseActivity extends AppCompatActivity {
    protected Toolbar mToolbar;
    protected ActionBar mActionBar;
//    private DrawerLayout drawerLayout;//侧滑根标签
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        LogUtils.i(getClass().getSimpleName());//每个活动名称
//        drawerLayout=findViewById(R.id.drawerLayout);
        ActivityManager.getInstance().addActivity(this);


    }

    public ActionBar initActioBar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar  actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return actionBar;
    }
    /**
     * 加载menu
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    /**
     * 菜单按钮点击事件
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:

                break;
            case R.id.add:
                Toast.makeText(this,"add",Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.changeUser:
                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                SPUtils.getInstance().put("autoLogin",false);
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivty(this);
    }

    private  long exitTime=0;//全局计时
    public boolean doubleClick(int keyCode, KeyEvent event,View v) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Snackbar.make(v, "再按一次退出程序！(๑ت๑)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
                //Toast.makeText(this,"再按一次退出程序！(๑ت๑)",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                Toast.makeText(this, "欢迎下次再来！(๑`･︶･´๑)", Toast.LENGTH_SHORT).show();
                ActivityManager.getInstance().exit();
            }
            return true;
        }
        return false;
    }
    /**
     * 隐藏软件盘
     */
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 点击软键盘之外的空白处，隐藏软件盘
     * @param ev
     * @return
     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (ToolUtil.isShouldHideInput(v, ev)) {
//
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
//    }

    /**
     * 显示软键盘
     */
    public void showInputMethod(){
        if (getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(),0);
        }
    }

    public void longToast(String mes){
        Toast.makeText(this,mes,Toast.LENGTH_LONG).show();
    }
    public void shortToast(String mes){
        Toast.makeText(this,mes,Toast.LENGTH_SHORT).show();
    }
}
