package mml.com.class_design.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mml.com.class_design.Model.RecyclerViewItem;
import mml.com.class_design.R;
import mml.com.class_design.adapter.RecyclerItemAdapter;
import mml.com.class_design.application.MyApplication;
import mml.com.class_design.common.Common;
import mml.com.class_design.utils.SPUtils;

public class MainActivity extends BaseActivity {
    private DrawerLayout drawerLayout;//侧滑根标签
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新
    private RecyclerView recyclerView;//列表控件
    private List<RecyclerViewItem> recyclerViewItems=new ArrayList<>();
    private RecyclerItemAdapter recyclerItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getApplication().setMainActivity(this);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawerLayout);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        setSwipeRefresh(swipeRefreshLayout);//下拉刷新
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);//侧栏
        setNavigationItemSelected(navView);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            //显示导航
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置导航图标
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        recyclerView=findViewById(R.id.recyclerView);
        for(int i=0;i<10;i++)
            recyclerViewItems.add(new RecyclerViewItem("这是第"+i+"个列表","2018-2-1"));
        recyclerItemAdapter=new RecyclerItemAdapter(recyclerViewItems);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
        recyclerView.setAdapter(recyclerItemAdapter);
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
                drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.add:
                Toast.makeText(this,"backup",Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.setting:
                Toast.makeText(this,"setting",Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
        }
        return true;
    }

    /**
     * 双击退出
     * */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(doubleClick(keyCode,event,drawerLayout))
            return true;
//        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
//            if((System.currentTimeMillis()-exitTime)>2000){
//                Snackbar.make(drawerLayout, "再按一次退出程序！(๑ت๑)", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null)
//                        .show();
//                //Toast.makeText(this,"再按一次退出程序！(๑ت๑)",Toast.LENGTH_SHORT).show();
//                exitTime=System.currentTimeMillis();
//            }
//            else
//            {
//                Toast.makeText(MyApplication.getApplication().getMainActivity(),"欢迎下次再来！(๑`･︶･´๑)",Toast.LENGTH_SHORT).show();
//                ActivityManager.getInstance().exit();
//            }
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    /**设置SwipeRefreshLayout刷新操作
     * */
    public void setSwipeRefresh(final SwipeRefreshLayout refreshLayout){

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               refreshLayout.setRefreshing(false);
            }
        });
    }
    /**设置NavigationItem的点击事件
     * */
    private int navItemId;
    public void setNavigationItemSelected(NavigationView navView){
        navView.setCheckedItem(R.id.nav_list);
        navItemId=R.id.nav_list;
        //点击邮箱发邮件
        View headerView=navView.inflateHeaderView(R.layout.nav_header);
        LinearLayout linearLayout=headerView.findViewById(R.id.icon_mail);
        TextView mail=headerView.findViewById(R.id.mail);
        mail.setText(SPUtils.getInstance().getString("userName")!=""?SPUtils.getInstance().getString("userName"):"未登录");

//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Intent.ACTION_SENDTO);
//                it.putExtra(Intent.EXTRA_EMAIL, getResources().getString(R.string.nav_header_email));
//                //   it.setType("text/plain");
//                startActivity(Intent.createChooser(it, "Choose Email Client"));
//            }
//        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_list:
                        navItemId=R.id.nav_list;
                        Toast.makeText(MainActivity.this,"nav_list",Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case R.id.nav_new:
                        navItemId=R.id.nav_new;
                        Toast.makeText(MainActivity.this,"nav_new",Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case R.id.nav_backup:
                        navItemId=R.id.nav_backup;
                        Toast.makeText(MainActivity.this,"nav_backup",Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case R.id.nav_reGet:
                        navItemId=R.id.nav_reGet;
                       shortToast("nav_reGet");
                        break;
                    case R.id.nav_donate:
                        Common.openALiPay(MainActivity.this);
                        break;
                    case R.id.contactMe:
                        Common.contactMe();
                        break;
                    case R.id.about:
                     //   Common.showNoticeDialog(MainActivity.this);
                        //虽然这里的参数是AlertDialog.Builder(Context context)但我们不能使用getApplicationContext()获得的Context,而必须使用Activity.this,因为只有一个Activity才能添加一个窗体。
                        break;
                    default:
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

}
