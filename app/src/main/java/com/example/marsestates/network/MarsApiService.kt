package com.example.marsestates.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://mars.udacity.com/"

enum class MarsApiFilter(val value: String) { SHOW_RENT("rent"), SHOW_BUY("buy"), SHOW_ALL("all") }

/**
 * Build the Moshi object that Retrofit will be using; adding the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter
 * with our Moshi object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * Returns a Coroutine [Deferred] [List] of [MarsProperty] which can be fetched with await() if
 * in a Coroutine scope.
 */
interface MarsApiService {
    /**
     * Returns a Retrofit callback that delivers a [List] of [MarsProperty]
     * The @GET annotation indicates that the "realestate" endpoint will be
     * requested with the GET HTTP method
     */
    @GET("realestate")
    fun getPropertiesAsync(@Query("filter") type: String):
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
            Deferred<List<MarsProperty>>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MarsApi {
    val retrofitService: MarsApiService by lazy { retrofit.create(MarsApiService::class.java) }
}