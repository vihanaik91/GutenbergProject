/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gutenberg.network

import com.gutenberg.model.BooksModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val BASE_URL = "http://skunkworks.ignitesol.com:8000/"
/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getBooks] method
 */
interface ApiService {
    /**
     * Returns a [List] of [BooksModel] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "books" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("books")
    suspend fun getBooks(
        @Query("mime_type") mime_type: String?,
        @Query("topic") topic: String?,
        @Query("page") page: Int,
        @Query("search") search: String?
    ): BooksModel
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object BooksApi {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
