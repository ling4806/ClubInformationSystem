package com.example.clubinformation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.clubinformation.R;
import com.example.clubinformation.base.Constants;
import com.example.clubinformation.clubcontent.InformationItem;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class UpdateActivity extends AppCompatActivity {

    private static int ifEdit =1;////是否编辑：0表示编辑；1表示新增，2表示查看
    private EditText et_title;
    private EditText et_information;
    private int position;
    private String User_ID;
    private InformationItem item;

    //利用 Intent 向当前活动传递数据
    public static void actionStart_read(Context context, InformationItem item, int ifEdit, int position ,String id) {
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("position", position);
        intent.putExtra("ID", id);
        UpdateActivity.ifEdit = ifEdit;
        context.startActivity(intent);
    }
    //利用 Intent 向当前活动传递数据
    public static void actionStart(Context context, InformationItem item, int ifEdit, int position,String id) {
                if(context instanceof Search_Activity ){
                    Toast.makeText(context, "请回主页编辑！", Toast.LENGTH_LONG).show();
                }else if(context instanceof ClubActivity){
                    Intent intent = new Intent(context, UpdateActivity.class);
                    intent.putExtra("item", item);
                    intent.putExtra("position", position);
                    intent.putExtra("ID", id);
                    UpdateActivity.ifEdit = ifEdit;
                    ((ClubActivity)context).startActivityForResult(intent, Constants.MYCONTACTS_REQUESTCODE_UPDATE);//??????????????
                }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_layout);
        et_title = (EditText) findViewById(R.id.et_title);
        et_information = (EditText) findViewById(R.id.et_information);
        Intent intent = getIntent();
        if (ifEdit==2) {
            //获得从 PhoneBookActivity 传递过来的信息
            item = (InformationItem) intent.getSerializableExtra("item");
            position = intent.getIntExtra("position", -1);
            et_title.setText(item.title);
            et_information.setText(item.content+"\n最后修改日期为"+item.time);
            et_information.setEnabled(false);
            et_title.setEnabled(false);
            et_information.requestFocus();
        }else if(ifEdit==0){
            item = (InformationItem) intent.getSerializableExtra("item");
            position = intent.getIntExtra("position", -1);
            et_title.setText(item.title);
            et_information.setText(item.content);
            et_information.requestFocus();
        }
        User_ID=intent.getStringExtra("ID");        //设置标题栏
        setTopBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ifEdit = 1;
    }

    //设置标题栏的两个按钮，都应绑定事件
    private void setTopBar() {
        TextView tvCancel = (TextView) findViewById(R.id.tv_topbar_left);
        TextView tvSave = (TextView) findViewById(R.id.tv_topbar_right);
        if(ifEdit==0||ifEdit==1){
            tvCancel.setText("取消");
            tvSave.setText("保存");
        }else{
            tvCancel.setText("返回");
            tvSave.setText("");
        }
        //“取消”点击事件
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//！！！！！销毁当前活动
            }
        });


        //“保存”点击事件
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = et_title.getText().toString();
                final String information = et_information.getText().toString();
                if (checkInput(title, information)) {
                    //显示“确认保存”的对话框
                    AlertDialog.Builder dialog = new AlertDialog.Builder(UpdateActivity.this);
                    dialog.setTitle("确认");
                    dialog.setMessage("是否保存？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            根据ifEdit的不同调用的方法不同
                            if (ifEdit==0) {
                                updateItem(item.ID, title, information);
                            } else if(ifEdit==1){
                                saveItem(title, information);
                            }
                            finish();//销毁当前活动
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    //检查输入的信息是否完整、有效
    private boolean checkInput(String name, String phonenumber) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(UpdateActivity.this, "姓名不能为空！", Toast.LENGTH_LONG).show();
            et_title.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(phonenumber)) {
            Toast.makeText(UpdateActivity.this, "号码不能为空！", Toast.LENGTH_LONG).show();
            et_information.requestFocus();
            return false;
        }
        return true;
    }

    // 保存数据
    private void saveItem(final String title, final String content) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String time= simpleDateFormat.format(date);
        InformationItem item = clubcontent.insertData(title, content,time,User_ID);//保存在数据库
        if (item == null) {
            Toast.makeText(UpdateActivity.this, "保存失败！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(UpdateActivity.this, "保存成功！", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.putExtra("item", item);
            setResult(RESULT_OK, intent);
        }
    }

    // 更新数据
    private void  updateItem(final String id, final String title, final String content) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        String time= simpleDateFormat.format(date);

        InformationItem item = clubcontent.updateData(id,title, content,time,User_ID);//调用静态函数更新数据库
        if (item == null) {
            Toast.makeText(UpdateActivity.this, "修改失败！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(UpdateActivity.this, "修改成功！", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
//            putExtra 返回数据
            intent.putExtra("item", item);
            intent.putExtra("position", position);
//            返回到上一个活动主活动MyContactsActivity去
            setResult(RESULT_OK, intent);
        }
    }
}


