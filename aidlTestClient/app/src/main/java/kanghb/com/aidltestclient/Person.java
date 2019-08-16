package kanghb.com.aidltestclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 创建时间：2018/8/14
 * 编写人：kanghb
 * 功能描述：
 */
public class Person implements Parcelable{
    private String name;

    protected Person(Parcel in) {
        name = in.readStringNoHelper();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringNoHelper(name);
    }
}
