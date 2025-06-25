package com.example.mykotlin.viewModel

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.mykotlin.isEmailValid
import com.example.mykotlin.runSuspend
import com.example.mykotlin.swap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlin.reflect.KProperty

fun testAll() {
    Interview.Algorithm.run()
    Interview.Exams.run()
    TestViewModel().run()
    Other().run()
}

class TestViewModel : ViewModel() {
    fun run() {
        test1()
        runSuspend { test2() }
        test3()
        test4()
        test5()
        test6()
    }

    private suspend fun task1(func: String) {
        println("====== task1 started in $func")
        delay(1000)
        println("====== task1 ended in $func")
    }

    private suspend fun task2(func: String) {
        println("====== task2 started in $func")
        delay(2000)
        println("====== task2 ended in $func")
    }

    private suspend fun task3(func: String) {
        println("====== task3 started in $func")
        delay(1500)
        println("====== task3 ended in $func")
    }

    private suspend fun task4(func: String) {
        println("====== task4 started in $func")
        delay(800)
        throw Exception("error occurred in $func")
        println("====== task4 ended in $func")
    }

    private fun test1() { // 通过 launch 启动多个 task 可以按顺序执行异步函数
        println("====== task1 started in order")
        runSuspend {
            task1("test1")
            task2("test1")
            task3("test1")
            println("====== task1 ended in order")
        }
    }

    private suspend fun test2() { // 通过 suspend 可以按顺序执行异步函数
        println("====== task2 started in order")
        task1("test2")
        task2("test2")
        task3("test2")
        println("====== task2 ended in order")
    }

    private fun test3() { // 通过多个 launch 可以并发执行异步函数，任何一个 task throw error 整个 coroutine 都会退出
        println("====== test3 started tasks concurrent")
        runSuspend {
            launch { task1("test3") }
            launch { task2("test3") }
            launch { task3("test3") }
            launch { task4("test3") }
            println("====== test3 ended tasks concurrent")
        }
    }

    private fun test4() { // 通过 async await 来完成并发，如果有任何一个 task throw error，则整个 coroutine 会退出
        println("====== test4 started tasks concurrent")
        runSuspend {
            val deferred1 = async { task1("test4") }
            val deferred2 = async { task2("test4") }
            val deferred3 = async { task3("test4") }
            val deferred4 = async { task4("test4") }
            deferred1.await()
            deferred2.await()
            deferred3.await()
            try {
                deferred4.await()
            } catch (e: Exception) { // 手动捕获也不行，必须借助 supervisorScope 和 CoroutineExceptionHandler，参看 test6()
                println("====== catch " + e.message)
            }
            println("====== test4 ended tasks concurrent")
        }
    }

    private fun test5() { // 通过 async awaitAll 来完成并发
        println("====== test5 started tasks concurrent")
        runSuspend {
            val deferred1 = async { task1("test5") } // 在调用 async 的时候，task1 会立即启动，但是何时结束不确定
            val deferred2 = async { task2("test5") }
            val deferred3 = async { task3("test5") }
            val deferred4 = async { task4("test5") }
            try {
                awaitAll(
                    deferred1,
                    deferred2,
                    deferred4,
                    deferred3
                )// awaitAll 需要 catch error，任何一个 task throw error，整个 coroutine 都会退出
                println("====== test5 ended tasks concurrent")
            } catch (e: Exception) {
                println("====== catch awaitAll " + e.message + " in test5")
            }
        }
    }

    private fun test6() {
        println("====== test6 started tasks concurrent")
        runSuspend {
            supervisorScope { // 使用 supervisorScope 可以隔离 error，在这里当 task4 抛出异常时，会被 runSuspend 里的 CoroutineExceptionHandler 接收，并且不影响其他 tasks 的加载
                launch { task1("test6") }
                launch { task4("test6") }
                launch { task2("test6") }
                launch { task3("test6") }
                println("====== test6 ended tasks concurrent")
            }
        }
    }
}

open class Father {
    public var a = "A"    //public: 公开属性，任何类实例都可访问，是类成员属性的默认访问权限
    private var b = "B"   //private: 私有属性，紧在当前类内可以访问
    protected var c = "C" //protected: 受保护属性，紧当前类及其子类可以访问
    internal var d = "D"  //internal: 内部属性，紧同一模块下可以访问
}

class Son : Father() {
    var e = "E"

    fun run() {
        println(a + c + d)
    }
}

open class Animal(value1: Int) {
    constructor(name: String, value1: Int) : this(value1) {
        println("====== Animal: $name")
    }
}

