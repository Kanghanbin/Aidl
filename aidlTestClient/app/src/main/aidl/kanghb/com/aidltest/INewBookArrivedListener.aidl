// INewBookArrivedListener.aidl
package kanghb.com.aidltest;
import kanghb.com.aidltest.Book;

// Declare any non-default types here with import statements

interface INewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
