package remote

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ServiceAPI : Application() {

    lateinit var requestAPI: RequestAPI

    override fun onCreate() {
        super.onCreate()

        configure()
    }

    private fun configure() {
        val httpLoginInterceptor = HttpLoggingInterceptor()
        httpLoginInterceptor.level = BODY

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(httpLoginInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://178.63.9.114:7777/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        requestAPI = retrofit.create(RequestAPI ::class.java)
    }
}