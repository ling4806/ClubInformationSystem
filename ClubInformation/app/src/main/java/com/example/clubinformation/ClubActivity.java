package com.example.clubinformation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.clubinformation.base.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.clubinformation.clubcontent.InformationItem;

import java.util.List;

public class ClubActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ConstraintLayout layout;
    private ClubAdapt adapter;
    String User_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_layout);
        Toolbar toolbar = findViewById(R.id.mycontacts_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        User_ID= intent.getStringExtra("ID");
        // 获取 RecyclerView 实例
        recyclerView = (RecyclerView) findViewById(R.id.rv_contacts_list);
        // 获取显示“暂无记录”信息的布局实例
        layout = (ConstraintLayout) findViewById(R.id.mycontacts_norecord);

        // 调用构造函数时调用查询数据库函数，结果保存在content.item里，从数据库查询联系人信息
        clubcontent content = new clubcontent(this);
        // 创建 RecyclerView.Adapter适配器对象
        adapter = new ClubAdapt(this, content.getItems(),User_ID);
        // 定义布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置 RecyclerView 布局样式
        recyclerView.setLayoutManager(linearLayoutManager);
        //使用适配器对象 adapter 为 recyclerView 加载数据
        recyclerView.setAdapter(adapter);
        //设置默认分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //设置主布局 可见性，记录条数小于0显示无记录
        setViewVisible();
    }
    //设置布局可见性函数，记录条数小于0显示无记录
    public void setViewVisible() {
        if (adapter.getItemCount() > 0) {
            layout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }
    @Override//此方法用于初始化上下文菜单 并只会在第一次初始化菜单时调用
    public boolean onCreateOptionsMenu(Menu menu) {
        // 填充菜单，将菜单项显示在 ToolBar 上
        getMenuInflater().inflate(R.menu.menu_mycontacts, menu);
        return true;
    }

    //    调用函数启用编辑活动布局   菜单项被点击的时候被调用 菜单项被点击的时候被调用
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
//搜索
        if (id == R.id.action_sync_phonenumber) {
            Intent intent = new Intent(ClubActivity.this, Search_Activity.class);
            intent.putExtra("ID", User_ID);
            startActivity(intent);
            return true;
        }
//新增
        if (id == R.id.action_add_phonenumber) {
//            在此处显示启动编辑活动，请求活动结束后返回结果Constants.MYCONTACTS_REQUESTCODE_INSERT请求码
            Intent intent = new Intent(ClubActivity.this, UpdateActivity.class);
            intent.putExtra("ID", User_ID);
//            请求活动结束 后返回结果，Constants.MYCONTACTS_REQUESTCODE_INSERT请求码
            startActivityForResult(intent, Constants.MYCONTACTS_REQUESTCODE_INSERT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //    子活动返回后会重写onActivityResult方法
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.MYCONTACTS_REQUESTCODE_INSERT:
                if (resultCode == RESULT_OK) {
                    //传递的是对象需要序列化
                    InformationItem item = (InformationItem) data.getSerializableExtra("item");
//                    添加到adapter对象的列表属性
                    adapter.addItem(item);
                    setViewVisible();
                }
                break;
            case Constants.MYCONTACTS_REQUESTCODE_UPDATE:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("position", -1);
                    InformationItem item = (InformationItem) data.getSerializableExtra("item");
                    adapter.updateItem(item, position);
                }
                break;
        }
    }
}
