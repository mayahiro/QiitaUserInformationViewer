package xyz.mayahiro.qiitauserinformationviewer.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import xyz.mayahiro.qiitauserinformationviewer.R;
import xyz.mayahiro.qiitauserinformationviewer.UserDetailActivity;
import xyz.mayahiro.qiitauserinformationviewer.databinding.RecyclerItemUserBinding;
import xyz.mayahiro.qiitauserinformationviewer.model.User;

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
        notifyDataSetChanged();
    }

    // view holder
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private RecyclerItemUserBinding binding;

        public UserViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(User user) {
            binding.idTextView.setText(user.id);

            Glide.with(itemView.getContext()).load(user.profileImageUrl).into(binding.profileImageView);

            binding.cardView.setOnClickListener(v -> itemView.getContext().startActivity(UserDetailActivity.createIntent(itemView.getContext(), user.id)));
        }
    }
}
