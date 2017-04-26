package com.example.mrye.xiaoyouquan;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrye.xiaoyouquan.adpter.MenuItemAdapter;
import com.example.mrye.xiaoyouquan.fragment.DongTaiFragment;
import com.example.mrye.xiaoyouquan.fragment.QuanZiFragment;
import com.example.mrye.xiaoyouquan.fragment.XiaoZhiTiaoFragment;
import com.example.mrye.xiaoyouquan.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_tab_layout)
    BottomNavigationView navigation;
    @BindView(R.id.nav_header_and_menu)
    ListView mLvHeaderAMenu;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    FragmentManager fragmentManager;
    QuanZiFragment quanZiFragment;
    XiaoZhiTiaoFragment xiaoZhiTiaoFragment;
    DongTaiFragment dongTaiFragment;

    //ButtonNavigationView底部导航栏事件监听器
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    hideFragments(transaction);
                    switch (item.getItemId()) {
                        case R.id.navigation_quanzi:
                            if (quanZiFragment == null) {
                                quanZiFragment = new QuanZiFragment();
                                transaction.add(R.id.fragment_content, quanZiFragment);
                            } else {
                                transaction.show(quanZiFragment);
                            }
                            toolbar.setTitle(R.string.quanzi);
                            return true;
                        case R.id.navigation_xiaozhitiao:
                            if (xiaoZhiTiaoFragment == null) {
                                xiaoZhiTiaoFragment = new XiaoZhiTiaoFragment();
                                transaction.add(R.id.fragment_content, xiaoZhiTiaoFragment);
                            } else {
                                transaction.show(xiaoZhiTiaoFragment);
                            }
                            toolbar.setTitle(R.string.xiaozhitiao);
                            return true;
                        case R.id.navigation_dongtai:
                            if (dongTaiFragment == null) {
                                dongTaiFragment = new DongTaiFragment();
                                transaction.add(R.id.fragment_content, dongTaiFragment);
                            } else {
                                transaction.show(dongTaiFragment);
                            }
                            toolbar.setTitle(R.string.dongtai);
                            return true;
                    }
                    transaction.commit();//提交事务
                    return false;
                }
            };

    private void hideFragments(FragmentTransaction transaction) {
        //hide()和show()方法来隐藏和显示Fragment，这就不会让Fragment的生命周期重走一遍
        if (quanZiFragment != null) {
            if (!quanZiFragment.isHidden()) {
                transaction.hide(quanZiFragment);
            }
        }
        if (dongTaiFragment != null) {
            if (!dongTaiFragment.isHidden()) {
                transaction.hide(dongTaiFragment);
            }
        }
        if (xiaoZhiTiaoFragment != null) {
            if (!xiaoZhiTiaoFragment.isHidden()) {
                transaction.hide(xiaoZhiTiaoFragment);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);

        //ButtonNavigationView底部导航栏
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //侧边导航栏
        setUpDrawer();

        fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        quanZiFragment = new QuanZiFragment();
        transaction.add(R.id.fragment_content, quanZiFragment);
        //三个重要的Fragment添加到Fragment管理栈中
        transaction.addToBackStack(null);
        transaction.commit();
        toolbar.setTitle(R.string.quanzi);
    }

    private void setUpDrawer() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mLvHeaderAMenu.addHeaderView(inflater.inflate(R.layout.nav_header, mLvHeaderAMenu, false));
        mLvHeaderAMenu.setAdapter(new MenuItemAdapter(this));
        mLvHeaderAMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                LogUtil.d("MainActivity", "" + position + "=====" + id);
                //1.私语墙 2.跳蚤市场 3.失物招领 4.课程表 5.顺风快递
                switch (position) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        Intent intent = new Intent(MainActivity.this, KeChengBiaoActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        //navigationView.getMenu().getItem(0).setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick2NavMenu(View view) {
        if (view.getId() == R.id.nav_menu_setup) {
            Toast.makeText(this, ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.nav_menu_mode) {
            Toast.makeText(this, ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
