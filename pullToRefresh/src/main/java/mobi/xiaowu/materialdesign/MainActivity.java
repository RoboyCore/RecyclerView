package mobi.xiaowu.materialdesign;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PullToRefreshListView prlv;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private Handler mHandler;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        init();

    }

    private void init() {
        prlv = (PullToRefreshListView) findViewById(R.id.refresh);
        prlv.setMode(PullToRefreshBase.Mode.BOTH);
//        prlv.setPullToRefreshOverScrollEnabled();
        prlv.setPullLabel("加载完成");
        prlv.setRefreshingLabel("正在刷新");
        prlv.setReleaseLabel("下拉刷新");
        prlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(2);
            }
        });
        prlv.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                list.remove(position-1);
                list.remove((int)id);
                System.out.println("parent = " + position +"//" + ((int) id));
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        prlv.setAdapter(adapter);

    }

    private void loadData(final int type) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                switch (type) {
                    case 1://下拉刷新
                        for (int i = time; i < 5+time; i++) {
                            list.add("这是最新的数据" + i);
                        }
                        time += 5;
                        System.out.println("type = " + type);
                        break;
                    case 2://上拉加载更多
                        for (int i = time; i < 10 +time; i++) {
                            list.add("这是加载的数据" + i);
                        }
                        time += 10;
                        break;
                }
                adapter.notifyDataSetChanged();
//                prlv.setRefreshing(false);
                System.out.println("type =2222 " + type);
                prlv.onRefreshComplete();

            }
        },1000);

    }
}
