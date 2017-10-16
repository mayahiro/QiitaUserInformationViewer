package xyz.mayahiro.qiitauserinformationviewer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import xyz.mayahiro.qiitauserinformationviewer.adapter.UsersAdapter;
import xyz.mayahiro.qiitauserinformationviewer.databinding.ActivityMainBinding;
import xyz.mayahiro.qiitauserinformationviewer.model.User;
import xyz.mayahiro.qiitauserinformationviewer.network.Network;

public class MainActivity extends AppCompatActivity {
    private static final long PER_PAGE = 20;

    private ActivityMainBinding binding;

    private long page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }

        binding.recyclerView.setAdapter(new UsersAdapter(this));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                UsersAdapter adapter = (UsersAdapter) recyclerView.getAdapter();
                if (adapter.getItemCount() - 5 == linearLayoutManager.findLastVisibleItemPosition()) {
                    getUsers();
                }
            }
        });

        getUsers();
    }

    private void getUsers() {
        page++;

        Network.instance().qiitaApiService().users(page, PER_PAGE)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<User>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull List<User> users) {
                        ((UsersAdapter) binding.recyclerView.getAdapter()).addAllUsers(users);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
    }
}