class Dog : Animal {
    constructor(type: String, age: Int) : super(type, age) {
        println("====== Dog: $type")
    }
}

class NestClass {
    class NestClass1 {
        class NestClass2 {

        }
    }

    internal class InternalClass {
        class InternalClass1 {

        }
    }

    interface NestInterface {
        var test0: String
        val test1: String

        interface InternalInterface {
            fun test1()
            fun test2() {
                println("====== this is test2 in NestInterface")
            }
        }
    }
}

fun NestClass.add(a: Int, b: Int) = a + b

class MyClass {
    fun hello() {
        println("hi there")
    }
}

fun add1(nc: NestClass) {
    println("======add1: ${nc.add(1, 2)}")
}

fun add2(nc: NestClass) {
    nc.add(1, 2)
}

fun throwException() {
    throw IllegalArgumentException("Bad Argument")
    println("throwException() code")
}

fun doMath(a: Int, b: Int) = a / b

inline fun <reified T : Number> append(array: Array<T>, add: T): Array<T> {
    val tmp: MutableList<T> = array.toMutableList()
    tmp.add(add)
    return tmp.toTypedArray()
}

interface Fruit {
    var color: String
    var sweet: Boolean
    var texture: List<String>
    val desc: String
        get() = "Color: $color, Sweet: $sweet, Texture: $texture"

    fun eat()
    fun dispose() {
        println("Trashing leftover")
    }
}

interface Validate {
    fun checkIt(x: Int): String
}

class PCheck {
    companion object : Validate {
        override fun checkIt(x: Int): String {
            if (x > 5) {
                return "Great!"
            } else {
                return "Bah!"
            }
        }

    }
}

data class TestData(var value: Int)

class Other {
    fun run() {
        val s = Son()
        println("====== other: ${s.a}, ${s.d}, ${s.e}")
        //会先调用父类的初始化方法，再调用本类的初始化方法
        println(Dog("dog", 1))

        //
        NestClass.NestClass1.NestClass2()
        NestClass.InternalClass.InternalClass1()
        val obj = object : NestClass.NestInterface.InternalInterface {
            override fun test1() {
                println("====== this is test1 in NestInterface")
            }
        }

        val nc = NestClass()
        println("extension add: ${nc.add(1, 2)}")
        add1(NestClass())
        println(add2(NestClass()))
//        var myNestClass = NestClass.add(1, 2)

        val testString = "expanded"
//        println("========: ${testString[3..5]}")
        println("========chunked: ${testString.chunked(3)[2]}")
        println("========subSequence: ${testString.subSequence(3, 6)}")

//        try {
//            throwException()
//            println("try block code")
//        } finally {
//            println("finally block code")
//        }

//        try {
//            println(doMath(2, 0))
//        } catch (e: Error) {
//            println(e)
//        }

        //反射
        println("====simpleName: ${MyClass::class.java.simpleName}")
        println("====qualifiedName: ${MyClass::class.qualifiedName}")
//        println("====constructors: ${MyClass::class.constructors}")
        println("====forEach: ${MyClass::class.java.methods.forEach(::println)}")
        println("====methods: ${MyClass::class.java.methods}")
        println("====methods[0]: ${MyClass::class.java.methods[0]}")
        println("====DeclaredMethod: ${MyClass::class.java.getDeclaredMethod("hello")}")

        var start = arrayOf(1, 2, 3)
        start = append(start, 4)
        println(start[3])

        test1()
        test2()
        test3()
        test6()
        test8()
        test10()
    }

    fun test1() {
        val keys = listOf("First", "Second", "Third")
        val values = listOf(5, 10, 15)

        var myMap1 = buildMap {
            for (i in keys.indices) {
                put(keys[i], values[i])
            }
        }.toMap()

        var myMap2 = mutableMapOf<String, Int>().apply {
            for (i in keys.indices) {
                this[keys[i]] = values[i]
            }
        }

        //zip 返回的是 List<Pair<String, Int>>
        var myMap3 = keys.zip(values)

        var myMap4: Map<String, Int> = keys.zip(values).toMap()

        var myMap5 = buildMap {
            for (i in keys.indices) {
                put(this[keys[i]], values[i])
            }
        }

        println("===== myMap1: ${myMap1 is LinkedHashMap}")
        println("===== myMap2: ${myMap2 is LinkedHashMap}")
//        println("===== myMap3: ${myMap3 is LinkedHashMap}")
        println("===== myMap4: ${myMap4 is LinkedHashMap}")
//        println("===== myMap5: ${myMap5 is LinkedHashMap}")
    }

