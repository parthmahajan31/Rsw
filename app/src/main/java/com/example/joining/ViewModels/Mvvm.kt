package com.example.joining.ViewModels

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.joining.Models.GetCategoryModel
import com.example.joining.Models.SaveModel
import com.example.joining.Utils.Util
import com.example.xicomtask.Retrofit.Api
import com.example.xicomtask.Retrofit.ApiClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Mvvm : ViewModel() {
    val api = ApiClient.getClient()?.create(Api::class.java)
    private lateinit var getCategoryDetail : MutableLiveData<GetCategoryModel>

    fun getData (activity: Activity): LiveData<GetCategoryModel>{

        getCategoryDetail = MutableLiveData<GetCategoryModel>()

        when{
            Util.isNetworkConnected(activity)->{
                Util.showProgress(activity, "Please Wait")
                api?.getData()?.enqueue(object : Callback<GetCategoryModel>{
                    override fun onFailure(call: Call<GetCategoryModel>, t: Throwable) {
                        Util.dismissDialog()
                        Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<GetCategoryModel>,
                        response: Response<GetCategoryModel>
                    ) {
                        Util.dismissDialog()
                       if (response.body()!=null){
                           getCategoryDetail.value = response.body()
                       }
                    }

                })
            }
        }

        return getCategoryDetail
    }


    private lateinit var saveDataDetail : MutableLiveData<SaveModel>

    fun saveData (activity: Activity, category_id : RequestBody,name : RequestBody,desc : RequestBody,expiry : RequestBody,product_image : ArrayList<MultipartBody.Part>): LiveData<SaveModel>{

        saveDataDetail = MutableLiveData<SaveModel>()

        when{
            Util.isNetworkConnected(activity)->{
                Util.showProgress(activity, "Please Wait")
                api?.saveData(category_id, name, desc, expiry, product_image)?.enqueue(object : Callback<SaveModel>{
                    override fun onFailure(call: Call<SaveModel>, t: Throwable) {
                        Util.dismissDialog()
                        Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<SaveModel>,
                        response: Response<SaveModel>
                    ) {
                        Util.dismissDialog()
                        if (response.body()!=null){
                            saveDataDetail.value = response.body()
                        }
                    }

                })
            }
        }

        return saveDataDetail
    }

}