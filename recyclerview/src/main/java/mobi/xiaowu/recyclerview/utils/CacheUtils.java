package mobi.xiaowu.recyclerview.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import static android.content.ContentValues.TAG;

/**
 * Created by lx on 2016/12/9.
 * 缓存工具类
 */

public class CacheUtils {
    //添加缓存回收引用到引用队列
    private ReferenceQueue<LruCache<String, Bitmap>> rq;
    //用于内存缓存
    private SoftReference<LruCache<String, Bitmap>> sfCache;
    //用于文件缓存的目录
    private String dirFilePath;
    private  LruCache<String,Bitmap> lru;
    /**只使用内存缓存-LruCache*/
    public CacheUtils(int size)
    {
        this(size,null);
    }
    /**只使用文件缓存-推荐存储到扩展存储*/
    public CacheUtils(String path)
    {
        this(0,path);
    }
    /**同时使用内存缓存和文件缓存*/
    public CacheUtils(int size,String dirPath)
    {
        rq=new ReferenceQueue();
        //内存缓存
        if(size>0)
        {
            sfCache=new SoftReference<LruCache<String, Bitmap>>(
                    new LruCache<String, Bitmap>(size){
                        @Override
                        protected int sizeOf(String key, Bitmap value) {
                            return value.getByteCount();
                        }
                    },rq);
            lru=sfCache.get();
            Log.e("TS", "CacheUtils: "+lru);
        }
        //文件存储
        if(dirPath!=null)
        {
            //指定的目录不存在就创建
            File file=new File(dirPath);
            if(!file.exists())
            {
                file.mkdirs();
            }
            dirFilePath=file.getAbsolutePath();
        }
    }
    //获取缓存中的图片对象
    public Bitmap getBitMap(String imgUrl)
    {
        Bitmap bitmap=null;
        //验证是否有内存缓存
        if(sfCache!=null)
        {
            //从内存中获取数据
            lru.get(imgUrl);
            //验证是否有文件存储且内存存储没有改对象
            if(dirFilePath!=null && bitmap==null)
            {
                //从指定的文件路径中加载图片
                bitmap=loadFile(imgUrl);
                if(bitmap!=null)
                {
                    //添加到内存
                    put(imgUrl,bitmap,false);
                }
            }
        }
        else if(dirFilePath!=null){//只有文件缓存
            bitmap=loadFile(imgUrl);
        }

        return bitmap;
    }
    //加载指定文件缓存中的图片
    private Bitmap loadFile(String imgUrl)
    {
        //从指定的文件路径中加载图片
        return BitmapFactory.decodeFile(getFilePath(imgUrl));
    }
    //获取文件路径
    private String getFilePath(String imgUrl)
    {
        //获取资源的文件名称
        String file=imgUrl.substring(imgUrl.lastIndexOf("/")+1);
        return dirFilePath+File.separator+file;
    }
    //添加到缓存
    public void put(String imgUrl,Bitmap bitmap,boolean isSaveFile)
    {
        if(sfCache!=null)//内存存储
        {
            //LruCache<String,Bitmap> lru=sfCache.get();
            lru.put(imgUrl,bitmap);
            if(isSaveFile)//是否需要进行文件存储
            {
                try {
                    //保存图片，指明格式和质量
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,
                            new FileOutputStream(getFilePath(imgUrl)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }else if(dirFilePath!=null)//文件存储
        {
            try {
                //保存图片，指明格式和质量
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,
                        new FileOutputStream(getFilePath(imgUrl)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
