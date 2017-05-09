package com.example.mrye.xiaoyouquan;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mrye.xiaoyouquan.utils.LogUtil;
import com.example.mrye.xiaoyouquan.utils.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.media.CamcorderProfile.get;

public class KeChengBiaoActivity extends AppCompatActivity {

    @BindView(R.id.layout_schedule_content)
    RelativeLayout mRelativeLayout;

    @BindView(R.id.tv_1)
    TextView tv_1;

    @BindView(R.id.tv_2)
    TextView tv_2;

    @BindView(R.id.tv_3)
    TextView tv_3;

    @BindView(R.id.tv_4)
    TextView tv_4;

    @BindView(R.id.tv_5)
    TextView tv_5;

    @BindView(R.id.tv_6)
    TextView tv_6;

    @BindView(R.id.tv_7)
    TextView tv_7;

    private int mCellWidth;  //单元格宽度
    private RelativeLayout.LayoutParams mLayoutParams;
    private String mWay;
    private List<Map<String, String>> mList;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ke_cheng_biao);
        ButterKnife.bind(this);
        initWeek();  //初始化当前星期,将当前星期背景设置为灰色
        initCell();  //初始化课程表格
        initData();  //解析存储在SharedPreferences中的JSON数据,并将其存储在List<Map<String, String>> mList中
        addData();
    }

    private void addData() {
        for (int i = 0; i < mList.size(); i++) {
            int j;
            if (mList.get(i).get("xqjmc").equals("星期一")) {
                j = 1;
                addCourse(i, j);
            }else if (mList.get(i).get("xqjmc").equals("星期二")) {
                j = 2;
                addCourse(i, j);
            }else if (mList.get(i).get("xqjmc").equals("星期三")) {
                j = 3;
                addCourse(i, j);
            }else if (mList.get(i).get("xqjmc").equals("星期四")) {
                j = 4;
                addCourse(i, j);
            }else if (mList.get(i).get("xqjmc").equals("星期五")) {
                j = 5;
                addCourse(i, j);
            }else if (mList.get(i).get("xqjmc").equals("星期六")) {
                j = 6;
                addCourse(i, j);
            }else if (mList.get(i).get("xqjmc").equals("星期日")) {
                j = 7;
                addCourse(i, j);
            }

        }
    }

    private void addCourse(int i, int j) {
        String[] str = mList.get(i).get("jc").split("[^0-9]");
        int startJc = Integer.parseInt(str[0]);
        int endJc = Integer.parseInt(str[1]);
        TextView textView = new TextView(this);
        textView.setText(mList.get(i).get("kcmc")
                + "@" + mList.get(i).get("zcd")
                + "@" + mList.get(i).get("jc")
                + "@" + mList.get(i).get("cdmc")
                + "@" + mList.get(i).get("xm"));
        textView.setTextSize(10);
        textView.setPadding(6, 4, 6, 4);
        textView.setBackgroundResource(R.drawable.schedule_title_background);
        mLayoutParams = new RelativeLayout.LayoutParams(2 * mCellWidth, (endJc - startJc + 1) * 3 * mCellWidth);
        mLayoutParams.setMargins(mCellWidth * (2 * j - 1), (startJc - 1) * 3 * mCellWidth, 0, 0);
        mRelativeLayout.addView(textView, mLayoutParams);
    }

    private void initData() {
        String keBiaoData = PrefUtils.getString(KeChengBiaoActivity.this, "KeBiaoData", "");
        LogUtil.d("KeChengBiaoActivity", keBiaoData);
        JSONObject jsonObject1;
        try {
            jsonObject1 = new JSONObject(keBiaoData);
            JSONArray jsonArray = jsonObject1.getJSONArray("kbList");
            mList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, String> map = new HashMap<>();
                map.put("xqjmc", jsonObject2.getString("xqjmc"));  //星期一
                map.put("kcmc", jsonObject2.getString("kcmc"));  //数据仓库与数据挖掘
                map.put("zcd", jsonObject2.getString("zcd"));  //5-16周
                map.put("jc", jsonObject2.getString("jc"));  //1-2节
                map.put("xqmc", jsonObject2.getString("xqmc"));  //阳光校区
                map.put("cdmc", jsonObject2.getString("cdmc"));  //未排地点
                map.put("xm", jsonObject2.getString("xm"));  //叶威
                mList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initCell() {
        WindowManager wm = this.getWindowManager();
        mCellWidth = wm.getDefaultDisplay().getWidth() / 15;  //获取单元格宽度
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 12; j++) {
                TextView textView = new TextView(this);
                textView.setBackgroundResource(R.drawable.schedule_title_background);
                if (i == 0) {
                    mLayoutParams = new RelativeLayout.LayoutParams(mCellWidth, 3 * mCellWidth);
                    mLayoutParams.setMargins(0, 3 * j * mCellWidth, 0, 0);
                    textView.setText(j + 1 + "");
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.rgb(0, 0, 255));
                } else {
                    mLayoutParams = new RelativeLayout.LayoutParams(2 * mCellWidth, 3 * mCellWidth);
                    mLayoutParams.setMargins((2 * i - 1) * mCellWidth, 3 * j * mCellWidth, 0, 0);
                }
                mRelativeLayout.addView(textView, mLayoutParams);
            }
        }
    }

    private void initWeek() {
        Calendar c = Calendar.getInstance();
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));  //获取当前星期
        Log.d("aaa", "initWeek: " + mWay);
        if ("1".equals(mWay)) {
            tv_7.setBackgroundColor(Color.GRAY);
        } else if ("2".equals(mWay)) {
            tv_1.setBackgroundColor(Color.GRAY);
        } else if ("3".equals(mWay)) {
            tv_2.setBackgroundColor(Color.GRAY);
        } else if ("4".equals(mWay)) {
            tv_3.setBackgroundColor(Color.GRAY);
        } else if ("5".equals(mWay)) {
            tv_4.setBackgroundColor(Color.GRAY);
        } else if ("6".equals(mWay)) {
            tv_5.setBackgroundColor(Color.GRAY);
        } else if ("7".equals(mWay)) {
            tv_6.setBackgroundColor(Color.GRAY);
        }
    }
}
