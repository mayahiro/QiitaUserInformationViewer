package xyz.mayahiro.qiitauserinformationviewer.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xyz.mayahiro.qiitauserinformationviewer.R;
import xyz.mayahiro.qiitauserinformationviewer.UserDetailActivity;
import xyz.mayahiro.qiitauserinformationviewer.databinding.RecyclerItemUserBinding;
import xyz.mayahiro.qiitauserinformationviewer.model.User;
import xyz.mayahiro.qiitauserinformationviewer.util.GlideApp;

/**
 * Created by mayahiro on 10/16/17.
 */

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<User> users;

    public UsersAdapter(@NonNull Context context) {
        layoutInflater = LayoutInflater.from(context);
        users = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User user = users.get(position);
        UserViewHolder userViewHolder = (UserViewHolder) holder;
        userViewHolder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void addAllUsers(List<User> users) {
        this.users.addAll(users);
        notifyItemRangeInserted(this.users.size() - users.size(), users.size());
    }

    // view holder
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private RecyclerItemUserBinding binding;

        public UserViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(User user) {
            GlideApp.with(itemView.getContext()).load(user.profileImageUrl).circleCrop().into(binding.profileImageView);

            if (TextUtils.isEmpty(user.name)) {
                binding.nameTextView.setText(user.id);
            } else {
                binding.nameTextView.setText(user.name);
            }

            if (TextUtils.isEmpty(user.organization)) {
                binding.organizationTextView.setVisibility(View.GONE);
            } else {
                binding.organizationTextView.setText(user.organization);
                binding.organizationTextView.setVisibility(View.VISIBLE);
            }

            binding.itemsCountTextView.setText(itemView.getContext().getString(R.string.items_count, user.itemsCount));
            binding.followersCountTextView.setText(itemView.getContext().getString(R.string.followers_count, user.followersCount));
            binding.followeesCountTextView.setText(itemView.getContext().getString(R.string.followees_count, user.followeesCount));

            if (TextUtils.isEmpty(user.description)) {
                binding.descriptionTextView.setVisibility(View.GONE);
            } else {
                binding.descriptionTextView.setText(user.description);
                binding.descriptionTextView.setVisibility(View.VISIBLE);
            }

            binding.cardView.setOnClickListener(v -> itemView.getContext().startActivity(UserDetailActivity.createIntent(itemView.getContext(), user.id)));
        }
    }
}
