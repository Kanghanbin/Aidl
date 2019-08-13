# `Aidl`

### 学习目的

实现进程间通信，尤其是在涉及多进程并发情况下的进程间通信。

### 项目概括

学习`Aidl`时，写的demo。项目包含客户端`aidlTestClient`和服务端`aidlTestServer`，通过学习`aidl`实现`aidlTestClient`和`aidlTestServer`之间的通讯，也就是跨进程通讯。客户端和服务端分别是两个安卓app

### 项目实战

- 创建 AIDL

1.首先，新建一个实体类Book，实现 `Parcelable` 接口，以便序列化/反序列化

```java
package kanghb.com.aidltest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 创建时间：2018/8/10
 * 编写人：kanghb
 * 功能描述：
 */
public class Book implements Parcelable {
    private int price;
    private String name;

    public Book() {
    }

    protected Book(Parcel in) {
        price = in.readInt();
        name = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(price);
        dest.writeString(name);
    }
    /**
     * 参数是一个Parcel,用它来存储与传输数据
     * @param dest
     */
    public void readFromParcel(Parcel dest) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        price = dest.readInt();
        name = dest.readString();

    }

    @Override
    public String toString() {
        return "Book{" +
                "price=" + price +
                ", name='" + name + '\'' +
                '}';
    }
}
```

2.在src/main目录下新建aidl文件(包含实体类的映射aidl文件和接口aidl文件)，Androidstudio创建很简单，自动生成aidl文件夹。

实体类映射文件

```java
// Book.aidl
//第一类AIDL文件的例子
//这个文件的作用是引入了一个序列化对象 Book 供其他的AIDL文件使用
//注意：Book.aidl与Book.java的包名应当是一样的
package kanghb.com.aidltest;
//注意parcelable是小写
parcelable Book;
```

接口 aidl 文件

```java
// AidlBookManager.aidl
package kanghb.com.aidltest;

// Declare any non-default types here with import statements
import kanghb.com.aidltest.Book;

interface AidlBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
  //所有的返回值前都不需要加任何东西，不管是什么数据类型
    List<Book> getBooks();

    //传参时除了Java基本类型以及String，CharSequence之外的类型
    //其他类型的参数都需要标上方向类型：in(输入), out(输出), inout(输入输出)，具体加什么量需而定
    void addBook(in Book book);


}
```

在接口 aidl 文件中定义将来要在跨进程进行的操作，上面的接口中定义了两个方法：

- addBook: 添加 Book
- getBooks：获取 Book列表

**需要注意的是**：非默认支持数据类型需要导入包，传参是，除了基本数据类型外，其他都要表明方向类型tag

3.build project，生成java文件

点击 `Build` -> `Make Project`，然后等待构建完成。

然后就会在 `build/generated/source/aidl/你的 flavor/` 下生成一个 Java 文件。代码自动生成，aidl强大之处

