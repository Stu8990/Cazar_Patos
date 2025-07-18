package com.stuart.palma.cazarpatos

data class Player(var username: String, var huntedDucks: Int) {
    constructor(): this("", 0)
}
