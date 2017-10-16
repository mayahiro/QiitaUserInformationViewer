package xyz.mayahiro.qiitauserinformationviewer;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import xyz.mayahiro.qiitauserinformationviewer.databinding.ActivityUserDetailBinding;
import xyz.mayahiro.qiitauserinformationviewer.model.User;
import xyz.mayahiro.qiitauserinformationviewer.network.Network;

public class UserDetailActivity extends AppCompatActivity {
    private static String KEY_EXTRA_USER_ID = "key_extra_user_id";

    private ActivityUserDetailBinding binding;

    public static Intent createIntent(@NonNull Context context, String userId) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(KEY_EXTRA_USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        String userId = getIntent().getStringExtra(KEY_EXTRA_USER_ID);

        getUserData(userId);
    }

    private void getUserData(String userId) {
        Network.instance().qiitaApiService().user(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull User user) {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return false;
    }
}
