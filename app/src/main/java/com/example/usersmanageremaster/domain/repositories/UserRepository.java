package com.example.usersmanageremaster.domain.repositories;

import com.example.usersmanageremaster.Models.DeleteUser;
import com.example.usersmanageremaster.Models.InsertUser;
import com.example.usersmanageremaster.Models.ReturnUsers;
import com.example.usersmanageremaster.Models.UpdateUser;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserRepository {

    //@Headers("Authorization:Bearer d94ad5c0b23bc4ef0b39425b2ef1e15e")



    @GET("/users_api/GetUserList.php")
    Observable<Response<ReturnUsers>>
    getUsers(@Query("token") String token
            , @Query("count") String count
            , @Query("offset") String offset);

    @GET("/users_api/GetUserList.php")
    Observable<Response<ReturnUsers>>
    searchUsers(@Query("search") String search);

    @POST("/users_api/InsertUser.php")
    Call<InsertUser> insertUser(@Body InsertUser insertUser);

    @Multipart
    @POST("/users_api/insertImageFile.php")
    Call<ResponseBody>
    uploadImage(@Part MultipartBody.Part image);

    @POST("/users_api/UpdateUser.php")
    Call<UpdateUser> updateUser(@Body UpdateUser updateUser);

    @POST("/users_api/DeleteUser.php")
    Call<DeleteUser> deleteUser(@Body DeleteUser deleteUser);

}
