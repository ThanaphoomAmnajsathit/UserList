package com.example.usersmanageremaster.presentations.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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


import com.example.usersmanageremaster.R;
import com.example.usersmanageremaster.interfaces.InsertUserFragmentInterface;
import com.example.usersmanageremaster.presentations.presenters.InsertUserFragmentPresenter;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.lamudi.phonefield.PhoneEditText;


public class InsertUserFragment extends Fragment implements InsertUserFragmentInterface.view {

    private final InsertUserFragment fragment = this;

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

    //---presenter---
    private InsertUserFragmentPresenter presenter;

    //---drop down menu---
    ArrayAdapter<CharSequence> genderAdapter;



    public InsertUserFragment() {
        // Required empty public constructor
        presenter = new InsertUserFragmentPresenter(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        view = inflater.inflate(R.layout.fragment_insert_user, container, false);
        init(view);
        setGenderAdapter();
        onClickUploadImage(this);
        onClickButtonSave();
        return view;
    }

    private void init(View view){
        cv_user = view.findViewById(R.id.cardView_imageUser);
        iv_user = view.findViewById(R.id.imageView_userProfile);
        et_firstName = view.findViewById(R.id.editText_firstName);
        et_lastName = view.findViewById(R.id.editText_lastName);
        et_email = view.findViewById(R.id.editText_email);
        et_phone = view.findViewById(R.id.editText_phone);
        spinner_gender = view.findViewById(R.id.spinner_gender);
        btn_save = view.findViewById(R.id.button_save);
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

    //------------------PickImage From Gallery User Image Profile-----------------
    private static final int RESULT_LOAD_IMAGE = 1;
    private void onClickUploadImage(Fragment fragment){
        cv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* ImagePicker.with(fragment)
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
    //-------------------------------------------------------------------

    private void onClickButtonSave(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.CheckNameCondition(
                        imgPath
                        , et_firstName.getText().toString()
                        , et_lastName.getText().toString()
                        , et_email.getText().toString()
                        , et_phone.getPhoneNumber()
                        , spinner_gender.getSelectedItem().toString());
            }
        });
    }

    @Override
    public void onInsertSuccessfully(){
        NavHostFragment.findNavController(this).popBackStack();
        UserListFragment.offset = 0;
    }

    @Override
    public void onInsertUser(){
        Log.d("Start Insert","Started!");
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

}