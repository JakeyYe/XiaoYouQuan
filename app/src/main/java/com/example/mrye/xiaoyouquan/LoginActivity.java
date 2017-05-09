package com.example.mrye.xiaoyouquan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mrye.xiaoyouquan.utils.Constants;
import com.example.mrye.xiaoyouquan.utils.LogUtil;
import com.example.mrye.xiaoyouquan.utils.OkHttpUtil;
import com.example.mrye.xiaoyouquan.utils.PrefUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_username)
    EditText et_username;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.btn_username_clear)
    Button btn_username_clear;

    @BindView(R.id.btn_password_clear)
    Button btn_password_clear;

    @BindView(R.id.btn_password_eye)
    Button btn_password_eye;

    @BindView(R.id.btn_login)
    Button btn_login;

    private TextWatcher username_watcher;
    private TextWatcher password_watcher;
    private String mUsername;
    private String mPassword;

    /**
     * 用于存放请求头和请求参数
     */
    private static Map<String, String> requestHeadersMap, loginRequestBodyMap, scheduleRequestBodyMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //保存上一次登陆成功的用户名和密码
        et_username.setText(PrefUtils.getString(LoginActivity.this, "username", ""));
        et_password.setText(PrefUtils.getString(LoginActivity.this, "password", ""));
        PrefUtils.setString(LoginActivity.this, "username", "");
        PrefUtils.setString(LoginActivity.this, "password", "");
        //设置图标按钮点击事件
        btn_username_clear.setOnClickListener(this);
        btn_password_clear.setOnClickListener(this);
        btn_password_eye.setOnClickListener(this);
        //设置图标清除按钮的显示隐藏
        initWatcher();
        et_username.addTextChangedListener(username_watcher);
        et_password.addTextChangedListener(password_watcher);
        //设置登陆按钮点击事件
        btn_login.setOnClickListener(this);
        //初始化用于存放请求头和请求参数集合
        requestHeadersMap = new LinkedHashMap<String, String>();
        loginRequestBodyMap = new LinkedHashMap<>(); // java 1.7 开始可以通过这种写法初始化集合
        scheduleRequestBodyMap = new LinkedHashMap<>();
    }

    //设置图标清除按钮的显示隐藏，有输入则显示，无输入则隐藏
    private void initWatcher() {
        username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                et_password.setText("");
                if (s.toString().length() > 0) {
                    btn_username_clear.setVisibility(View.VISIBLE);
                } else {
                    btn_username_clear.setVisibility(View.INVISIBLE);
                }
            }
        };

        password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    btn_password_clear.setVisibility(View.VISIBLE);
                } else {
                    btn_password_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_username_clear:
                et_username.setText("");
                et_password.setText("");
                et_username.requestFocus();
                break;
            case R.id.btn_password_clear:
                et_password.setText("");
                break;
            case R.id.btn_password_eye:
                if (et_password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    btn_password_eye.setBackgroundResource(R.drawable.button_eye_s);
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {
                    btn_password_eye.setBackgroundResource(R.drawable.button_eye_n);
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                et_password.setSelection(et_password.getText().toString().length());
                break;
            case R.id.btn_login:
                login();
                break;
            default:
                break;
        }
    }

    //以武汉纺织大学为例，登陆信息门户--进入教务系统--获取课表数据（cookie不一致问题解决）
    private void login() {
        mUsername = et_username.getText().toString().trim();
        mPassword = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(mUsername)) {
            et_username.setError("学号不能为空");
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {
            et_password.setError("密码不能为空");
            return;
        }
        requestHeadersMap.put(Constants.HEADER_NAME_HOST, Constants.HEADER_VALUE_HOST);
        requestHeadersMap.put(Constants.HEADER_NAME_REFERER, Constants.HEADER_VALUE_REFERER);
        requestHeadersMap.put(Constants.HEADER_NAME_AGENT, Constants.HEADER_VALUE_AGENT);
        loginRequestBodyMap.put(Constants.LOGIN_BODY_NAME_IDToken0, Constants.LOGIN_BODY_VALUE_IDToken0);
        loginRequestBodyMap.put(Constants.LOGIN_BODY_NAME_IDButton, Constants.LOGIN_BODY_VALUE_IDButton);
        loginRequestBodyMap.put(Constants.LOGIN_BODY_NAME_goto, Constants.LOGIN_BODY_VALUE_goto);
        loginRequestBodyMap.put(Constants.LOGIN_BODY_NAME_encoded, Constants.LOGIN_BODY_VALUE_encoded);
        loginRequestBodyMap.put(Constants.LOGIN_BODY_NAME_gx_charset, Constants.LOGIN_BODY_VALUE_gx_charset);
        loginRequestBodyMap.put(Constants.LOGIN_BODY_NAME_USERNAME, mUsername);
        loginRequestBodyMap.put(Constants.LOGIN_BODY_NAME_PASSWORD, mPassword);
        OkHttpUtil.postAsync(Constants.EDUCATION_SYSTEM_LOGIN_URL, new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(LoginActivity.this, "世界上最遥远的距离就是没有网", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(byte[] response) {
                if (null != response) {
                    try {
                        String result = new String(response, "utf-8");
                        Document doc = Jsoup.parse(result);
                        String title = doc.title();
                        LogUtil.d("LoginActivity", title);
                        if ("\uFEFF武汉纺织大学统一身份认证".equals(title)) {
                            Toast.makeText(LoginActivity.this, "学号或密码不正确", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            //进入教务系统
                            searchEduSystem(LoginActivity.this);
                            //保存用户名和密码
                            PrefUtils.setString(LoginActivity.this, "username", mUsername);
                            PrefUtils.setString(LoginActivity.this, "password", mPassword);
                            //跳转到主界面
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            //关闭登录界面
                            finish();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "服务器错误，请重试!", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.finish();
                }
            }


        }, loginRequestBodyMap, requestHeadersMap);
    }

    private void searchEduSystem(final Context context) {
        //http://jwglxt.wtu.edu.cn/ssoserver/login?ywxt=jw&url=xtgl/index_initMenu.html
        OkHttpUtil.getAsync("http://jwglxt.wtu.edu.cn/ssoserver/login?ywxt=jw&url=xtgl/index_initMenu.html", new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(context, "进入教务系统失败，请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(byte[] response) {
                //查看课表
                searchScheduleOperation(LoginActivity.this);
            }
        });
    }

    public void searchScheduleOperation(final Context context) {
        requestHeadersMap.put(Constants.HEADER_NAME_HOST, "jwglxt.wtu.edu.cn");
        requestHeadersMap.put(Constants.HEADER_NAME_REFERER, "http://jwglxt.wtu.edu.cn/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdmKey=N253508");
        Calendar calendar = Calendar.getInstance();
        int month = 1 + calendar.get(Calendar.MONTH);  //获取当前月份,2-8月为第二学期，9-3月为第一学期
        String team;
        if (month >= 2 && month <= 8) {
            team = "12";
        } else {
            team = "3";
        }
        int currentYear = calendar.get(Calendar.YEAR);
        if (month >= 1 && month <= 8) {
            currentYear--;  //1到8月学年为当前年份减1
        }
        scheduleRequestBodyMap.put(Constants.SCHEDULE_BODY_NAME_SCHOOLYEAR, "" + currentYear);
        scheduleRequestBodyMap.put(Constants.SCHEDULE_BODY_NAME_TERM, team);  //3为第一学期--12为第二学期

        OkHttpUtil.postAsync("http://jwglxt.wtu.edu.cn/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdmKey=N253508", new OkHttpUtil.ResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(context, "获取课表失败,请重试!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(byte[] response) {
                try {
                    String result = new String(response, "utf-8");
                    LogUtil.d("LoginActivity", result);
                    //将课表数据存储在SharedPreferences中
                    PrefUtils.setString(LoginActivity.this, "KeBiaoData", result);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, scheduleRequestBodyMap, requestHeadersMap);
    }
}