    fun test2() {
        class Animal(map: MutableMap<String, String?>) {
            var name: String? by map
            var species: String? by map

            override fun toString() = "$name species $species"
        }

        val thisAnimal = Animal(mutableMapOf("name" to null, "species" to ""))
        thisAnimal.species = "tigris"
        println(thisAnimal)
    }

    fun test3() {
        class Pear : Fruit {
            //typealias 不能在类中出现
//            typealias Pe = Animal

            override var color: String = "Yellow"
            override var sweet: Boolean = true
            override var texture = listOf<String>("A", "B", "C")
            override val desc = "this is a lemon"
            override fun eat() {
                println("Yum, slurp, slurp")
            }

            override fun dispose() {
                println("Toss, toss")
            }
//            override fun dispose(destination: String) {
//                println("Toss, toss")
//            }
        }
    }

    fun test4() {
        var value1 = TestData(2)
        var value2 = TestData(value1.value)
        var value3 = TestData(4)

//        value1.freeze()
//        println(value1.isFrozen) //true mutable
//        println(value2.isFrozen) //true mutable
//        println(value3.isFrozen) //false mutable
    }

    fun test5() {
        class GetName {
            operator fun getValue(thisRef: Any?, prop: KProperty<*>): String {
                val propName = prop.name
                return "The property name is $propName"
            }
        }

//        class UserName {
//            var someName = GetName("This prop value")
//        }

//        class UserName {
//            val someName by GetName() = "This prop value"
//        }

//        class UserName {
//            var someName by GetName()
//        }

        class UserName2 {
            val someName = GetName()
        }

        class UserName3 {
            val someName by GetName()
        }
    }

    fun test6() {
        var c1 = setOf(1, 2, 1, 3, 4, 1)
        println("====== set: $c1,  ${c1.map { it * 2 }.elementAt(2)}")

        var colors = mapOf("Red" to 1, "Green" to 2, "Blue" to 3)
        colors.map { (key, value) -> print("==== $value ") }

        var v1: Long = 1
        var v2: Long = 2
//        test7(v1, v2)

        //HashSet 会自动去重但不保证顺序
        var mySet1 = hashSetOf(3, 1, 2, 1, 5, 4, 4)
        //Set 会自动去重并保证有序，本质上返回的是 LinkedHashSet
        var mySet2 = setOf(3, 1, 2, 1, 5, 4, 4)
        for (value in mySet1) {
            print(value.toString())
        }
        println()
        for (value in mySet2) {
            print(value.toString())
        }
        println()

        val list = listOf("10", "20", "hello", "30", "40")
        for (input in list) {
            val r = input.all {
                println("***" + it.code)
                it.code in 0..9
            }
            //code 的范围应该是 48-57
//            if (!input.all { it.code in 0..9 }) {
//                break
//            }
            if (!input.all { Character.isDigit(it) }) {
                break
            }
            print("$input ")
        }
        println()
    }

    fun test7(value1: Int, value2: Int): Int {
        var result: Int = value1 + value2
        return result
    }

    fun test8() {
        GlobalScope.launch {
            delay(1000)
            println("First Text")
            val sum1 = async {
                delay(500)
                println("cal sum1")
                8 % 2
            }
            val sum2 = async {
                delay(100)
                println("cal sum2")
                6 / 2
            }
            println("sum values")
            val total = sum1.await() + sum2.await()
            delay(1000)
            println("total is $total")
        }
        println("Second Text")
        Thread.sleep(2000)
    }

    fun test9() {
//        val co5: List<Int> = (1..4)
//        val co4 = ArrayList<Int>(1, 2, 3, 4)
        val co3 = (1..4).toList()
//        val co2 = arrayOf<Int>(*1..4).toList()
    }

    fun test10() {
        PCheck.checkIt(6)
        val array = arrayOf(5, 9, 3, 2, 6, 4, 1, 8, 7)
//        array.sort()
//        println("==== " + array.joinToString())

        val op = StringBuilder()
//        //降序排列
//        array.sortWith(Comparator { v1, v2 -> v2 - v1 })
//        println("==== " + array.joinTo(op))
//
        array.sortWith(Comparator { v1, v2 -> v1 - v2 })
        println("==== " + array.joinToString())
    }
}

object Interview {
    enum class ListType {
        Random,
        Ordered,
        Reversed
    }

    object Algorithm {
        fun run() {
            selectionSort()
            bubbleSort()
            insertionSort()
            quickSort()
        }

        private val listType = ListType.Random
        private val SOURCE: List<Int>
            get() = when (listType) {
                ListType.Random -> listOf(4, 3, 5, 9, 6, 2, 8, 7, 1)
                ListType.Ordered -> listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
                ListType.Reversed -> listOf(9, 8, 7, 6, 5, 4, 3, 2, 1)
            }
        private val list = SOURCE.toMutableList()

        private fun selectionSort() {
            for (i in 0 until list.size) {
                var min = i
                for (j in i until list.size) {
                    if (list[j] < list[min]) {
                        min = j
                    }
                }
                if (i != min) {
                    list.swap(i, min)
                }
            }
            println("====== selection sort: $list")
        }

        private fun bubbleSort() {
            for (i in 0 until list.size - 1) {
                var sorted = true
                for (j in 0 until list.size - 1 - i) {
                    if (list[j] > list[j + 1]) {
                        list.swap(j, j + 1)
                        sorted = false
                    }
                }
                if (sorted) {
                    break
                }
            }
            println("====== bubble sort: $list")
        }

        private fun insertionSort() {
            for (i in 1 until list.size) {
                val key = list[i]
                var j = i - 1
                while (j >= 0 && list[j] > key) {
                    list[j + 1] = list[j]
                    j--
                }
                list[j + 1] = key
            }
            println("====== insertion sort: $list")
        }

        private fun quickSort() {
            qsw(list)
            println("====== quick sort: $list")
        }

        private fun qsw(
            list: MutableList<Int>,
            low: Int = 0,
            high: Int = list.size - 1
        ) {
            if (low < high) {
                val index = qsp(list, low, high)
                qsw(list, low, index - 1)
                qsw(list, index + 1, high)
            }
//            val result = quickSortInFilter(list)
//            println("====== quick sort: $result")
        }

        private fun qsp(list: MutableList<Int>, low: Int, high: Int): Int {
            val pivot = list[high]
            var i = low
            for (j in low until high) {
                if (list[j] < pivot) {
                    list.swap(i, j)
                    i++
                }
            }
            list.swap(i, high)
            return i
        }

        private fun quickSortInFilter(list: List<Int>): List<Int> {
            if (list.size <= 1) return list
            val pivot = list[list.size / 2]
            val less = list.filter { it < pivot }
            val equal = list.filter { it == pivot }
            val greater = list.filter { it > pivot }
            return quickSortInFilter(less) + equal + quickSortInFilter(greater)
        }
    }

    object Exams {
        fun run() {
            test1()
            test2()
            test3()
            test4()
        }

        private fun test1() {
            println("====== " + "abc".isEmailValid())
            println("====== " + "abc@dec".isEmailValid())
            println("====== " + "bc@def.hij".isEmailValid())
        }

        class User(var name: String, var age: Int) {
            var country: String? = null
            var enabled = false
        }

        private fun test2() {
            val dataSource = listOf(1, 2, null, 4, 5, null, 7, 8, null)
            val mappedList = dataSource.filterNotNull().map { it }
            println("====== $mappedList")

//            dataSource.add(10) //error dataSource is immutable
//            val mappedList2 = dataSource.filterNotNull().map { it * it }
//            println(mappedList2)
        }

        private suspend fun fetchUserData(): String {
            delay(1000)
            return "====== User Data"
        }

        private suspend fun fetchUserPosts(): String {
            delay(1500)
            return "====== User Posts"
        }

        private fun test3() {
            runBlocking {
                val posts = fetchUserPosts()
                println(posts)
                val data = fetchUserData()
                println(data)
            }
        }

        private fun test4() {
            println("====== test4 started")
            runBlocking {//runBlocking 会阻塞当前线程，直到当前的协程和子协程全部执行完毕之后才会执行 runBlocking 后面的代码
                delay(1000)
                println("====== runBlocking executed")
                return@runBlocking
            }
            println("====== test4 ended")
        }

        data class User2(val id: Int, val name: String)

        sealed class Result {
            data class Success(val user2: User2) : Result()
            data class Error(val message: String) : Result()
            data object Loading : Result()
        }

        sealed class Result1 : Result() {

        }

        private fun handleResult(result: Result) {
            println(
                when (result) {
                    is Result.Error -> result.message
                    Result.Loading -> ""
                    is Result.Success -> result.user2.name + result.user2.id
                }
            )
        }

        @Composable
        private fun UserList(list: List<User>) {
            val context = LocalContext.current
            LazyColumn {
                items(list) {
                    Text(text = "Name:${it.name}, Age:${it.age}", modifier = Modifier.clickable {
                        Toast.makeText(context, "Clicked:${it.name}", Toast.LENGTH_SHORT).show()
                    })
                }
            }
        }
    }
}