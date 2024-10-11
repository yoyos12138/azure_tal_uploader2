package TallUploader.entity

import org.zzz.TallUploader.entity.Data

data class ResponseData(
    val error_code:Int,
    val error_reason:String,
    val data: Data?
) {
}