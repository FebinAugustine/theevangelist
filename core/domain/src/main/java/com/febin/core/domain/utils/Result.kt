package com.febin.core.domain.utils


/**
 * Sealed Result for async operations (MVI-friendly).
 * - Emits loading, success, failure.
 */
sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val failure: com.febin.core.domain.utils.Failure) : Result<Nothing>()
    data class IsFailure(val exception: Throwable) : Result<Nothing>()
    fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Loading -> Loading
        is Success -> Success(transform(data))
        is Failure -> Failure(failure)
        is IsFailure -> IsFailure(exception)
    }

    companion object {
        fun <T> loading(): Result<T> = Loading as Result<T>
        fun <T> success(data: T): Result<T> = Success(data)
        fun failure(failure: com.febin.core.domain.utils.Failure): Result<Nothing> = Failure(failure)
        fun isFailure(exception: Throwable): Result<Nothing> = IsFailure(exception)
    }
}

fun <T> Result<T>.isLoading(): Boolean = this is Result.Loading
fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success
fun <T> Result<T>.getOrNull(): T? = (this as? Result.Success)?.data

fun <T> Result<T>.exceptionOrNull(): Throwable? = (this as? Result.IsFailure)?.exception