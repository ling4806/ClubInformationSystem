package com.example.clubinformation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static SQLiteDatabase database;//数据库对象
    private EditText getName,getPassword;
    private int getSex;
    private Button joinButton,regesterButton,deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 调用构造函数时调用查询数据库函数，结果保存在content.item里，从数据库查询联系人信息
        usercontent content = new usercontent(this);
        database=content.db;
        joinButton=(Button) findViewById(R.id.login);
        regesterButton = (Button) findViewById(R.id.register);
        deleteButton = (Button) findViewById(R.id.delect);
    }
        //添加数据到数据库
    public void button_register_Click(View view){
        getName = (EditText) findViewById(R.id.editTextName);
        getPassword = (EditText) findViewById(R.id.editTextpass);

        if(TextUtils.isEmpty(getName.getText().toString())){
            Toast.makeText(MainActivity.this,"请输入用户名！", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(getPassword.getText().toString())){
            Toast.makeText(MainActivity.this,"请输入密码！", Toast.LENGTH_LONG).show();
        } else{
//创建对象
            final String  userName =getName.getText().toString();
            final String  userPassword =getPassword.getText().toString();
            //将数据保存到数据库
            ContentValues values = new ContentValues();
            values.put("user_Name",getName.getText().toString());
            values.put("user_Password",getPassword.getText().toString());
            Cursor cursor = database.query("user_table", null, null, null, null, null, null);
            boolean exit=false;
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex("user_Name"));
                    if(getName.getText().toString().equals(name)){
                        Toast.makeText(MainActivity.this, "用户名重复，注册失败！", Toast.LENGTH_LONG).show();
                        exit=true;
                        break;
                    }
                } while (cursor.moveToNext());
            }
            if (!exit) {//如果要保存的用户在数据库中
                long flag = database.insert("user_table", null, values);
                if(flag>0){
                Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_LONG).show();}
                else {//如果不在数据库中，改为添加操作
                    Toast.makeText(MainActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
    //数据库数据登录
    public void button_login_Click(View view){
        getName = (EditText) findViewById(R.id.editTextName);
        getPassword = (EditText) findViewById(R.id.editTextpass);

        if(TextUtils.isEmpty(getName.getText().toString())){
            Toast.makeText(MainActivity.this,"请输入用户名！", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(getPassword.getText().toString())){
            Toast.makeText(MainActivity.this,"请输入密码！", Toast.LENGTH_LONG).show();
        } else{
//创建对象
            final String  userName =getName.getText().toString();
            final String  userPassword =getPassword.getText().toString();
            boolean exit=false;
            Cursor cursor = database.query("user_table", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex("user_Name"));
                    String pass = cursor.getString(cursor.getColumnIndex("user_Password"));
                    if(getName.getText().toString().equals(name)){
                        if(getPassword.getText().toString().equals(pass)){
                            Intent intent = new Intent(MainActivity.this, ClubActivity.class);
                            String ID = String.valueOf(cursor.getString(cursor.getColumnIndex("ID")));
                            intent.putExtra("ID", ID);
                            startActivity(intent) ;
                            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                            exit=true;
                            break;
                        }else{
                            Toast.makeText(MainActivity.this, "密码错误！", Toast.LENGTH_LONG).show();
                        }
                    }
                } while (cursor.moveToNext());

            }
            if (!exit) {//如果要保存的用户在数据库中
                    Toast.makeText(MainActivity.this, "用户不存在，请先注册！！", Toast.LENGTH_LONG).show();}
            }

        }

    //将数据从数据库删除
    public void buttonDelete_Click(View view){
//创建对象
        final String  userName =getName.getText().toString();
        final String  userPassword =getPassword.getText().toString();
        int is_succeed=database.delete("user_table","user_Name=?",new String[]{getName.getText().toString(),getPassword.getText().toString()});
        if(is_succeed>0){
            Toast.makeText(MainActivity.this,"删除成功", Toast.LENGTH_LONG).show();
            }else {//如果不在数据库中，改为添加操作
            Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_LONG).show();
        }
    }
}
