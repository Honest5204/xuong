package com.example.quanlykhohang.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlykhohang.R;
import com.example.quanlykhohang.dao.UserDAO;
import com.example.quanlykhohang.databinding.IteamListuserBinding;
import com.example.quanlykhohang.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> list;
    private UserDAO dao;
    public UserAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }
    public void setData(List<User> userList) {
        this.list = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var inflater = LayoutInflater.from(parent.getContext());
        var view = inflater.inflate(R.layout.iteam_listuser, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = list.get(position);
        holder.bind(user);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        IteamListuserBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = IteamListuserBinding.bind(itemView);
        }

        public void bind(User user) {
            binding.txtId.setText("ID: " + user.getId());
            binding.txtNameUser.setText("Name: " + user.getName());
            binding.txthomeTown.setText("Home town: " + user.getHomeTown());
            binding.txtposition.setText("Position: " + user.getPosition());
            binding.txtlastLogin.setText("Last login: " + user.getLastLogin());
            binding.txtlastAction.setText("Last action: " + user.getLastAction());
            String photoPath = user.getAvatar();
            if (photoPath != null) {
                Uri uri = Uri.parse(photoPath);
                binding.imgUser.setImageURI(uri);
            } else {
                binding.imgUser.setImageResource(R.drawable.sontung);
            }
        }
    }
}
