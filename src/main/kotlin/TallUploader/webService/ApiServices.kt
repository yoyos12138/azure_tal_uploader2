package TallUploader.webService

import TallUploader.entity.RequestData
import TallUploader.entity.ResponseData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiServices {

    @POST("api/public/azure/blob/getToken")
    fun getToken(@Body requestData: RequestData):Call<ResponseData>


    @POST("api/public/azure/blob/getToken")
    suspend fun getToken0(@Body requestData: RequestData):Response<ResponseData>


}