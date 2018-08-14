package kanghb.com.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2018/8/13
 * 编写人：kanghb
 * 功能描述：
 */
public class AIDLService extends Service {
    public final String TAG = this.getClass().getSimpleName();
    //包含Book对象的list
    private List<Book> books = new ArrayList<>();
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
    };
    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("Android开发艺术探索");
        book.setPrice(28);
        books.add(book);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bookManager;
    }
}
