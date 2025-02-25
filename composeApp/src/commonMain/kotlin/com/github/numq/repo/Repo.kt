package com.github.numq.repo

data class Repo(
    val id: Long,
    val name: String,
    val description: String,
    val url: String,
    val fullName: String,
    val stargazers: Int,
    val forks: Int,
    val isPrivate: Boolean,
    val clones: Int,
    val uniqueCloners: Int,
    val views: Int,
    val uniqueVisitors: Int,
    val createdAt: Long,
    val pushedAt: Long,
)