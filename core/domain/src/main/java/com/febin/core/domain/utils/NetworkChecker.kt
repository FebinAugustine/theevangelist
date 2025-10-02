package com.febin.core.domain.utils

interface NetworkChecker {
    suspend fun hasInternetConnection(): Boolean
}
