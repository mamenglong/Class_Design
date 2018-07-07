package mml.com.class_design.activity.createNew;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;


import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import mml.com.class_design.Model.RecyclerViewItem;
import mml.com.class_design.R;
import mml.com.class_design.activity.BaseActivity;
import mml.com.class_design.utils.LogUtils;

/**
 * 主Activity入口
 *
 * @author
 *
 */
@SuppressLint("SimpleDateFormat")
public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;
    private RichTextEditor editor;
    private View btn_gallery, btn_camera, btn_share;
    private TextView time,mUUId;
    private OnClickListener btnListener;

    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    private File mCurrentPhotoFile;// 照相机拍照得到的图片
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_activity_main);
        mActionBar=initActioBar();
        editor = (RichTextEditor) findViewById(R.id.richEditor);
        btnListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                editor.hideKeyBoard();
                if (v.getId() == btn_gallery.getId()) {
                    // 打开系统相册
                    requestPermission(REQUEST_EXTERNAL_STORAGE);

                } else if (v.getId() == btn_camera.getId()) {
                    // 打开相机
                    requestPermission(REQUEST_CAMERA);

                } else if (v.getId() == btn_share.getId()) {
                    List<RichTextEditor.EditData> editList = editor.buildEditData();
                    // 下面的代码可以上传、或者保存，请自行实现
                      //  dealEditData(editList);
                }
            }
        };

        btn_gallery = findViewById(R.id.button1);
        btn_camera = findViewById(R.id.button2);
        btn_share = findViewById(R.id.button3);
        time=findViewById(R.id.time);
        time.setText(getTime());
        btn_gallery.setOnClickListener(btnListener);
        btn_camera.setOnClickListener(btnListener);
        btn_share.setOnClickListener(btnListener);
        mUUId=findViewById(R.id.uuId);
        Intent detial=getIntent();
        if(detial.getStringExtra("uuId")!=null)
        {
            String uId=detial.getStringExtra("uuId");
            mUUId.setText(uId);
            List<RecyclerViewItem> itemList= LitePal.where("uuId=?",uId).find(RecyclerViewItem.class);
            editor.seeDetial(itemList);
        }
    }

    /****
     * 设置actionbar
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.delete).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1,REQUEST_CAMERA=2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private static String[] PERMISSIONS_CAMERA={Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private void requestPermission(int CASE){
        switch(CASE){
            case REQUEST_EXTERNAL_STORAGE:
                if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                    }
                    else {
                        ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                    }
                }
                else {
                    openGallery();
                }
                break;
            case REQUEST_CAMERA:
                // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
                // 向用户解释为什么需要这个权限
                if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(this, PERMISSIONS_CAMERA, REQUEST_CAMERA);
                    } else {
                        ActivityCompat.requestPermissions(this, PERMISSIONS_CAMERA, REQUEST_CAMERA);
                    }
                }
                else {
                    openCamera();
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case  REQUEST_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogUtils.i("Permission", "Storage Permission Sucessed");
                    Toast.makeText(this, "Storage Permission Sucessed", Toast.LENGTH_SHORT).show();
                    openGallery();
                }
                else
                {
                    LogUtils.i("Permission", "Permission Failed.");
                    Toast.makeText(this, "You must allow permission write external storage to your mobile device.", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogUtils.i("Permission", "Camera Permission Sucessed");
                    Toast.makeText(this, "Camera Permission Sucessed.", Toast.LENGTH_SHORT).show();
                    openCamera();
                }
                else
                {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "相机权限已被禁止", Toast.LENGTH_SHORT).show();
                    }
                    LogUtils.i("Permission", "Permission Failed");
                    Toast.makeText(this, "You must allow permission open camera to your mobile device.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                List<RichTextEditor.EditData> editList = editor.buildEditData();
                dealEditData(editList);
                finish();
                break;
            case R.id.add:
                if(editor.buildEditData().size()!=0) {
                    dealEditData(editor.buildEditData());
                    editor.removeAllView();
                }
                break;
            case R.id.delete:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("是否清空？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.removeAllView();
                        dialog.dismiss();
                    }
                });
               builder.setNegativeButton("手误了", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       shortToast("下次想好哦！");
                       dialog.dismiss();
                   }
               });
               builder.show();
                break;
            case R.id.setting:
                Toast.makeText(this,"setting",Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    protected void dealEditData(List<RichTextEditor.EditData> editList) {
        List<RecyclerViewItem> itemList=new ArrayList<>();
        String uuId=UUID.randomUUID().toString();
        if(!mUUId.getText().toString().equals("")) {
            uuId = mUUId.getText().toString();
            LitePal.deleteAll("RecyclerViewItem","uuId=?",uuId);
        }
        if(editList.size()==1&&editList.get(0).inputStr.length()==0)
            return;
        for (RichTextEditor.EditData itemData : editList) {
            if(itemData.inputStr!=""||itemData.imagePath!="") {
                RecyclerViewItem item = new RecyclerViewItem();
                item.setUuId(uuId);
                item.setBitmapPath(itemData.imagePath);
                item.setTitle(itemData.inputStr);
                item.setTime(itemData.time);
                itemList.add(item);
                item.save();
            }
        }//Create_New 100
//        if(itemList.size()!=0) {
//            Intent intent=new Intent();
//            intent.putExtra("uuId",uuId);
//            setResult(101,intent);
//        }
    }
    protected  void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }
    protected void openCamera() {
        try {
            // Launch camera to take photo for selected contact
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
        } catch (ActivityNotFoundException e) {
        }
    }

    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            insertBitmap(getRealFilePath(uri));
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            insertBitmap(mCurrentPhotoFile.getAbsolutePath());
        }
    }

    /**
     * 添加图片到富文本剪辑器
     *
     * @param imagePath
     */
    private void insertBitmap(String imagePath) {
        editor.insertImage(imagePath);
    }

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[] { ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    public static String getTime(){
        String result="";
        Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年
        int year = calendar.get(Calendar.YEAR);
//月
        int month = calendar.get(Calendar.MONTH)+1;
//日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
        int minute = calendar.get(Calendar.MINUTE);
//秒
        int second = calendar.get(Calendar.SECOND);
        result=year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second+" "+getWeek();
        return result;
    }
    /*获取星期几*/
    public static String getWeek() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }
}
