package com.example.mrye.xiaoyouquan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mrye.xiaoyouquan.utils.HttpUtil;
import com.example.mrye.xiaoyouquan.utils.LogUtil;
import com.example.mrye.xiaoyouquan.utils.PrefUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        et_username.setText(PrefUtils.getString(LoginActivity.this, "username", ""));
        et_password.setText(PrefUtils.getString(LoginActivity.this, "password", ""));
        PrefUtils.setString(LoginActivity.this, "username", "");
        PrefUtils.setString(LoginActivity.this, "password", "");
        btn_username_clear.setOnClickListener(this);
        btn_password_clear.setOnClickListener(this);
        btn_password_eye.setOnClickListener(this);
        initWatcher();
        et_username.addTextChangedListener(username_watcher);
        et_password.addTextChangedListener(password_watcher);

        btn_login.setOnClickListener(this);

    }

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
        String URL = "http://ids.wtu.edu.cn/amserver/UI/Login?IDToken1=" + mUsername + "&IDToken2=" + mPassword + "&goto=http://my.wtu.edu.cn/index.portal&gx_charset=UTF-8";
        HttpUtil.sendOkHttpRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "世界上最遥远的距离就是没有网", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Document doc = Jsoup.parse(response.body().string());
                String title = doc.title();
                LogUtil.d("LoginActivity", title);
                if ("\uFEFF武汉纺织大学统一身份认证".equals(title)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "学号或密码不正确", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    PrefUtils.setString(LoginActivity.this, "username", mUsername);
                    PrefUtils.setString(LoginActivity.this, "password", mPassword);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
