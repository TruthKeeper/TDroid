package com.tk.tdroiddemo.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.tdroid.annotation.AutoInject;
import com.tdroid.annotation.Router;
import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.constants.RouterConstants;
import com.tk.tdroid.router.TRouter;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/24
 *     desc   : xxxx描述
 * </pre>
 */
@Router(path = RouterConstants.MINE_ACTIVITY)
public class MineActivity extends BaseActivity {
    @AutoInject(desc = "昵称")
    String nickName;
    @AutoInject(desc = "用户Id")
    long userId;
    @AutoInject(name = "sex", desc = "性别")
    boolean gender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity);
        TextView tv = findViewById(R.id.tv);
        StringBuilder builder = new StringBuilder();
        builder.append("昵称：");
        builder.append(nickName);
        builder.append("\n");
        builder.append("用户Id：");
        builder.append(userId);
        builder.append("\n");
        builder.append("性别：");
        builder.append(gender ? "男" : "女");
        tv.setText(builder.toString());

        findViewById(R.id.btn_open_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TRouter.with(RouterConstants.HOME_ACTIVITY)
                        .request(MineActivity.this);
            }
        });
    }

    @Override
    public boolean autoInjectData() {
        return true;
    }
}
