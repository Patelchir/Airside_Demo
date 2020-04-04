package com.example.airside_demo.base


import android.accounts.NetworkErrorException
import com.example.airside_demo.network.client.HttpCommonMethod
import com.example.airside_demo.network.client.ResponseHandler
import com.example.airside_demo.network.model.ErrorWrapper
import com.example.airside_demo.network.model.HttpErrorCode
import com.example.airside_demo.network.model.ResponseData
import com.example.airside_demo.utils.DebugLog
import com.google.gson.Gson
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Common class for API calling
 */

open class BaseRepository {

    /**
     * This is the Base suspended method which is used for making the call of an Api and
     * Manage the Response with response code to display specific response message or code.
     * @param call ApiInterface method defination to make a call and get response from generic Area.
     */
    suspend fun <T : Any> makeAPICall(call: suspend () -> Response<ResponseData<T>>): ResponseHandler<ResponseData<T>?> {
        try {
            val response = call.invoke()
            when {
                response.code() == 200 -> {
                    return when (response.body()?.statusCode) {
                        400 -> {

                            ResponseHandler.OnFailed(
                                response.body()?.statusCode!!,
                                response.body()?.message!!,
                                "0"
                            )
                        }
                        401 -> {
                            ResponseHandler.OnFailed(
                                HttpErrorCode.UNAUTHORIZED.code,
                                response.body()?.message!!,
                                response.body()?.statusCode!!.toString()
                            )
                        }
                        else -> ResponseHandler.OnSuccessResponse(response.body())
                    }
                }
                response.code() == 401 -> {
                    val body = response.errorBody()
                    val message: String
                    val bodyString = body?.string()
                    val responseWrapper =
                        Gson().fromJson<ErrorWrapper>(bodyString, ErrorWrapper::class.java)
                    message = if (responseWrapper?.status_code == 401) {
                        if (responseWrapper.errors != null) {
                            HttpCommonMethod.getErrorMessage(responseWrapper.errors)
                        } else {
                            responseWrapper.message.toString()
                        }
                    } else {
                        responseWrapper?.message.toString()
                    }
                    return ResponseHandler.OnFailed(
                        HttpErrorCode.UNAUTHORIZED.code,
                        message,
                        responseWrapper?.message_code.toString()
                    )
                }
                response.code() == 422 -> {
                    val body = response.errorBody()
                    val message: String
                    val bodyString = body?.string()
                    val responseWrapper =
                        Gson().fromJson<ErrorWrapper>(bodyString, ErrorWrapper::class.java)
                    message = if (responseWrapper?.status_code == 422) {
                        if (responseWrapper.errors != null) {
                            HttpCommonMethod.getErrorMessage(responseWrapper.errors)
                        } else {
                            responseWrapper.message.toString()
                        }
                    } else {
                        responseWrapper?.message.toString()
                    }
                    return ResponseHandler.OnFailed(
                        HttpErrorCode.EMPTY_RESPONSE.code,
                        message,
                        responseWrapper?.message_code.toString()
                    )
                }
                response.code() == 500 -> {
                    return ResponseHandler.OnFailed(
                        HttpErrorCode.NOT_DEFINED.code,
                        "",
                        response.body()?.message_code.toString()
                    )
                }
                else -> {
                    val body = response.errorBody()
                    val bodyString = body?.string()
                    val responseWrapper =
                        Gson().fromJson<ErrorWrapper>(bodyString, ErrorWrapper::class.java)
                    val message = if (responseWrapper?.status_code == 422) {
                        if (responseWrapper.errors != null) {
                            HttpCommonMethod.getErrorMessage(responseWrapper.errors)
                        } else {
                            responseWrapper?.message.toString()
                        }
                    } else {
                        responseWrapper?.message.toString()
                    }
                    return if (message.isNullOrEmpty()) {
                        ResponseHandler.OnFailed(
                            HttpErrorCode.EMPTY_RESPONSE.code,
                            message,
                            responseWrapper?.message_code.toString()
                        )
                    } else {
                        ResponseHandler.OnFailed(
                            HttpErrorCode.NOT_DEFINED.code,
                            message,
                            responseWrapper?.message_code.toString()
                        )
                    }
                }
            }
        } catch (e: Exception) {
            DebugLog.print(e)
            return if (
                e is UnknownHostException ||
                e is ConnectionShutdownException
            ) {
                ResponseHandler.OnFailed(HttpErrorCode.NO_CONNECTION.code, "", "")
            } else if (e is SocketTimeoutException || e is IOException ||
                e is NetworkErrorException
            ) {
                ResponseHandler.OnFailed(HttpErrorCode.NOT_DEFINED.code, "", "")
            } else {
                ResponseHandler.OnFailed(HttpErrorCode.NOT_DEFINED.code, "", "")
            }
        }


    }

}