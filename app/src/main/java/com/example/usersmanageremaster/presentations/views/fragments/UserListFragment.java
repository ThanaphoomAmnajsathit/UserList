package com.example.usersmanageremaster.presentations.views.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.usersmanageremaster.Models.User;
import com.example.usersmanageremaster.R;
import com.example.usersmanageremaster.adapter.UserListAdapter;
import com.example.usersmanageremaster.interfaces.UserListFragmentInterface;
import com.example.usersmanageremaster.presentations.presenters.UserListFragmentPresenter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lamudi.phonefield.PhoneEditText;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class UserListFragment extends Fragment implements UserListFragmentInterface.view {

    UserListFragmentInterface.view viewInterface = this;

    //view
    private View view;
    private FloatingActionButton fab;

    //Recyclerview
    private RecyclerView userList_recyclerView;
    private UserListAdapter userList_Adapter;
    private ProgressBar progressBar_loadMore;

    //get user list -offset
    public static int offset = 0;

    //GetData from presenter<-useCase<-API
    private UserListFragmentPresenter presenter;
    private List<User> users;

    //---pass data to update user fragment---
    private static String user_id;
    private static String image_user;
    private static String firstName;
    private static String lastName;
    private static String email;
    private static String phone;
    private static String gender;

    private String[] saf = {"asdasf","Sfasf"};

    public UserListFragment() {
        // Required empty public constructor
    }

    public static UserListFragment newInstance() {
        Bundle args = new Bundle();
        args.putString("user_id",user_id);
        args.putString("image_user",image_user);
        args.putString("firstName",firstName);
        args.putString("lastName",lastName);
        args.putString("email",email);
        args.putString("phone",phone);
        args.putString("gender",gender);
        UserListFragment fragment = new UserListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_list, container, false);
        init(view);
        onClickFloatButton(this);
        onSetRecyclerView();
        recyclerviewScrollListenerLoadmore();
        return view;
    }

    private void init(View view){
        fab = view.findViewById(R.id.fab);
        userList_recyclerView = view.findViewById(R.id.userList_recyclerview);
        progressBar_loadMore = view.findViewById(R.id.progress_loadmore);
        presenter = new UserListFragmentPresenter(this,10,0);
        users = new ArrayList<>();
    }

    //--- Insert User ---
    private void onClickFloatButton(Fragment fragment){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_userListFragment_to_insertUserFragment);
            }
        });
    }

    //-----------------set user data into recyclerview-------------
    @Override
    public void onSetUserList(List<User> users) {
        //this.users = users;
        Log.e("USERS LIST",users.toString());
        if (users.size() > 0){
            for (int i = 0; i < users.size() ; i++) {
                this.users.add(users.get(i));
            }
            offset += 10;
            onSetAdapter(this.users);
        }else {
            Toast.makeText(getContext(),"LoadMore is no have users.",Toast.LENGTH_SHORT).show();
        }
    }

    private void onSetAdapter(List<User> users){
        userList_Adapter.setUsers(users);
        userList_Adapter.notifyDataSetChanged();
    }

    private void onSetRecyclerView(){
        userList_Adapter = new UserListAdapter(this,presenter,users);
        userList_recyclerView.setAdapter(userList_Adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getContext()
                ,LinearLayoutManager.VERTICAL
                ,false
        );
        userList_recyclerView.setLayoutManager(layoutManager);
        onRecyclerClick();
    }

    private void onRecyclerClick(){
        userList_Adapter.setItemClickListener(new UserListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {

                onCreateUserDetailsSheetBottom(position);

            }
        });
    }

    //---------Load more----------
    private void recyclerviewScrollListenerLoadmore(){
        userList_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    userList_recyclerView.scrollToPosition(users.size()-1);
                    progressBar_loadMore.setVisibility(View.VISIBLE);

                    presenter = new UserListFragmentPresenter(viewInterface,10,offset);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @SuppressLint("ShowToast")
                        @Override
                        public void run() {
                            progressBar_loadMore.setVisibility(View.GONE);
                        }
                    }, 1000);
                }
            }
        });
    }
    //---------------------------------------------------------------
    //------------------User Details Sheet Bottom--------------------
    private void onCreateUserDetailsSheetBottom(int position){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                getContext(),R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getContext())
                .inflate(
                        R.layout.layout_bottom_sheet
                        ,(LinearLayout)view.findViewById(R.id.user_bottom_sheet)
                );
        onSetUserDetailsBottomSheet(bottomSheetView , bottomSheetDialog ,position);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    //-----------Bottom sheet----------
    @SuppressLint("SetTextI18n")
    private void onSetUserDetailsBottomSheet(
            View btm_view
            , BottomSheetDialog btm_sheet
            , int position){
        ImageView user_profile = btm_view.findViewById(R.id.imageView_userProfile_btm);
        TextView user_name = btm_view.findViewById(R.id.textView_name_btm);
        TextView user_phone = btm_view.findViewById(R.id.textView_phone_btm);
        TextView user_email = btm_view.findViewById(R.id.textView_email_btm);
        TextView user_gender = btm_view.findViewById(R.id.textView_gender_btm);
        ImageView iv_gender_btm = btm_view.findViewById(R.id.imageView_userGender_btm);
        Button btn_edit = btm_view.findViewById(R.id.button_edit);
        ImageView iv_trash = btm_view.findViewById(R.id.imageView_trash);

        Glide.with(this)
                .load(users.get(position).user_image) // http://10.0.2.2/users_api/img/
                .into(user_profile);

        user_name.setText(users
                .get(position)
                .getUser_first_name()+" "+ users
                .get(position)
                .getUser_last_name()
        );

        user_phone.setText(
                users.get(position).user_phone
        );

        user_email.setText(
                users.get(position).user_email
        );

        presenter.genderBottomSheetCondition(
                Integer.parseInt(users.get(position).getUser_gender())
                ,iv_gender_btm
                ,user_gender
        );

        //---Button Edit User---
        btn_edit.setOnClickListener(v -> {
            user_id = String.valueOf(users.get(position).user_id);
            image_user = users.get(position).user_image;
            firstName = users.get(position).user_first_name;
            lastName = users.get(position).user_last_name;
            email = users.get(position).user_email;
            phone = users.get(position).user_phone;
            gender = users.get(position).user_gender;
            btm_sheet.dismiss();
            NavHostFragment.findNavController(this).navigate(R.id.action_userListFragment_to_updateUserFragment);
        });

        //---Button Delete User---
        iv_trash.setOnClickListener(v -> {
            alertView(position,this,btm_sheet);
            Log.e("=====",users.get(position).user_image.substring(
                    users.get(position).user_image.lastIndexOf("/")+1));
        });
    }
    //---------Confirm to delete user------------
    private void alertView(int position
            , UserListFragmentInterface.view view
            , BottomSheetDialog btm_sheet) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle( "Delete Dialog" )
                .setIcon(R.drawable.delete)
                .setMessage(
                        "Delete " + users
                                .get(position)
                                .getUser_first_name() + " " +
                                users
                                .get(position)
                                .getUser_last_name() + " ...?")

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                         dialoginterface.cancel();
                    }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {

                        //-------Delete User------
                        presenter = new UserListFragmentPresenter(
                                 view
                                ,String.valueOf(users.get(position).getUser_id())
                                ,users.get(position).user_image.substring(
                                        users.get(position).user_image.lastIndexOf("/")+1
                                )+"123"
                        );

                        users.remove(position);
                        userList_Adapter.notifyDataSetChanged();
                        btm_sheet.dismiss();
                    }
                }).show();
    }

    //----------------Search View------------------
    @Override
    public void onCreateOptionsMenu(Menu menu, @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userList_Adapter.getFilter().filter(newText);
                return false;
            }
        });
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                users.clear();
                presenter = new UserListFragmentPresenter(viewInterface,10,0);
                offset = 0;
                return true;
            }
        });
    }

    @Override
    public void onSetSheetGenderMan(ImageView iv_gender_btm , TextView user_gender){
        iv_gender_btm.setImageResource(R.drawable.man);
        user_gender.setText(R.string.gender_man);
    }

    @Override
    public void onSetSheetGenderFemale(ImageView iv_gender_btm , TextView user_gender){
        iv_gender_btm.setImageResource(R.drawable.female);
        user_gender.setText(R.string.gender_female);
    }

}