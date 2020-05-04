package com.example.clubinformation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Search_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ConstraintLayout layout;
    private ClubAdapt adapter;
    private List<clubcontent.InformationItem> mValues;
    String User_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        Toolbar toolbar = findViewById(R.id.search_toolbar);
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
        recyclerView = (RecyclerView) findViewById(R.id.rv_search_list);
        // 获取显示“暂无记录”信息的布局实例
        layout = (ConstraintLayout) findViewById(R.id.search_norecord);
////设置主布局 可见性，记录条数小于0显示无记录
//        setViewVisible();
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
    public void search_Click(View view){
        EditText cin=findViewById(R.id.search_content);
        // 调用构造函数时调用查询数据库函数，结果保存在content.item里，从数据库查询联系人信息
        clubcontent content = new clubcontent(this);
        mValues=content.getItems();
        List<clubcontent.InformationItem> x=search(mValues,cin.getText().toString());
        // 创建 RecyclerView.Adapter适配器对象
        adapter = new ClubAdapt(this, x,User_ID);
        // 定义布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置 RecyclerView 布局样式
        recyclerView.setLayoutManager(linearLayoutManager);
        //使用适配器对象 adapter 为 recyclerView 加载数据
        recyclerView.setAdapter(adapter);
        //设置默认分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        setViewVisible();

    }

    public List<clubcontent.InformationItem> search(List<clubcontent.InformationItem> m,String w){
        List<clubcontent.InformationItem> x=new ArrayList<clubcontent.InformationItem>();
        String str=".*"+w+".*";  //判断字符串中是否含有ll
        String z;
        for (int i = 0; i < m.size()-1; i++) {
            z=m.get(i).title;
            if (Pattern.matches(str,m.get(i).title)) {
                x.add(m.get(i));
            }
        }
        return x;
    }

}
