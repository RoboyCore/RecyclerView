package mobi.xiaowu.recyclerview.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


/**
 * Created by xiaowu on 2016/11/12.
 */

public class JsonAsyncTask extends AsyncTask<String,Integer,byte[]> {
    private Context mContext;
    private Callback mCallback;

    public JsonAsyncTask(Callback callback, Context context) {
        mCallback = callback;
        mContext = context;
    }



    @Override
    protected byte[] doInBackground(String... params) {

        return HttpUtils.OkHttpCon(params[0]);
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
        if (bytes != null) {
            mCallback.sendData(bytes);
        }else{
            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
        }
    }


    public interface Callback{
        void sendData(byte[] json);
    }
}
