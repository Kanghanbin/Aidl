package kanghb.com.aidltestclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import kanghb.com.aidltest.AidlBookManager;
import kanghb.com.aidltest.Book;

public class MainActivity extends AppCompatActivity {


    //由AIDL文件生成的Java类
    private AidlBookManager aidlBookManager = null;
    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;
    private List<Book> booklist;
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.e(getLocalClassName(), "服务死亡了，要开始重连了");
            if(aidlBookManager == null)
                return;
            aidlBookManager.asBinder().unlinkToDeath(mDeathRecipient,0);
            aidlBookManager = null;

            //重新绑定远程services
            attemptToBindService();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void send(View view){
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (aidlBookManager == null) {
            return;
        }

        Book book = new Book();
        book.setName("APP研发录In");
        book.setPrice(30);
        try {
            aidlBookManager.addBook(book);
            Log.e(getLocalClassName(), book.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    /**
     * 尝试与服务端建立连接
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.khb.aidl.server");
        intent.setPackage("kanghb.com.aidltest");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (!mBound) {
//            attemptToBindService();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mBound) {
//            unbindService(serviceConnection);
//            mBound = false;
//        }
//    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(getLocalClassName(), "service connected");
            aidlBookManager = AidlBookManager.Stub.asInterface(service);
            try {
                aidlBookManager.asBinder().linkToDeath(mDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBound = true;
            if(aidlBookManager != null){
                try {
                    booklist = aidlBookManager.getBooks();
                    Log.e(getLocalClassName(), booklist.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(getLocalClassName(), "service disconnected");
            mBound = false;
        }
    };
}
