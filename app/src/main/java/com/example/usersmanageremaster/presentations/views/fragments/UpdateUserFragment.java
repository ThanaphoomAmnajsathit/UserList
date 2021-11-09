package com.example.usersmanageremaster.presentations.views.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.usersmanageremaster.FileUtils;
import com.example.usersmanageremaster.R;
import com.example.usersmanageremaster.RealPathUtil;
import com.example.usersmanageremaster.interfaces.UpdateUserFragmentInterface;
import com.example.usersmanageremaster.interfaces.UserListFragmentInterface;
import com.example.usersmanageremaster.presentations.presenters.UpdateUserFragmentPresenter;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.lamudi.phonefield.PhoneEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class UpdateUserFragment extends Fragment implements UpdateUserFragmentInterface.view {

    private final UpdateUserFragment fragment = this;
    private UpdateUserFragmentPresenter presenter;

    //---View---
    private View view;
    private CardView cv_user;
    private ImageView iv_user;
    private EditText et_firstName;
    private EditText et_lastName;
    private EditText et_email;
    private PhoneEditText et_phone;
    private Spinner spinner_gender;
    private Button btn_save;

    //---User Image path and fileName---
    private String imgPath;
    private String imgName;
    private Uri uri;

    private String user_id;
    private String image_user;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;

    //---drop down menu---
    ArrayAdapter<CharSequence> genderAdapter;

    public UpdateUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserListFragment listFragment = UserListFragment.newInstance();
        Bundle bundle = listFragment.getArguments();
        if (bundle != null){
            setUser_id(bundle.getString("user_id"));
            setImage_user(bundle.getString("image_user"));
            setFirstName(bundle.getString("firstName"));
            setLastName(bundle.getString("lastName"));
            setEmail(bundle.getString("email"));
            setPhone(bundle.getString("phone"));
            setGender(bundle.getString("gender"));
        }

        presenter = new UpdateUserFragmentPresenter(
                this
                ,user_id
                ,image_user
                ,firstName
                ,lastName
                ,email
                ,phone
                ,gender);

        Log.e("NEW IMAGE IS",image_user);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(fragment).popBackStack();
                UserListFragment.offset = 0;
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_user, container, false);
        init(view);
        setGenderAdapter();
        onClickUploadImage(this);
        setUserData();
        onClickButtonSave();

        return view;
    }

    private void init(View view){
        cv_user = view.findViewById(R.id.cardView_update_imageUser);
        iv_user = view.findViewById(R.id.imageView_update_userProfile);
        et_firstName = view.findViewById(R.id.editText_update_firstName);
        et_lastName = view.findViewById(R.id.editText_update_lastName);
        et_email = view.findViewById(R.id.editText_update_email);
        et_phone = view.findViewById(R.id.editText_update_phone);
        spinner_gender = view.findViewById(R.id.spinner_update_gender);
        btn_save = view.findViewById(R.id.button_update_save);
    }

    private void setUserData(){
        String filename=image_user.substring(image_user.lastIndexOf("/")+1);
        if (filename.equalsIgnoreCase("default.png")){
            iv_user.setImageResource(R.drawable.uploadimg);
        }else {
            Glide.with(this).load(image_user).into(iv_user);
        }
        et_firstName.setText(firstName);
        et_lastName.setText(lastName);
        et_email.setText(email);
        et_phone.setPhoneNumber(phone);
        et_email.setText(email);
        presenter.genderCondition(gender);
    }

    private void setGenderAdapter(){
        genderAdapter = ArrayAdapter.createFromResource(
                getContext()
                ,R.array.gender_array
                ,R.layout.spinner_item
        );

        genderAdapter.setDropDownViewResource(
                R.layout.spinner_item
        );

        spinner_gender.setAdapter(genderAdapter);
    }

    //--------------------Update User Image Profile-----------------
    private static final int RESULT_LOAD_IMAGE = 1;

    private void onClickUploadImage(Fragment fragment){
        cv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*ImagePicker.with(fragment)
                       //Crop image(Optional), Check Customization for more option
                       .compress(1024)			//Final image size will be less than 1 MB(Optional)
                       .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                       .start();*/
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {

            Uri selectedImage = data.getData();

            //------Get Image Real Path-----
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            iv_user.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imgPath = picturePath;

            Log.e("PATH**", picturePath);

        }
    }
    //-----------------------------------------------------------------------

    /*private String getRealPathFromURI(Uri contentURI) {

        String thePath = "no-path-found";
        String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            thePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return thePath;
    }*/


    private void onClickButtonSave(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processUpdateUser(
                        imgPath
                        ,et_firstName.getText().toString()
                        ,et_lastName.getText().toString()
                        ,et_email.getText().toString()
                        ,et_phone.getPhoneNumber()
                        ,spinner_gender.getSelectedItem().toString()
                );
            }
        });
    }

    @Override
    public void onUpdateGenderMale(){
        spinner_gender.setSelection(0);
    }

    @Override
    public void onUpdateGenderFemale(){
        spinner_gender.setSelection(1);
    }

    @Override
    public void setErrorFirstName(){
        et_firstName.requestFocus();
        et_firstName.setError("firstname incorrect!");
    }

    @Override
    public void setErrorLastName(){
        et_lastName.requestFocus();
        et_lastName.setError("lastname incorrect!");
    }

    @Override
    public void setErrorEmail(){
        et_email.requestFocus();
        et_email.setError("email incorrect!");
    }

    @Override
    public void setErrorPhone(){
        et_phone.requestFocus();
        et_phone.setError("phone incorrect!");
    }

    @Override
    public void onPopBack(){
        NavHostFragment.findNavController(this).popBackStack();
        UserListFragment.offset = 0;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setImage_user(String image_user) {
        this.image_user = image_user;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage_user() {
        return image_user;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }
}