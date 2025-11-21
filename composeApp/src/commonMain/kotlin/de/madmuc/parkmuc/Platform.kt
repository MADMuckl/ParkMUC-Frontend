package de.madmuc.parkmuc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform