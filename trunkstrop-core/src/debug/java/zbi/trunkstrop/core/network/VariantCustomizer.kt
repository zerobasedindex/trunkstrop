package zbi.trunkstrop.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun customizeOkhttpBuilder(builder: OkHttpClient.Builder) {
    builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
}