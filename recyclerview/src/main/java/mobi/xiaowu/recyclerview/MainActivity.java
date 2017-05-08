package mobi.xiaowu.recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mobi.xiaowu.recyclerview.adapter.RecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private List<String> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("hehe",""+BuildConfig.IM_SERVER_PORT);
        init();
    }

    private void init() {
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mList = new ArrayList<>();
        mAdapter = new RecyclerAdapter(this,mList);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);//线性

        GridLayoutManager gm = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);//网格

        StaggeredGridLayoutManager sgm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);//瀑布流
//        mRecyclerView.setLayoutManager(lm);
//        mRecyclerView.setLayoutManager(gm);
        mRecyclerView.setLayoutManager(sgm);
        mRecyclerView.setAdapter(mAdapter);
    }
    public void click(View view){
        Toast.makeText(this, "appcode+"+BuildConfig.API_SERVER_URL, Toast.LENGTH_SHORT).show();
        switch (view.getId()) {
            case R.id.btn_01:
                for (int i = 0; i < 5; i++) {
                    mList.add(String.format(Locale.CHINA,"当前这是第%03d的数字",i));
                }
                break;
            case R.id.btn_02:
                for (int i = 0; i < 5; i++) {
                    mList.remove(i);
                }
                break;
        }
        mAdapter.notifyDataSetChanged();
    }
}
