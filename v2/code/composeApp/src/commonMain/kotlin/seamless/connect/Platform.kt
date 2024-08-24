package seamless.connect

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform