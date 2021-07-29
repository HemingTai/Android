package com.example.mykotlin

class Test {
    var name = ""
}

fun main() {
    val m = Test().apply { name = "123" }
    println(m.name)
}