[![img](https://raw.githubusercontent.com/Kanghanbin/Aidl/master/img/1.png)](https://raw.githubusercontent.com/Kanghanbin/Aidl/master/img/1.png)

```java
/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\aidlTestClient\\app\\src\\main\\aidl\\kanghb\\com\\aidltest\\AidlBookManager.aidl
 */
package kanghb.com.aidltest;

public interface AidlBookManager extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements kanghb.com.aidltest.AidlBookManager {
        private static final java.lang.String DESCRIPTOR = "kanghb.com.aidltest.AidlBookManager";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an kanghb.com.aidltest.AidlBookManager interface,
         * generating a proxy if needed.
         */
        public static kanghb.com.aidltest.AidlBookManager asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof kanghb.com.aidltest.AidlBookManager))) {
                return ((kanghb.com.aidltest.AidlBookManager) iin);
            }
            return new kanghb.com.aidltest.AidlBookManager.Stub.Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_getBooks: {
                    data.enforceInterface(DESCRIPTOR);
                    java.util.List<kanghb.com.aidltest.Book> _result = this.getBooks();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case TRANSACTION_addBook: {
                    data.enforceInterface(DESCRIPTOR);
                    kanghb.com.aidltest.Book _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = kanghb.com.aidltest.Book.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.addBook(_arg0);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements kanghb.com.aidltest.AidlBookManager {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            /**
             * Demonstrates some basic types that you can use as parameters
             * and return values in AIDL.
             *///所有的返回值前都不需要加任何东西，不管是什么数据类型
            @Override
            public java.util.List<kanghb.com.aidltest.Book> getBooks() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.util.List<kanghb.com.aidltest.Book> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_getBooks, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createTypedArrayList(kanghb.com.aidltest.Book.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
//传参时除了Java基本类型以及String，CharSequence之外的类型
//都需要在前面加上定向tag，具体加什么量需而定

            @Override
            public void addBook(kanghb.com.aidltest.Book book) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((book != null)) {
                        _data.writeInt(1);
                        book.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_getBooks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    }

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     *///所有的返回值前都不需要加任何东西，不管是什么数据类型
    public java.util.List<kanghb.com.aidltest.Book> getBooks() throws android.os.RemoteException;
//传参时除了Java基本类型以及String，CharSequence之外的类型
//都需要在前面加上定向tag，具体加什么量需而定

    public void addBook(kanghb.com.aidltest.Book book) throws android.os.RemoteException;
}
```

- 2.编写服务端`aidlTestServer`

创建 Service，在其中创建上面生成的 Binder 对象实例，实现接口定义的方法；然后在 onBind() 中返回

创建将来要运行在另一个进程的 Service，在其中实现了 AIDL 接口中定义的方法:

```java
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
```

上面的代码中，创建的对象是一个 `AidlBookManager.Stub` ，它是一个 Binder 。

**在 Manifest 文件中声明：**

```java
   <service android:name=".AIDLService">
            <intent-filter>
                <action android:name = "com.khb.aidl.server"/>
                <category android:name="android.intent.category.DEFAULT"/>
            </intent-filter>
   </service>
```

**这里需要注意**：因为我的服务端和客户端分别是两个app所以是两个进程，如果在一个项目里跨进程service可以这样声明：

```java
<service
    android:name="AIDLService"
    android:enabled="true"
    android:exported="true"
    android:process=":aidl"/>
```

- 3.编写客户端`aidlTestClient`

在`MainActivity`中放入一个send按钮，点击实现跨进程通信

```java
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
    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(serviceConnection);
            mBound = false;
        }
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(getLocalClassName(), "service connected");
            aidlBookManager = AidlBookManager.Stub.asInterface(service);
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
```

运行两个app至手机上，查看log日志，分析夸进程通讯

客户端点击send

[![img](https://raw.githubusercontent.com/Kanghanbin/Aidl/master/img/2.png)](https://raw.githubusercontent.com/Kanghanbin/Aidl/master/img/2.png)

服务端接受：

[![img](https://raw.githubusercontent.com/Kanghanbin/Aidl/master/img/3.png)](https://raw.githubusercontent.com/Kanghanbin/Aidl/master/img/3.png)

### 采坑记录

1.默认生成的模板类的对象只支持为 in 的定向 tag

因为默认生成的类里面只有 *writeToParcel()* 方法，而如果要支持为 out 或者 inout 的定向 tag 的话，还需要实现 *readFromParcel()* 方法

```java
/**
 * 参数是一个Parcel,用它来存储与传输数据
 * @param dest
 */
public void readFromParcel(Parcel dest) {
    //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
    name = dest.readString();
    price = dest.readInt();
}
```

2.Book.aidl必须和Book.java的名字，刚开始我申明为`AidlBook.aidl`,发现会build'出错

3.Book.aidl和Book.java 包名必须一致，为了方便移植aidl我把Book.java也移到了aidl目录中，这样移植只需要copy`aidl`即可以，但是这样会有个问题。Androidstudio找不到Book，因为。Gradle 默认是将 java 代码的访问路径设置在 java 包下的 可以通过下面方法解决：

- 修改 build.gradle 文件：在 android{} 中间加上下面的内容：

```java
sourceSets {
    main {
        java.srcDirs = ['src/main/java', 'src/main/aidl']
    }
}
```
