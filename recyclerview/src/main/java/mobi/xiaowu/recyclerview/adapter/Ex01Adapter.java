package mobi.xiaowu.recyclerview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import mobi.xiaowu.recyclerview.R;
import mobi.xiaowu.recyclerview.model.Focus;
import mobi.xiaowu.recyclerview.utils.ImgAsync;

/**
 * Created by xiaowu on 2016/12/21.
 */

public class Ex01Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnLongClickListener, View.OnClickListener {
    private List<Focus.Item> mItems;
    private Context mContext;
    private LayoutInflater mInflater;

    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;
    private RecyclerView mRecyclerView;

    private static final int POSITION_SINGLE = 1;//设置单双行标记
    private static final int POSITION_MULTIPLE = 2;

    private Random rm;

    public Ex01Adapter(Context context, List<Focus.Item> items) {
        mContext = context;
        mItems = items;
        mInflater = LayoutInflater.from(mContext);
        rm = new Random();
    }
    //初始化监听事件
    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public void setOnLongClickListener(OnItemLongClickListener listener) {
        mLongListener = listener;
    }

    /**
     * 根据类型创建不同的布局
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case POSITION_SINGLE:
                 view =  mInflater.inflate(R.layout.item_ex01, parent, false);
                holder = new ViewHolder(view);
                break;
            case POSITION_MULTIPLE:
                view =  mInflater.inflate(R.layout.item_ex02, parent, false);
                holder = new ViewHolder2(view);
                break;
        }
        if (view != null) {
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }
        return holder;
    }

    /**
     * 根据返回的ViewHolder类型加载不同数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder vh = (ViewHolder) holder;
            if (position >0) {
                vh.mTextView.setText(mItems.get(position).getTitle());
            }
            else {
                vh.mTextView.setText("");
            }
            vh.mImageView.setImageResource(R.mipmap.ic_launcher);
            vh.mImageView.setTag(mItems.get(position).getCoverPath());
            new ImgAsync(vh.mImageView).execute(mItems.get(position).getCoverPath());
        }else if (holder instanceof ViewHolder2){
            ViewHolder2 vh = (ViewHolder2) holder;
            if (position >0) {
                vh.mTextView.setText(mItems.get(position).getTitle());
            }
            else {
                vh.mTextView.setText("");
            }
            vh.mTextView.setTextColor(Color.rgb(rm.nextInt(256),rm.nextInt(256),rm.nextInt(256)));//第二种布局使用的颜色随机
            vh.mImageView.setImageResource(R.mipmap.ic_launcher);
            vh.mImageView.setTag(mItems.get(position).getCoverPath());
            new ImgAsync(vh.mImageView).execute(mItems.get(position).getCoverPath());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * 返回布局的类型标识
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position%2==0 ?POSITION_SINGLE:POSITION_MULTIPLE;
    }

    //挂载RecyclerView
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;

    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    @Override
    public boolean onLongClick(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        mLongListener.MyItemLongClick(mRecyclerView,v,position,mItems.get(position));
        return true;
    }

    @Override
    public void onClick(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        mListener.MyItemClick(mRecyclerView,v,position,mItems.get(position));
    }


    //接口
    public interface OnItemClickListener{
        void MyItemClick(RecyclerView recyclerView, View v, int position, Focus.Item data);
    }

    public interface OnItemLongClickListener {
        void MyItemLongClick(RecyclerView recyclerView, View v, int position, Focus.Item data);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.item_tv);
            mImageView = (ImageView)itemView.findViewById(R.id.item_iv);
        }
    }
    public static class ViewHolder2 extends RecyclerView.ViewHolder{
        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder2(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.item_tv);
            mImageView = (ImageView)itemView.findViewById(R.id.item_iv);
        }
    }
}
