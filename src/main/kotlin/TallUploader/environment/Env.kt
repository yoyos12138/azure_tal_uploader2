package TallUploader.environment

class Env {
    companion object{
        private const val URL_TEST_ENVIRONMENT="https://overseas-pad.chengjiukehu.com/";
        //测试环境
        private const val URL_PRODUCT_ENVIRONMENT="https://pad-api.thinkbuddy.com/"
        //生产环境

        @JvmStatic
        fun getUrl(envType: Int):String{
            //获取当前的url
            return when (envType){
                EnvType.TEST -> URL_TEST_ENVIRONMENT
                EnvType.PRODUCT-> URL_PRODUCT_ENVIRONMENT
                else -> throw IllegalArgumentException("unknown envType,please check your code")
            }

        }
    }
}