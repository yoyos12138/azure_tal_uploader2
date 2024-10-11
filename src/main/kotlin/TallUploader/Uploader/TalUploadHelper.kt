package TallUploader.Uploader

import TallUploader.entity.RequestData
import TallUploader.environment.Env
import TallUploader.webService.ApiServices
import com.azure.storage.blob.BlobContainerClientBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.zzz.TallUploader.entity.Data
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class TalUploadHelper() {

    /*
    *  1.获取请求参数    RequestData     deviceId appId state
    *  2.从请求参数获取  相应信息 ResponseData
    *  3.从响应信息获取  Data和token
    */

    private var requestData:RequestData?=null
    private var data:Data?=null
    private var url:String?=null


    private var retrofit:Retrofit?=null
    private var apiServices:ApiServices?=null
    //web请求


    //初始化
    fun init(
        envType: Int,               //环境类型
        requestData: RequestData    //获取请求参数
    ){
        url=Env.getUrl(envType)
        this.requestData=requestData
        //初始化web请求参数

        retrofit= url?.let {
            Retrofit.Builder()
                .baseUrl(it)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        apiServices= retrofit?.create(ApiServices::class.java)
        //初始化web部分
        isInited=true
    }


    //更新令牌协程版本
    private suspend fun updateTokenCroroutineAsync(){
        val data= requestData?.let { apiServices?.getToken0(it) }?.body()?.data?.apply {
            createTime=System.currentTimeMillis()
        }
        this.data=data
        println("我执行了")
    }


    //上传单个文件
    fun uploadSingleFile(absolutePath:String){
        if (!File(absolutePath).exists()){
            throw IllegalArgumentException("filePath: $absolutePath is not exists")
        }else{
            CoroutineScope(Dispatchers.Default).launch {
                updateTokenCroroutineAsync()
                while (data?.tokenIsExpire() == true){
                    //updateTokenSync()
                    updateTokenCroroutineAsync()
                    println("更新token")
                }//确保令牌始终有效

                println("令牌有效")

                if (data!=null){
                    val blobContainerClient=BlobContainerClientBuilder()
                        .endpoint(data!!.host)
                        .sasToken(data!!.token)
                        .containerName(data!!.containerName)
                        .buildClient()
                    //填入上传信息

                    val serverPath="${data!!.basePath}/${if (absolutePath.startsWith("/")) absolutePath.drop(1) else absolutePath}"
                    //删除绝对路径前面的斜杠/

                    val blobClient=blobContainerClient.getBlobClient(serverPath)
                    //上传文件

                    println(serverPath)

                    isUploading=true
                    blobClient.uploadFromFile(absolutePath)
                    isUploading=false
                }
            }

        }
    }


    fun uploadMultFiles(absolutePaths:Array <String>){
        CoroutineScope(Dispatchers.Default).launch {
            updateTokenCroroutineAsync()
            while (data?.tokenIsExpire() == true){
                //updateTokenSync()
                updateTokenCroroutineAsync()
                println("更新token")
            }//确保令牌始终有效

            println("令牌有效")

            if (data!=null){
                val blobContainerClient=BlobContainerClientBuilder()
                    .endpoint(data!!.host)
                    .sasToken(data!!.token)
                    .containerName(data!!.containerName)
                    .buildClient()

                val task:ArrayList<Job> = arrayListOf()

                absolutePaths.forEach {absolutePath->
                    task.add(launch {
                        val serverPath="${data!!.basePath}/${if (absolutePath.startsWith("/")) absolutePath.drop(1) else absolutePath}"
                        val blobClient=blobContainerClient.getBlobClient(serverPath)
                        blobClient.uploadFromFile(absolutePath)
                    })
                }





            }
        }
    }


    var isInited:Boolean=false
    var isReleased:Boolean=false
    var isUploading:Boolean=false




    //清理释放资源
    fun release(){
        requestData=null
        data=null
        url=null
        requestData=null
        apiServices=null

        isReleased=true
        isInited=false
    }







}