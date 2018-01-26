package com.tk.tdroiddemo.sample;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tdroid.annotation.AutoInject;
import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroiddemo.R;

/**
 * <pre>
 *      author : TK
 *      time : 2018/1/26
 *      desc :
 * </pre>
 */

public class SampleAutoInjectActivity extends BaseActivity {
    @AutoInject(desc = "昵称")
    String nickName;
    @AutoInject(desc = "用户Id")
    long userId;
    @AutoInject(name = "sex", desc = "性别")
    boolean gender;
    @AutoInject(name = "parent", parcelable = true, desc = "父母")
    Extra extra;

    private TextView tv;
    private FrameLayout container;
    private SampleAutoInjectFragment fragment;

    @Override
    public boolean autoInjectData() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_auto_inject);
        tv = findViewById(R.id.tv);
        findViewById(R.id.btn_add_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment();
            }
        });
        container = findViewById(R.id.container);

        StringBuilder builder = new StringBuilder();
        builder.append("昵称：");
        builder.append(nickName);
        builder.append("\n");
        builder.append("用户Id：");
        builder.append(userId);
        builder.append("\n");
        builder.append("性别：");
        builder.append(gender ? "男" : "女");
        builder.append("\n");
        builder.append(extra.toString());

        tv.setText(builder.toString());
    }

    private void addFragment() {
        if (fragment == null) {
            fragment = new SampleAutoInjectFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArray("tags", new String[]{"Java", "Android", "Web"});
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    public static class Extra implements Parcelable {
        private String father;
        private String mother;

        public Extra(String father, String mother) {
            this.father = father;
            this.mother = mother;
        }

        @Override
        public String toString() {
            return "Extra{" +
                    "father='" + father + '\'' +
                    ", mother='" + mother + '\'' +
                    '}';
        }

        public String getFather() {
            return father;
        }

        public void setFather(String father) {
            this.father = father;
        }

        public String getMother() {
            return mother;
        }

        public void setMother(String mother) {
            this.mother = mother;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.father);
            dest.writeString(this.mother);
        }

        public Extra() {
        }

        protected Extra(Parcel in) {
            this.father = in.readString();
            this.mother = in.readString();
        }

        public static final Parcelable.Creator<Extra> CREATOR = new Parcelable.Creator<Extra>() {
            @Override
            public Extra createFromParcel(Parcel source) {
                return new Extra(source);
            }

            @Override
            public Extra[] newArray(int size) {
                return new Extra[size];
            }
        };
    }
}
