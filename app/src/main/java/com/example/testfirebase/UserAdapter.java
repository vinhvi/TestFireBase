package com.example.testfirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHoler> {

    private List<User> mListUser;

    public UserAdapter(List<User> mListUser) {
        this.mListUser = mListUser;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHoler holder, int position) {
        User user = mListUser.get(position);
        if (user == null) {
            return;
        }
        holder.txtName.setText("Name:" + user.getName());
        holder.txtEmail.setText("Email: " + user.getEmail());
    }

    @Override
    public int getItemCount() {
        if (mListUser != null) {
            return mListUser.size();
        }
        return 0;
    }

    public class UserViewHoler extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtEmail;

        public UserViewHoler(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tv_Name);
            txtEmail = itemView.findViewById(R.id.tv_Email);
        }
    }
}
