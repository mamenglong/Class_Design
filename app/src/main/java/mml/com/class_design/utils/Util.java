package mml.com.class_design.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**********************************************
 * @类名: Util
 * 
 * @描述: 工具
 * 
 * @作者： LXY
 * 
 * @创建日期： 2015-5-20 下午5:09:47
 * 
 * @版本： V1.0
 * 
 * @修订历史：
 * 
 ***********************************************/
public class Util {
	/**
	 * @方法名: list2Array
	 * @方法描述: 集合转数组
	 * @param list
	 * @return： String[]
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-6-5 下午1:06:54
	 */
	public static String[] list2Array(List<String> list) {
		String[] array = new String[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	/**
	 * @方法名: isFileExists
	 * @方法描述: 判断文件是否存在
	 * @param filePath
	 * @return： boolean
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-6-5 下午1:09:55
	 */
	public static boolean isFileExists(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * @方法名: insertSeparator
	 * @方法描述: 电话号码添加分隔符
	 * @param str
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-6-24 下午3:01:25
	 */
	public static String insertSeparator(String str) {
		str = str.replace("-", "");
		if (str.startsWith("0")) {
			str = str.substring(0, 4) + "-" + str.substring(4, 8) + "-"
					+ str.substring(8, str.length());
		} else {
			str = str.substring(0, 3) + "-" + str.substring(3, 7) + "-"
					+ str.substring(7, str.length());
		}
		return str;
	}

	/**
	 * @方法名: qpDecoding
	 * @方法描述: ENCODING=QUOTED-PRINTABLE解码
	 * @param str
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-6-26 下午3:43:17
	 */
	public static String qpDecoding(String str) {
		if (str == null) {
			return "";
		}
		try {
			str = str.replaceAll("=\n", "");
			byte[] bytes = str.getBytes("US-ASCII");
			for (int i = 0; i < bytes.length; i++) {
				byte b = bytes[i];
				if (b != 95) {
					bytes[i] = b;
				} else {
					bytes[i] = 32;
				}
			}
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			for (int i = 0; i < bytes.length; i++) {
				int b = bytes[i];
				if (b == '=') {
					try {
						int u = Character.digit((char) bytes[++i], 16);
						int l = Character.digit((char) bytes[++i], 16);
						if (u == -1 || l == -1) {
							continue;
						}
						buffer.write((char) ((u << 4) + l));
					} catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
					}
				} else {
					buffer.write(b);
				}
			}
			return new String(buffer.toByteArray(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @方法名: qpEncodeing
	 * @方法描述:ENCODING=QUOTED-PRINTABLE编码
	 * @param str
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-6-26 下午3:42:06
	 */
	public static String qpEncodeing(String str) {
		char[] encode = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < encode.length; i++) {
			if ((encode[i] >= '!') && (encode[i] <= '~') && (encode[i] != '=')
					&& (encode[i] != '\n')) {
				sb.append(encode[i]);
			} else if (encode[i] == '=') {
				sb.append("=3D");
			} else if (encode[i] == '\n') {
				sb.append("\n");
			} else {
				StringBuffer sbother = new StringBuffer();
				sbother.append(encode[i]);
				String ss = sbother.toString();
				byte[] buf = null;
				try {
					buf = ss.getBytes("utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (buf.length == 3) {
					for (int j = 0; j < 3; j++) {
						String s16 = String
								.valueOf(Integer.toHexString(buf[j]));
						// 抽取中文字符16进制字节的后两位,也就是=E8等号后面的两位,
						// 三个代表一个中文字符
						char c16_6;
						char c16_7;
						if (s16.charAt(6) >= 97 && s16.charAt(6) <= 122) {
							c16_6 = (char) (s16.charAt(6) - 32);
						} else {
							c16_6 = s16.charAt(6);
						}
						if (s16.charAt(7) >= 97 && s16.charAt(7) <= 122) {
							c16_7 = (char) (s16.charAt(7) - 32);
						} else {
							c16_7 = s16.charAt(7);
						}
						sb.append("=" + c16_6 + c16_7);
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * @方法名: getContent
	 * @方法描述: 获取输入框的内容
	 * @param editText
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-7 上午8:48:22
	 */
	public static String getContent(TextView editText) {
		if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
			return editText.getText().toString().trim();
		} else {
			return null;
		}
	}

	/**
	 * @方法名: getEditTextsContent
	 * @方法描述: 获取ViewGroup中所有EditText中的内容
	 * @param editText
	 * @param viewGroup
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-29 上午9:37:04
	 */
	public static String getEditTextsContent(EditText editText,
                                             ViewGroup viewGroup) {
		StringBuffer buffer = new StringBuffer();
		if (!TextUtils.isEmpty(editText.getText())) {
			buffer.append(editText.getText().toString() + ",");
		}
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			if (viewGroup.getChildAt(i) instanceof EditText) {
				if (!TextUtils.isEmpty(((EditText) (viewGroup.getChildAt(i)))
						.getText())
						&& !"null"
								.equals(((EditText) (viewGroup.getChildAt(i)))
										.getText())) {
					if (!buffer
							.toString()
							.trim()
							.contains(
									((EditText) (viewGroup.getChildAt(i)))
											.getText().toString().trim())) {
						buffer.append(getContent((EditText) viewGroup
								.getChildAt(i)) + ",");
					}
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * @方法名: setEditTextsContent
	 * @方法描述: 设置ViewGroup中所有EditText中的内容
	 * @param viewGroup
	 * @param editText
	 * @param string
	 * @return： String[]
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-29 上午9:37:40
	 */
	public static String[] setEditTextsContent(ViewGroup viewGroup,
                                               EditText editText, String string) {
		String[] arrStr;
		if (!TextUtils.isEmpty(string) && !"null".equals(string)) {
			arrStr = string.split(",");
			editText.setText(arrStr[0]);
			for (int i = 1; i < arrStr.length; i++) {
				if (viewGroup.getChildAt(i - 1) instanceof EditText) {
					((EditText) viewGroup.getChildAt(i - 1)).setText(arrStr[i]);
				}
			}
			return arrStr;
		} else {
			return null;
		}
	}

	/**
	 * @方法名: getBytes
	 * @方法描述: Bitmap转字节数组
	 * @param bitmap
	 * @return： byte[]
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-9 下午4:15:39
	 */
	public static byte[] getBytes(Bitmap bitmap) {
		// 实例化字节数组输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);// 压缩位图
		return baos.toByteArray();// 创建分配字节数组
	}

	/**
	 * @方法名: getBitmap
	 * @方法描述: 字节数组转Bitmap
	 * @param data
	 * @return： Bitmap
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-9 下午4:16:21
	 */
	public static Bitmap getBitmap(byte[] data) {
		return BitmapFactory.decodeByteArray(data, 0, data.length);// 从字节数组解码位图
	}

	/**
	 * @方法名: getSysFileDir
	 * @方法描述: 获取系统文件目录
	 * @param context
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-21 上午8:59:32
	 */
	public static String getSysFileDir(Context context) {
		return context.getFilesDir().getAbsolutePath() + "/";
	}

	/**
	 * @方法名: getSDCardFileDir
	 * @方法描述: 获取sd卡根目录
	 * @param context
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-21 上午9:03:33
	 */
	public static String getSDCardFileDir(Context context) {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/";
	}

	/**
	 * @方法名: getDataCache
	 * @方法描述: 获取对象缓存
	 * @param context
	 * @param fileName
	 * @return： Object
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-21 上午11:53:38
	 */
	public static Object getDataCache(Context context, String fileName) {
		fileName = getSysFileDir(context) + fileName;
		Object object = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					new File(fileName)));
			object = ois.readObject();
			ois.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * @方法名: saveDataCache
	 * @方法描述: 对象缓存
	 * @param context
	 * @param fileName
	 * @param object
	 *            ： void
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-21 上午11:53:50
	 */
	public static void saveDataCache(Context context, String fileName,
                                     Object object) {
		fileName = getSysFileDir(context) + fileName;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(new File(fileName)));
			oos.writeObject(object);
			oos.flush();
			oos.close();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @方法名: call
	 * @方法描述: 拨打电话
	 * @param context
	 * @param tel
	 *            ： void
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-29 上午9:38:05
	 */
	public static void call(Context context, String tel) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * @方法名: sendSms
	 * @方法描述: 发送短信
	 * @param context
	 * @param tel
	 *            ： void
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-29 上午9:38:11
	 */
	public static void sendSms(Context context, String tel) {
		Uri smsToUri = Uri.parse("smsto:" + tel);
		Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		context.startActivity(mIntent);
	}

	/**
	 * @方法名: cutString
	 * @方法描述: 去掉字符串前后的指定字符
	 * @param str
	 * @param cut
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-27 下午7:41:59
	 */
	public static String cutString(String str, String cut) {
		if (str.startsWith(cut)) {
			str = str.substring(1, str.length());
		}
		if (str.endsWith(cut)) {
			str = str.substring(0, str.length() - 1);
		}
		return str;

	}

	/**
	 * @方法名: cutString
	 * @方法描述: 截掉前三位字符
	 * @param str
	 * @param count
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-28 下午5:31:13
	 */
	public static String cutString(String str, int count) {

		return str.substring(count, str.length());

	}

	/**
	 * @方法名: getScreenHW
	 * @方法描述: 获取手机屏幕宽高
	 * @param ct
	 * @return： int[]
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-27 下午7:41:20
	 */
	public static int[] getScreenHW(Context ct) {
		int[] wh = new int[2];
		WindowManager manager = (WindowManager) ct
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		wh[0] = height;
		wh[1] = width;
		return wh;
	}

	/**
	 * @方法名: getPakageName
	 * @方法描述: 获得包名
	 * @param context
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-28 下午3:41:49
	 */
	public static String getPakageName(Context context) {
		return context.getPackageName();
	}

	/**
	 * @方法名: getVersionName
	 * @方法描述: 获得版本名
	 * @param context
	 * @return： String
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-28 下午3:42:00
	 */
	public static float getVersionName(Context context) {
		try {
			return Float.valueOf(context.getPackageManager().getPackageInfo(
					getPakageName(context), 0).versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * @方法名: getVersionCode
	 * @方法描述: 获得版本号
	 * @param context
	 * @return： int
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-7-28 下午3:42:12
	 */
	public static int getVersionCode(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					getPakageName(context), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * @方法名: saveMyBitmap2SDCard
	 * @方法描述: 保存图片到sd卡
	 * @param bitName
	 * @param mBitmap
	 *            ： void
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-8-16 上午11:37:37
	 */
	public static void saveMyBitmap2SDCard(String bitName, Bitmap mBitmap) {
		File f = new File(bitName);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 动态设置ListView组建的高度
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * @方法名: getImage
	 * @方法描述: 显示赞的图标
	 * @param context
	 * @return： SpannableStringBuilder
	 * @异常： 无
	 * @作者： LXY
	 * @创建日期： 2015-8-27 下午2:54:13
	 */
//	public static SpannableStringBuilder showZan(Context context,
//                                                 CharSequence text) {
//		SpannableStringBuilder builder = new SpannableStringBuilder(text);
//		String rexgString = "★";
//		Pattern pattern = Pattern.compile(rexgString);
//		Matcher matcher = pattern.matcher(text);
//		while (matcher.find()) {
//			builder.setSpan(new ImageSpan(context, R.drawable.zan_icon),
//					matcher.start(), matcher.end(),
//					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
//		return builder;
//	}

	/**
	 * 把对象序列化到本地
	 * 
	 * @param path
	 * @param saveObject
	 */
	public static final void saveObject(String path, Object saveObject) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		File f = new File(path);
		try {
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			if (!f.exists()) {
				f.createNewFile();
			}
			LogUtils.i("---f.getAbsolutePath---"+f.getAbsolutePath());
			fos = new FileOutputStream(f);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(saveObject);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 把本地文件反序列化到内存
	 * 
	 * @param path
	 * @return
	 */
	public static final Object restoreObject(String path) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Object object = null;
		File f = new File(path);
		if (!f.exists()) {
			return null;
		}
		try {
			fis = new FileInputStream(f);
			ois = new ObjectInputStream(fis);
			object = ois.readObject();
			return object;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return object;
	}
}
