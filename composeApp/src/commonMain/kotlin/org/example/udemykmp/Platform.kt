package org.example.udemykmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform