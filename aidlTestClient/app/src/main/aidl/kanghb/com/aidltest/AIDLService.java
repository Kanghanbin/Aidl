package kanghb.com.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 创建时间：2018/8/13
 * 编写人：kanghb
 * 功能描述：
 */
public class AIDLService extends Service {
    public final String TAG = this.getClass().getSimpleName();
    //包含Book对象的list
    private List<Book> books = new ArrayList<>();
    private AtomicBoolean isServiceDestroyed = new AtomicBoolean(false);
    private CopyOnWriteArrayList<INewBookArrivedListener> iNewBookArrivedListeners = new CopyOnWriteArrayList<>();
    //由AIDL文件生成的AidlbookManager，文件目录在app\build\generated\source\aidl\debug\kanghb\com\aidltest\AidlBookManager.java
    private final AidlBookManager.Stub bookManager = new AidlBookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this){
                Log.e(TAG, "invoking getBooks() method , now the list is : " + books.toString());
                if (books != null) {
                    return books;
                }
                return new ArrayList<>();
            }

        }

        @Override
        public void addBook(Book book) throws RemoteException {

            synchronized (this){
                if(books == null){
                    books = new ArrayList<>();
                }
                if(book == null){
                    Log.e(TAG, "addBook: Book is null in In" );
                }
                //尝试修改客户端传过来book参数，观察到客户端的反馈
                book.setPrice(56);
                if(!books.contains(book)){
                    books.add(book);
                }
                //打印mBooks列表，观察客户端传过来的值
                Log.e(TAG, "invoking addBooks() method , now the list is : " + books.toString());
            }
        }

        @Override
        public void registerListener(INewBookArrivedListener listener) throws RemoteException {
            if(!iNewBookArrivedListeners.contains(listener)){
                iNewBookArrivedListeners.add(listener);
            }else {
                Log.e(TAG,"already exists.");
            }
            Log.e(TAG,"registerListener,size:" + iNewBookArrivedListeners.size());
        }

        @Override
        public void unRegisterListener(INewBookArrivedListener listener) throws RemoteException {
            if(iNewBookArrivedListeners.contains(listener)){
                iNewBookArrivedListeners.remove(listener);
                Log.e(TAG,"unregister listener succeed");
            }else {
                Log.e(TAG,"not found,unregister faild");
            }
            Log.e(TAG,"unregisterListener,size:" + iNewBookArrivedListeners.size());
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("Android开发艺术探索");
        book.setPrice(28);
        books.add(book);
        //没隔5秒新加一本书
        new Thread(new ServiceRunnable()).start();
    }

    private class ServiceRunnable implements Runnable{

        @Override
        public void run() {
            while (!isServiceDestroyed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Book book = new Book();
                book.setName("服务端加新书啦" + books.size());
                book.setPrice(books.size());
                books.add(book);
                for (int i = 0; i < iNewBookArrivedListeners.size(); i++) {
                    INewBookArrivedListener iNewBookArrivedListener = iNewBookArrivedListeners.get(i);
                    try {
                        iNewBookArrivedListener.onNewBookArrived(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bookManager;
    }
}
