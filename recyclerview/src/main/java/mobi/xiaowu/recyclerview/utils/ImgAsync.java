package mobi.xiaowu.recyclerview.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xiaowu on 2016/11/8.
 */

public class ImgAsync extends AsyncTask<String, Integer, Bitmap> {
    private ImageView iv;
    private String url;
    private CacheUtils mCacheUtils;
    // private ProgressBar progressBar;

    public ImgAsync(ImageView imageView) {
        this.iv = imageView;


    }

    public String getUrl() {
        return url;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        url = strings[0];
        if (url.length() > 0) {
            int size = (int) Runtime.getRuntime().freeMemory() / 8;
            System.out.println((int) Runtime.getRuntime().totalMemory());
            System.out.println((int) Runtime.getRuntime().freeMemory());
            try {
                String path = Environment.getExternalStorageDirectory().getCanonicalPath();
                mCacheUtils = new CacheUtils(size, path + "/sd/text");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitMap = mCacheUtils.getBitMap(url.substring(url.lastIndexOf("/")));
            if (bitMap != null) {
                System.out.println("bitMap = ");
                return bitMap;
            }
            byte[] bytes = HttpUtils.UrlConnection(strings[0]);
            if (bytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mCacheUtils.put(url, bitmap, true);
                return bitmap;
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmaps) {
        super.onPostExecute(bitmaps);
        if (bitmaps != null && url.equals(iv.getTag())) {
            iv.setImageBitmap(bitmaps);
        } else {
            Log.d("TAG", "图片加载失败");
        }
    }


}
