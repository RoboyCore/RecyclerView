package mobi.xiaowu.recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import mobi.xiaowu.recyclerview.adapter.Ex01Adapter;
import mobi.xiaowu.recyclerview.model.Focus;
import mobi.xiaowu.recyclerview.utils.JsonAsyncTask;

public class Ex01Activity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, Ex01Adapter.OnItemClickListener, Ex01Adapter.OnItemLongClickListener {
    private RecyclerView mRecyclerView;
    private List<Focus.Item> mList;
    private Ex01Adapter mAdapter;
    private RadioGroup mRadioGroup;
    private RecyclerView.OnScrollListener mOnScrollListener;//滚动事件
    private LinearLayoutManager mLm;
    private StaggeredGridLayoutManager mSgm;
    private GridLayoutManager mGm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex01);
        init();
        loadData();//数据下载
    }

    private void loadData() {
        Toast.makeText(getApplicationContext(), "hehe"+Ex01Url.URL, Toast.LENGTH_SHORT).show();
        new JsonAsyncTask(new JsonAsyncTask.Callback() {
            @Override
            public void sendData(byte[] json) {
                try {
                    Focus focus = new Gson().fromJson(new String(json, "utf-8"), Focus.class);
                    mList.addAll(focus.getList());
                    mAdapter.notifyDataSetChanged();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        },this).execute(Ex01Url.URL);
    }

    private void init() {
        mList = new ArrayList<>();
        mAdapter = new Ex01Adapter(this,mList);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRadioGroup = (RadioGroup)findViewById(R.id.rg);
        mRadioGroup.setOnCheckedChangeListener(this);

        //初始化布局管理器
        mLm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mGm = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        mGm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position==0? 2:1;
            }
        });
        mSgm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);


        mRecyclerView.setLayoutManager(mLm);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(this);//adapter传过来的点击事件
        mAdapter.setOnLongClickListener(this);//adapter传过来的长按事件
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置默认动画

        //!!!滚动事件是内部类，java不支持多继承，所以要自己创建类对象再添加相应方法
        mOnScrollListener = new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {//!!!必须监听状态停止不然会刷新多次
                    int position = mLm.findLastVisibleItemPosition();//!!!布局管理器才能得到Item的当前位置关系，RecyclerView没有监听Item变化的
                    if (position == mList.size() - 1) {
                        Toast.makeText(Ex01Activity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                }
            }
        };

        mRecyclerView.addOnScrollListener(mOnScrollListener);//初始化后再添加滚动方法
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_01://线性布局管理
                mRecyclerView.setLayoutManager(mLm);//改变成线性布局
                break;
            case R.id.rb_02://网格布局管理

                mRecyclerView.setLayoutManager(mGm);//改变成网格布局
                break;
            case R.id.rb_03://稀疏布局管理(瀑布流)
                mRecyclerView.setLayoutManager(mSgm);//改变成稀疏布局
                break;
        }
    }


    @Override
    public void MyItemClick(RecyclerView recyclerView, View v, int position, Focus.Item data) {
        Toast.makeText(this, "当前"+position +data.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void MyItemLongClick(RecyclerView recyclerView, View v, int position, Focus.Item data) {
        Toast.makeText(this, "当前长按"+position +data.getTitle(), Toast.LENGTH_SHORT).show();
        mList.remove(position);
        mAdapter.notifyItemChanged(position);
        if (position != mList.size()-1) {
            mAdapter.notifyItemRangeChanged(position,mList.size()-position);
        }

    }
}
