package zbi.trunkstrop.core.network

import android.app.Application
import android.content.Context
import com.squareup.moshi.Moshi
import io.reactivex.schedulers.Schedulers.io
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import zbi.trunkstrop.core.BuildConfig
import java.io.File

fun cache(application: Application): Cache? {
    if (BuildConfig.DEBUG) {
        val configPrefs = application.getSharedPreferences("local-config", Context.MODE_PRIVATE)
        if (!configPrefs.getBoolean("enable_cache", false)) {
            return null
        }
    }

    return try {
        Cache(File(application.cacheDir, "cache-default"), 10 * 1024 * 1024)
    } catch (_: Exception) {
        null
    }
}

fun okhttpBuilder(
    application: Application,
    cache: Cache? = cache(application)
): OkHttpClient.Builder {
    val builder = OkHttpClient.Builder()
        .cache(cache)

    customizeOkhttpBuilder(builder)

    return builder
}

fun moshi(): Moshi {
    return Moshi.Builder()
        .add(ZonedDateTimeAdapter())
        .build()
}

fun retrofitBuilder(
    application: Application,
    okhttp: OkHttpClient = okhttpBuilder(application).build(),
    moshi: Moshi = moshi()
): Retrofit.Builder {
    return Retrofit.Builder()
        .client(okhttp)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(io()))
        .addConverterFactory(UnitConverterFactory)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
}