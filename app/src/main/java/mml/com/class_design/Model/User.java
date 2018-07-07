package mml.com.class_design.Model;

import org.litepal.crud.LitePalSupport;

/**
 * 项目名称：Class_Design
 * Created by Long on 2018/6/24.
 * 修改时间：2018/6/24 10:57
 */
public class User extends LitePalSupport {
    private String userName;
    private String passWord;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
