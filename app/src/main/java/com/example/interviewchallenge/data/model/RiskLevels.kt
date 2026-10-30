package com.example.interviewchallenge.data.model

import com.example.interviewchallenge.R

enum class RiskLevels {
    SAFE, MEDIUM, HIGH;

    companion object {
        fun toPersian(riskLevel: String?): Int {
            return when (riskLevel?.uppercase()) {
                HIGH.name -> R.string.filter_high
                MEDIUM.name -> R.string.filter_suspicious
                SAFE.name -> R.string.filter_safe
                else -> R.string.filter_safe
            }
        }
    }
}
