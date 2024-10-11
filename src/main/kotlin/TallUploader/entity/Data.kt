package org.zzz.TallUploader.entity

data class Data(
    val token:String,
    val host:String,
    val containerName:String,
    val basePath:String,
    val expiresTime:Long,
    //貌似获取的过期时间是一个0-9分钟的循环动态变化的时间


    var createTime:Long=System.currentTimeMillis()
    //获取的时候获取当前unix时间戳
) {
    fun tokenIsExpire():Boolean{
        val nowTime=System.currentTimeMillis()
        return nowTime>expiresTime+createTime
    }

}