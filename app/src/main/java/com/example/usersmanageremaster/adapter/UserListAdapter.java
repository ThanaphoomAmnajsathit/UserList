package com.example.usersmanageremaster.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.usersmanageremaster.Models.User;
import com.example.usersmanageremaster.R;
import com.example.usersmanageremaster.domain.repositories.UserRepository;
import com.example.usersmanageremaster.domain.services.MainServices;
import com.example.usersmanageremaster.domain.usecase.GetUsersUseCase;
import com.example.usersmanageremaster.presentations.presenters.UserListFragmentPresenter;
import com.example.usersmanageremaster.presentations.views.fragments.UserListFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.Holder> implements Filterable {

    private UserListFragment userListFragment;
    private UserListFragmentPresenter presenter;
    private List<User> users;

    ItemClickListener clickListener;

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public interface ItemClickListener{
        void onItemClick(int position);
    }

    public void setItemClickListener(ItemClickListener listener){
        clickListener = listener;
    }


    public UserListAdapter(UserListFragment userListFragment
            , UserListFragmentPresenter presenter
            , List<User> users){
        this.userListFragment = userListFragment;
        this.presenter = presenter;
        this.users = users;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_user_list,parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (users.get(position).user_image != null){
            Glide.with(userListFragment).load(users.get(position).user_image).into(holder.iv_user);
            Log.e("IMG URL",users.get(position).user_image);
        }
        holder.tv_user_name.setText(users.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private UserRepository userRepository = new MainServices().getRetrofit().create(UserRepository.class);

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            if (charSequence.toString().isEmpty()){

            }else {
                Log.e("Test",String.valueOf(charSequence));
                GetUsersUseCase getUsersUseCase = new GetUsersUseCase(userRepository,presenter,String.valueOf(charSequence));
                getUsersUseCase.execute(null);
            }

            return null;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            users.clear();
            notifyDataSetChanged();
        }
    };

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView iv_user;
        TextView tv_user_name;
        CardView cv_users;

        public Holder(View itemView) {
            super(itemView);
            iv_user = itemView.findViewById(R.id.iv_user_image);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            cv_users = itemView.findViewById(R.id.cardView_users);
            cv_users.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            if (clickListener != null){
                clickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
