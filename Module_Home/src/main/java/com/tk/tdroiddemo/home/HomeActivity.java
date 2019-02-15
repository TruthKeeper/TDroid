package com.tk.tdroiddemo.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tdroid.annotation.Router;
import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.constants.RouterConstants;
import com.tk.tdroid.router.TRouter;
import com.tk.tdroid.utils.FragmentHelper;

/**
 * <pre>
 *     author : TK
 *     time   : 2018/01/29
 *     desc   : xxxx描述
 * </pre>
 */
@Router(path = RouterConstants.HOME_ACTIVITY)
public class HomeActivity extends BaseActivity {

    private FragmentHelper fragmentHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Fragment mineInnerFragment = TRouter.instanceFragmentV4(RouterConstants.MINE_FRAGMENT);
        if (mineInnerFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("location", "杭州");
            bundle.putLong("userId", 1234);
            mineInnerFragment.setArguments(bundle);
            fragmentHelper = FragmentHelper.create(getSupportFragmentManager(), R.id.container,
                    savedInstanceState,
                    FragmentHelper.FragmentData.create("home", new FragmentHelper.FragmentFactory() {
                        @Override
                        public Fragment create(String sign) {
                            return mineInnerFragment;
                        }
                    }));

            fragmentHelper.switchFragment("home");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentHelper.onSaveInstanceState(outState);
    }

    public void openByUrl(View view) {
        TRouter.with(RouterConstants.MINE_ACTIVITY_NET + "?nickName=李四")
                .request(this);
        finish();
    }
}
