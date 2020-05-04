package com.example.clubinformation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.example.clubinformation.base.Constants;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import com.example.clubinformation.clubcontent.InformationItem;

import java.util.List;

public class ClubAdapt extends RecyclerView.Adapter<ClubAdapt.MyViewHolder> {

    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private List<InformationItem> mValues;
    private Context context;
    String User_ID;


    public ClubAdapt(Context contex, List<InformationItem> items,String id) {
        this.context = contex;
        mValues = items;
        User_ID=id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_club_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override//用于对 RecyclerView 子项的数据进行赋值。该方法会在每个子项被滚动到屏幕内的时候执行
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (mValues != null && (0 <= position && position < mValues.size())) {
            holder.mItem = mValues.get(position);
            binderHelper.bind(holder.swipeLayout, holder.mItem.ID);
            holder.bind(position);
        }
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;
        return mValues.size();
    }
    //添加到联系人数据列表
    public void addItem(InformationItem item) {
        mValues.add(item);
        notifyDataSetChanged();//刷新ui界面
    }

    public void updateItem(InformationItem item, int position) {
        if (position != -1) {
//            修改
            mValues.get(position).title = item.title;
            mValues.get(position).content = item.content;
//            更新数据库和界面？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？
            notifyItemChanged(position);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView;
        public InformationItem mItem;
        private SwipeRevealLayout swipeLayout;
        private View frontLayout;//拥有删除和编辑的前端布局
        private TextView deleteView;
        private TextView editView;
        //获取元素
        public MyViewHolder(View view) {
            super(view);
            swipeLayout = (SwipeRevealLayout) view.findViewById(R.id.swipe_layout);
            frontLayout = view.findViewById(R.id.front_layout);
            deleteView = (TextView) view.findViewById(R.id.information_delete);
            editView = (TextView) view.findViewById(R.id.information_edit);
            mNameView = (TextView) view.findViewById(R.id.tv_information_title);
        }
        //根据列表位置点击事件的处理
        public void bind(final int position) {
            mNameView.setText(mItem.title);
            // 为列表子项布局添加点击事件
            frontLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItem = mValues.get(position);
                    UpdateActivity.actionStart_read(context, mItem, 2, position,User_ID);
                }
            });
            // “删除”事件
            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItem = mValues.get(position);
                 if(User_ID.equals(mItem.userID)){
                     int flag = clubcontent.deleteData(mItem.ID);//删除指定数据的id
                     if (flag == 0) {
                         Toast.makeText(context, "删除失败！", Toast.LENGTH_LONG).show();
                     } else{
                         Toast.makeText(context, "删除成功！", Toast.LENGTH_LONG).show();
                         mValues.remove(mItem);//从当前集合移出
                         notifyDataSetChanged();//更新UI界面
                         //如果删除后为空
                         if (getItemCount() == 0) {
                             ClubActivity activity = (ClubActivity) context;
                             activity.setViewVisible();
                     }
                    }
                 }else{
                     Toast.makeText(context, "你没有权限删除！", Toast.LENGTH_LONG).show();
                 }
                }
            });
            //"编辑"事件
            editView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItem = mValues.get(position);
                    if(User_ID.equals(mItem.userID)){
                    UpdateActivity.actionStart(context, mItem, 0, position,User_ID);
                    }else{
                        Toast.makeText(context, "你没有权限编辑！", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
}