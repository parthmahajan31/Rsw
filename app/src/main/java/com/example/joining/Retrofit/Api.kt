package com.example.xicomtask.Retrofit

import com.example.joining.Models.GetCategoryModel
import com.example.joining.Models.SaveModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("get_categories.php")
    fun getData(): Call<GetCategoryModel>

    @Multipart
    @POST("save_user.php")
    fun saveData (@Part("category_id") category_id : RequestBody,
                  @Part("name") name : RequestBody,
                  @Part("desc") desc : RequestBody,
                  @Part("expiry") expiry : RequestBody,
                  @Part product_image : ArrayList<MultipartBody.Part>
    ) : Call<SaveModel>
}