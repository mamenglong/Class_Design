package mml.com.class_design.retrofit_interface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 项目名称：Class_Design
 * Created by Long on 2018/6/25.
 * 修改时间：2018/6/25 10:48
 */

public class UserResult {
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    private boolean result;
    private boolean isExist;

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exists) {
        isExist = exists;
    }

    public interface UserInterface {
        //@GET("Users/GetHasUser?userName={name}&pwd={pw}")
        @GET("Users/GetHasUser")
        Call<UserResult> getCall(@Query("userName") String name, @Query("pwd")String pw);
        // 注解里传入 网络请求 的部分URL地址
        // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
        // 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
        // getCall()是接受网络请求数据的方法

    }
    public interface InsertInterface {
        //@GET("Users/GetHasUser?userName={name}&pwd={pw}")
        @GET("Users/InsertUser")
        Call<UserResult> getCall(@Query("userName") String name, @Query("pwd")String pw);
        // 注解里传入 网络请求 的部分URL地址
        // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
        // 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
        // getCall()是接受网络请求数据的方法

    }
}

