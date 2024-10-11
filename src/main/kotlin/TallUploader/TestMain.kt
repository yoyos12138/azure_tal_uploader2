package TallUploader

import TallUploader.Uploader.TalUploadHelper
import TallUploader.entity.RequestData
import TallUploader.environment.EnvType
import kotlinx.coroutines.runBlocking

fun main()= runBlocking {
    val uploader=TalUploadHelper().apply {
        init(EnvType.TEST, RequestData(
            "123456789",
            "10004",
            "public"))
    }

    uploader.uploadSingleFile("/home/zzz/4.txt")

    while (true){
        val a=1
    }

    uploader.release()
}