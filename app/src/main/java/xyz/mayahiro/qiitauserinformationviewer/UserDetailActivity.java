package xyz.mayahiro.qiitauserinformationviewer;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import xyz.mayahiro.qiitauserinformationviewer.databinding.ActivityUserDetailBinding;
import xyz.mayahiro.qiitauserinformationviewer.model.User;
import xyz.mayahiro.qiitauserinformationviewer.network.Network;
import xyz.mayahiro.qiitauserinformationviewer.util.GlideApp;

public class UserDetailActivity extends AppCompatActivity {
    private static final long PER_PAGE = 5;

    private static final String KEY_EXTRA_USER_ID = "key_extra_user_id";

    private ActivityUserDetailBinding binding;

    private ActionBar actionBar;

    private User user;
    private List<User> followers;
    private List<User> followees;

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
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        String userId = getIntent().getStringExtra(KEY_EXTRA_USER_ID);

        getUserData(userId);
    }

    private void getUserData(String userId) {
        Single
                .zip(
                        Network.instance().qiitaApiService().user(userId),
                        Network.instance().qiitaApiService().followers(userId, 1L, PER_PAGE),
                        Network.instance().qiitaApiService().followees(userId, 1L, PER_PAGE),
                        (user, followers, followees) -> {
                            UserDetailActivity.this.user = user;
                            UserDetailActivity.this.followers = followers;
                            UserDetailActivity.this.followees = followees;
                            return new Object();
                        }
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Object>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Object obj) {
                        bind();
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
    }

    private void bind() {
        // top card
        GlideApp.with(this).load(user.profileImageUrl).circleCrop().into(binding.profileImageView);

        if (TextUtils.isEmpty(user.name)) {
            binding.nameTextView.setText(user.id);
            if (actionBar != null) {
                actionBar.setTitle(user.id);
            }
        } else {
            binding.nameTextView.setText(user.name);
            if (actionBar != null) {
                actionBar.setTitle(user.name);
            }
        }

        if (TextUtils.isEmpty(user.organization)) {
            binding.organizationTextView.setVisibility(View.GONE);
        } else {
            binding.organizationTextView.setText(user.organization);
            binding.organizationTextView.setVisibility(View.VISIBLE);
        }

        binding.itemsCountTextView.setText(getString(R.string.items_count, user.itemsCount));
        binding.followersCountTextView.setText(getString(R.string.followers_count, user.followersCount));
        binding.followeesCountTextView.setText(getString(R.string.followees_count, user.followeesCount));

        if (TextUtils.isEmpty(user.facebookId)) {
            binding.facebookTextView.setVisibility(View.GONE);
        } else {
            binding.facebookTextView.setText("https://www.facebook.com/" + user.facebookId);
            binding.facebookTextView.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(user.githubLoginName)) {
            binding.githubTextView.setVisibility(View.GONE);
        } else {
            binding.githubTextView.setText("https://github.com/" + user.githubLoginName);
            binding.githubTextView.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(user.linkedinId)) {
            binding.linkedinTextView.setVisibility(View.GONE);
        } else {
            binding.linkedinTextView.setText("https://jp.linkedin.com/in/" + user.linkedinId);
            binding.linkedinTextView.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(user.twitterScreenName)) {
            binding.twitterTextView.setVisibility(View.GONE);
        } else {
            binding.twitterTextView.setText("https://twitter.com/" + user.twitterScreenName);
            binding.twitterTextView.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(user.websiteUrl)) {
            binding.websiteTextView.setVisibility(View.GONE);
        } else {
            binding.websiteTextView.setText(user.websiteUrl);
            binding.websiteTextView.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(user.description)) {
            binding.descriptionTextView.setVisibility(View.GONE);
        } else {
            binding.descriptionTextView.setText(user.description);
            binding.descriptionTextView.setVisibility(View.VISIBLE);
        }

        // followers
        if (followers.size() == 0) {
            binding.followersTitleLayout.setVisibility(View.GONE);
            binding.followersCardView.setVisibility(View.GONE);
        } else {
            binding.followerLayout1.setVisibility(View.GONE);
            binding.followerLayout2.setVisibility(View.GONE);
            binding.followerLayout3.setVisibility(View.GONE);

            if (followers.size() > 0) {
                GlideApp.with(this).load(followers.get(0).profileImageUrl).circleCrop().into(binding.followerImageView1);
                if (TextUtils.isEmpty(followers.get(0).name)) {
                    binding.followerNameTextView1.setText(followers.get(0).id);
                } else {
                    binding.followerNameTextView1.setText(followers.get(0).name);
                }
                binding.followerLayout1.setOnClickListener(v -> startActivity(UserDetailActivity.createIntent(this, followers.get(0).id)));
                binding.followerLayout1.setVisibility(View.VISIBLE);
            }

            if (followers.size() > 1) {
                GlideApp.with(this).load(followers.get(1).profileImageUrl).circleCrop().into(binding.followerImageView2);
                if (TextUtils.isEmpty(followers.get(1).name)) {
                    binding.followerNameTextView2.setText(followers.get(1).id);
                } else {
                    binding.followerNameTextView2.setText(followers.get(1).name);
                }
                binding.followerLayout2.setOnClickListener(v -> startActivity(UserDetailActivity.createIntent(this, followers.get(1).id)));
                binding.followerLayout2.setVisibility(View.VISIBLE);
            }

            if (followers.size() > 2) {
                GlideApp.with(this).load(followers.get(2).profileImageUrl).circleCrop().into(binding.followerImageView3);
                if (TextUtils.isEmpty(followers.get(2).name)) {
                    binding.followerNameTextView3.setText(followers.get(2).id);
                } else {
                    binding.followerNameTextView3.setText(followers.get(2).name);
                }
                binding.followerLayout3.setOnClickListener(v -> startActivity(UserDetailActivity.createIntent(this, followers.get(2).id)));
                binding.followerLayout3.setVisibility(View.VISIBLE);
            }

            binding.followersTitleLayout.setVisibility(View.VISIBLE);
            binding.followersCardView.setVisibility(View.VISIBLE);
        }

        // followees
        if (followees.size() == 0) {
            binding.followeesTitleLayout.setVisibility(View.GONE);
            binding.followeesCardView.setVisibility(View.GONE);
        } else {
            binding.followeeLayout1.setVisibility(View.GONE);
            binding.followeeLayout2.setVisibility(View.GONE);
            binding.followeeLayout3.setVisibility(View.GONE);

            if (followees.size() > 0) {
                GlideApp.with(this).load(followees.get(0).profileImageUrl).circleCrop().into(binding.followeeImageView1);
                if (TextUtils.isEmpty(followees.get(0).name)) {
                    binding.followeeNameTextView1.setText(followees.get(0).id);
                } else {
                    binding.followeeNameTextView1.setText(followees.get(0).name);
                }
                binding.followeeLayout1.setOnClickListener(v -> startActivity(UserDetailActivity.createIntent(this, followees.get(0).id)));
                binding.followeeLayout1.setVisibility(View.VISIBLE);
            }

            if (followees.size() > 1) {
                GlideApp.with(this).load(followees.get(1).profileImageUrl).circleCrop().into(binding.followeeImageView2);
                if (TextUtils.isEmpty(followees.get(1).name)) {
                    binding.followeeNameTextView2.setText(followees.get(1).id);
                } else {
                    binding.followeeNameTextView2.setText(followees.get(1).name);
                }
                binding.followeeLayout2.setOnClickListener(v -> startActivity(UserDetailActivity.createIntent(this, followees.get(1).id)));
                binding.followeeLayout2.setVisibility(View.VISIBLE);
            }

            if (followees.size() > 2) {
                GlideApp.with(this).load(followees.get(2).profileImageUrl).circleCrop().into(binding.followeeImageView3);
                if (TextUtils.isEmpty(followees.get(2).name)) {
                    binding.followeeNameTextView3.setText(followees.get(2).id);
                } else {
                    binding.followeeNameTextView3.setText(followees.get(2).name);
                }
                binding.followeeLayout3.setOnClickListener(v -> startActivity(UserDetailActivity.createIntent(this, followees.get(2).id)));
                binding.followeeLayout3.setVisibility(View.VISIBLE);
            }

            binding.followeesTitleLayout.setVisibility(View.VISIBLE);
            binding.followeesCardView.setVisibility(View.VISIBLE);
        }
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
