package com.example.mykotlin

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import java.util.Locale
import kotlin.math.pow

fun getNullableLength(ns: String?) {
    println("for $ns:")

    val list = listOf("a", "b", "c")
    //with用于lambda表达式不需要返回值，可被看做是用某个东西做下面的事
    with(list) {
        println("with called")
    }
    //for optional only, let是判断optional的值是否为空
    ns?.let {
        println("let: $it")
    }
    //run和with类似，但是调用了let作为返回值，常用于初始化和计算返回值
    ns?.run {
        println("\tis empty? " + isEmpty())
        println("\tlength = $length")
    }
    //apply常用于对象的初始化赋值
    val model = Model().apply {
        name = "a"
    }
    println("apply called: ${model.name}")
    //also 常用于将此对象作为参数的一些操作
    ns.also {
        println("also called：$it")
    }
    //takeIf常用于条件判断,
    var result = ns.takeIf {
        it == "a"
    } ?: "d"
    println("takeIf called: $result")

    result = ns.takeUnless {
        it == "b"
    } ?: "c"
    println("takeUnless called: $result")
}

fun testBasicSyntax() {
    val a = "a" //常量
    var b = "b" //变量
    b = "z"
    //字符串插值
    println("a:$a, b:${b}")

    var c: String? = null //可选类型
    println(c ?: "default") //提供默认值
    val len = c?.length ?: 0
    println("c's length is $len")
    c = "123"
    println("c: $c")
    c = null
    //c.let{} 是指当c不为空的时候执行{}
    println("c_let: ${c.let { "c is not null" }}")
    c = "6"

    //条件表达式,类似于三目运算符, 可嵌套
    val e = if (c.toInt() > 5) 4 else if (c.toInt() < 8) 5 else 6
    println("e: $e")

    //判断数据类型 是 不是
    if (e is Int) {
        println("e is Int")
    }
    val d = false
    if (d !is Boolean) {
        println("d is not Boolean")
    }

    //判断元素是否在集合内 不在 在
    val f = intArrayOf(1, 2, 3, 4, 5)
    if (6 !in f) {
        println("6 is not an element of f")
    }
    if (4 in f) {
        println("4 is an element of f")
    }

    //区间：闭区间 步进 左开右闭 倒序
    print("区间1：")
    for (i in 0..6) print(i)
    print(" 区间2：")
    for (i in 0..10 step 2) print(i)
    print(" 区间3：")
    for (i in 0 until 10) print(i)
    print(" 区间4：")
    for (i in 10 downTo 10) print(i)
    val j = 6
    //!in 表示不在某个区间内
    if (j !in 0..6) print("j not in 0..6")
    println()

    //遍历
    val array = listOf(1, 2, 3, 4, 5, 6)
    print("iterator item: ")
    for (item in array) print(item)
    print(" index: ")
    for (i in array.indices) print(array[i])
    print(" index and item:")
    for ((index, item) in array.withIndex()) print(" (index: $index, item: ${item})")
    println()
    array.forEach { println(it) }

    //do-while
    var g = 5
    do {
        print("${g--} ")
    } while (g > 0)
    println()

    //when
    when (c.toInt()) {
        1 -> println("when: 1")
        2, 3 -> println("when: 2 or 3")
        4 -> println("when: 4")
        in 1..10 -> println("when in 1..10")
        is Int -> println("5")
        else -> println("when: $c")
    }

    //loop@ 给表达式加上标签，在break的时候可以指定标签
    loop@ for (i in 1..10) {
        for (j in 4..8) {
            if (i == 6) break@loop
            print("${i + j} ")
        }
        println()
    }

    //高阶函数
    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println(list.filter { it > 5 }.map { it * 2 })
    val map = mapOf("a" to 1, "b" to 2, "c" to 3)
    println(map.forEach { print("${it.key}:${it.value} ") })

    // any: 至少有一个大于5
    println(list.any { it > 5 })
    // all: 所有的都大于5
    println(list.all { it > 5 })
    // none: 没有一个小于1
    println(list.none { it < 1 })
    // first: 第一个大于5
    println(list.first { it > 5 })
    // last: 最后一个大于5
    println(list.last { it > 5 })

    val words = listOf("Lets", "find", "something", "in", "collection", "somehow")
    //find: 找到第一个匹配的element
    println(words.find { it.startsWith("some") })
    //findLast: 找到最后一个匹配的element
    println(words.findLast { it.startsWith("some") })
    //find | findLast: 找不到返回null
    println(words.find { it.contains("nothing") })
    //firstOrNull | lastOrNull: 找到则返回，找不到则返回null
    println(words.firstOrNull { it.endsWith("n") })
    println(words.lastOrNull { it.endsWith("f") })

    val people = listOf(
        Person1("John", "Boston", "+1-888-123456"),
        Person1("Sarah", "Munich", "+49-777-789123"),
        Person1("Svyatoslav", "Saint-Petersburg", "+7-999-456789"),
        Person1("Vasilisa", "Saint-Petersburg", "+7-999-123456")
    )
    //associateBy 和 groupBy的区别在于处理相同key的时候:
    //associateBy 使用最后一个元素的value
    //groupBy 把相同key的value放在一个list里面
    println(people.associateBy { it.phone })
    println(people.associateBy(Person1::phone, Person1::city))
    println(people.groupBy(Person1::city, Person1::name))
    println(people.associateBy(Person1::city, Person1::name))

    val numbers = listOf(1, -2, 3, -4, 5, -6)
    //partition: 按照给定的条件将原始数据分成两个部分，生成一个pair
    val (positives, negatives) = numbers.partition { it > 0 }
    println(positives)
    println(negatives)

    val fruitsBag = listOf("apple", "orange", "banana", "grapes")
    val clothesBag = listOf("shirts", "pants", "jeans")
    val cart = listOf(fruitsBag, clothesBag)
    //flatMap | flatten: 将二维数组降维成一维数组
    println(cart.flatMap { it })
    println(cart.flatten())

    val empty = emptyList<Int>()
    val only = listOf(3)
    //minOrNull | maxOrNull: 找到则返回，找不到则返回null
    println("Numbers: $numbers, min = ${numbers.minOrNull()} max = ${numbers.maxOrNull()}")
    println("Empty: $empty, min = ${empty.minOrNull()}, max = ${empty.maxOrNull()}")
    println("Only: $only, min = ${only.minOrNull()}, max = ${only.maxOrNull()}")

    println(numbers.sorted())
    println(numbers.sortedBy { -it })
    println(numbers.sortedByDescending { -it })

    val list1 = listOf("a", "b", "c")
    val list2 = listOf(1, 2, 3, 4)
    //zip: 会生成一个list，其中的元素item是一个pair，list的size等于最小数组的size，还可以给定一个zip的条件
    println(list1 zip list2)
    val resultReduce = list1.zip(list2) { a, b -> "$a$b" }
    println(resultReduce)

    //getOrElse: 获取给定index的值，如果超出则使用提供的默认值
    println(list1.getOrElse(1) { "d" })
    println(list1.getOrElse(10) { "d" })

    val map1 = mutableMapOf<String, Int?>()
    //getOrElse: 同样适用于map
    println(map1.getOrElse("x") { 1 })       // 1
    map1["x"] = 3
    println(map1.getOrElse("x") { 1 })       // 2
    map1["x"] = null
    println(map1.getOrElse("x") { 1 })

    // 懒加载
    var h = lazy { "abc" }
}

// 有返回值
fun sum0(a: Int, b: Int): Int {
    return a + b
}

// 若返回值只有一条语句，则可写成如下形式
fun sum1(a: Int, b: Int, c: Int): Int = a + b + c

// 若返回值类型可被推测，则返回值类型可省略
fun sum2(a: Int, b: Int, c: Int) = a + b + c

// 无返回值时，可将返回值类型写成Unit或省略
fun voidFunc0(): Unit {}
fun voidFunc1() {}

// 可变长参数
fun changeable(vararg v: Int) {
    v.forEach { print("$it ") }
}

// 类的修饰符
// abstract  抽象类
// final      类不可继承，默认属性
// enum       枚举类
// open       类可继承，类默认是final的
// annotation 注解类
open class Shape {
    open fun area() {}
}

class Circle {
    var name = "Circle" //默认实现了get set
    val type = "Shape" //常量只有get方法

    // private    仅在同一个文件中可见
    // protected  同一个文件中或子类可见
    // public     所有调用的地方都可见
    // internal   同一个模块中可见
    private var desc: String?
        //重写get，set
        get() = name.toUpperCase()
        set(value) {
            if (value != null) {
                desc = if (value.contains("a")) {
                    value.toLowerCase()
                } else {
                    value.toUpperCase()
                }
            }
        }
}

class Rectangle(len: Int) : Shape(), TestInterface {
    var width: Int = 0
    var length = len

    //次构造器
//    constructor(width: Int) {
//        this.width = width
//    }

    //如果类有主构造函数，每个次构造函数都要直接或间接通过另一个次构造函数代理主构造函数。
    //在同一个类中代理另一个构造函数使用 this 关键字
    constructor(width: Int, length: Int) : this(length) {
        this.width = width
    }

    override fun area() {
        super.area()
        println("The rectangle's area is ${width * length}")
    }

    override fun test() {
        println("test rectangle")
    }
}

//类可以有一个主构造器，以及一个或多个次构造器，主构造器是类头部的一部分，位于类名称之后
class Square0 constructor(len: Double) {}

//如果主构造器没有任何注解，也没有任何可见度修饰符，那么constructor关键字可以省略
class Square(len: Double) : Shape() {
    private var length = len

    override fun area() {
        super.area()
        println("The square's area is ${length.pow(2.0)}")
    }
}

// 接口，类似于swift中的协议
interface TestInterface {
    fun test()
}

open class A {
    open val a = ""
    open var b = 1
    fun a() {}
    open fun f() {
        println("A")
    }
}

interface B {
    // 接口中的属性只能是抽象的，不允许初始化值，接口不会保存属性值，实现接口时，必须重写属性
    var c: Boolean

    fun b() {}
    fun f() {
        println("B")
    }
}

class C : A(), B {
    // 可以用一个var属性重写一个val属性，但是反过来不行，
    // 因为val属性本身定义了getter方法，重写为var属性会在衍生类中额外声明一个setter方法
    override var a = "1"
    override var b: Int = super.b
    override var c: Boolean
        get() = false
        set(value) {
            c = value
        }

    override fun f() {
        // 同名函数调用需传入类型
        super<A>.f()
        super<B>.f()
    }

    fun baz() {
        println("C baz")
    }

    fun D.foo() {
        bar() // 调用 D.bar
        baz() // 调用 C.baz
    }

    fun caller(d: D) {
        d.foo()
        d.testSameFun()
    }

    fun test() {
        println("C Test")
    }

    fun D.testSameFun() {
        // 在某类内扩展方法中调用同名函数时，优先调用外类的方法，调用当前类的方法时需要加上 this 关键字
        test() // 调用 D.Test()
        this@C.test() // 调用 C.Test()
    }
}

class D {
    fun bar() {
        println("D bar")
    }

    fun test() {
        println("D Test")
    }
}

class User(var name: String) {

    fun foo() {
        println("foo")
    }
}

// 扩展函数可以在已有类中添加新的方法，不会对原类做修改，扩展函数定义形式
// fun receiverType.functionName(params){
//    body
//}
fun User.getName() {
    println("The user's name is $name")
}

// 若扩展函数和成员函数一致，则会优先使用成员函数。
fun User.foo() {
    println("foo external")
}

fun Rectangle.test() {
    println("Rectangle test")
}

fun Shape.test() {
    println("Shape test")
}

// 扩展函数是静态解析的，并不是接收者类型的虚拟成员，在调用扩展函数时，具体被调用的的是哪一个函数，由调用函数的的对象表达式来决定的，而不是动态的类型决定的
fun shapeTest(s: Shape) {
    s.test()
}

// 对属性进行扩展，扩展属性允许定义在类或者kotlin文件中，不允许定义在函数中
// 初始化属性因为属性没有后端字段（backing field），所以不允许被初始化，只能由显式提供的 getter/setter 定义
val <T> List<T>.lastIndex: Int
    get() = size - 1

// 在扩展函数内，可以通过 this 来判断接收者是否为 NULL,这样即使接收者为 NULL,也可以调用扩展函数
fun Any?.toString(): String {
    if (this == null) return "null"
    // 空检测之后，“this”会自动转换为非空类型，所以下面的 toString()
    // 解析为 Any 类的成员函数
    return toString()
}

open class E {}
class E1 : E() {}
open class F {
    /* 以成员的形式定义的扩展函数, 可以声明为 open , 而且可以在子类中覆盖 */
    open fun E.foo() {
        println("E.foo in F")
    }

    open fun E1.foo() {
        println("E1.foo in F")
    }

    fun caller(e: E) {
        e.foo()
    }
}

open class F1 : F() {
    override fun E.foo() {
        println("E.foo in F1")
    }

    override fun E1.foo() {
        println("E1.foo in F1")
    }
}

// 如果一个类定义有一个伴生对象，也可以为伴生对象定义扩展函数和属性
// 伴生对象通过"类名."形式调用，伴生对象声明的扩展函数，通过用类名限定符来调用
class MyClass {
    companion object {}
}

// 给伴生对象扩展方法
fun MyClass.Companion.foo() {
    println("MyClass.Companion.foo")
}

// 给伴生对象扩展属性
val MyClass.Companion.no: Int
    get() = 10

// 创建一个只包含数据的类，关键字为 data
// 编译器会自动的从主构造函数中根据所有声明的属性提取以下函数：
//
// equals() / hashCode()
// toString() 格式如 "User(name=John, age=42)"
// componentN() functions 对应于属性，按声明顺序排列
// copy() 函数

// 如果这些函数在类中已经被明确定义了，或者从超类中继承而来，就不再会生成。
// 为了保证生成代码的一致性以及有意义，数据类需要满足以下条件：
//
// 主构造函数至少包含一个参数。
// 所有的主构造函数的参数必须标识为val 或者 var ;
// 数据类不可以声明为 abstract, open, sealed 或者 inner;
// 数据类不能继承其他类 (但是可以实现接口)。
data class Person(var name: String, var age: Int) {
    fun setName_test(name: String) {
        this.name = name
    }
}

data class Person1(val name: String, val city: String, val phone: String)

class Person2 {
    var name = "A"
    var age = 0

    override fun toString() = "The person name is $name, and age is $age"
}

// 声明泛型类，泛型可以用在类，接口，方法上面
class Box<T>(t: T) {
    var value = t
}

// 定义泛型类型变量，可以完整地写明类型参数，如果编译器可以自动推定类型参数，也可以省略类型参数
fun <T> boxIn(value: T) = Box(value)

fun <T : Comparable<T>> sort(list: List<T>) {}

// 每一个枚举都是枚举类的实例，它们可以被初始化
// 值从0开始。若需要指定值，则可以使用其构造函数
enum class Color(value: Int) {
    RED(0),
    GREEN(3),
    BLUE(5)
}

// Kotlin 使用 object 关键字来声明一个对象。
// Kotlin 中我们可以方便的通过对象声明来获得一个单例
object Site {
    var url: String = ""
    val name: String = "百度"
}

interface Base {
    fun run()
}

class BaseImp(private val x: String) : Base {
    override fun run() {
        println("$x is running")
    }
}

class Cat(b: Base) : Base by b {
    fun foo() {
        run()
    }
}

// lamda函数: 以下几种表达式相等
val upperCase1: (String) -> String = { str: String -> str.toUpperCase(Locale.ROOT) }
val upperCase2: (String) -> String = { str -> str.toUpperCase(Locale.ROOT) }
val upperCase3 = { str: String -> str.toUpperCase(Locale.ROOT) }
val upperCase4: (String) -> String = { it.toUpperCase(Locale.ROOT) }

//::是function pointer
val upperCase5: (String) -> String = ::customUppercase

fun customUppercase(str: String): String {
    return str.toUpperCase(Locale.ROOT)
}

fun main() {
    testBasicSyntax()
    println("sum = " + sum0(3, 4))
    changeable(0)
    changeable(1, 2, 3)

    // 匿名函数
    val testLamda: (Int, Int) -> Int = { x, y -> x + y }
    println(testLamda(5, 6))

    //交换两个数
    var a = 3
    var b = 4
    a = b.also { b = a }
    println("a: $a, b: $b")

    val square = Square(len = 4.0)
    square.area()

    val rect = Rectangle(width = 3, length = 4)
    rect.area()
    rect.test()

    val c = C()
    c.f()

    val user = User("Hem1ng")
    user.getName()
    user.foo()
    shapeTest(rect)

    val list = listOf(1, 2, 3, 4)
//    list.lastIndex = 4 //错误：扩展属性不能有初始化器
    println(list.lastIndex)

    c.caller(D())

    val t = null
    println(t.toString())

    val e = E()
    val f = F()
    f.caller(e)
    val e1 = E1()
    val f1 = F1()
    f1.caller(e)
    f.caller(e1)

    MyClass.foo()
    println(MyClass.no)

    val jack = Person(name = "Jack", age = 18)
    val oldJack = jack.copy(age = 20)
    println(jack)
    println(oldJack)

    //组件函数允许数据类在解构声明中使用
    var (name, age) = jack
    println("name: $name, age: $age")

    //with: 可以直接访问实例的属性为不需要显示声明
    with(jack) {
        println("The person name: $name, age: $age")
    }
    println("The person name: $jack.name, age: $jack.age")

    Box(1)
    Box<Int>(2)
    Box("3")
    boxIn(4)
    boxIn("5")

    val obj = Site
    val obj2 = Site
    obj.url = "https://www.baidu.com"
    println(obj.url)
    println(obj2.url)

    val baseImp = BaseImp("Cat")
    Cat(baseImp).run()

    getNullableLength(null)
    getNullableLength("a")
    getNullableLength("c")

    val jake1 = Person("jake", 18)
    val desc = Person2().apply {
        name = "jake plus"
        age = 20
    }
    println("===== ${desc.name}")
    jake1.also { println("jake is created") }

    val m = Model().apply { name = "1" }
    println(m.name)

    val dice = Dice(6)
    println(dice.roll())

    println("The float value is %.2f".format(3.1415926))

    val entrees = mutableListOf<String>()
    entrees.add("noodles")
    entrees.addAll(listOf("apple", "peach", "lemon"))
    entrees.remove("lemon")
    entrees.removeAt(0)
    entrees.clear()
    println("Entrees is empty? ${entrees.isEmpty()}")

    val words = listOf(
        "about",
        "acute",
        "awesome",
        "balloon",
        "best",
        "brief",
        "class",
        "coffee",
        "creative"
    )
    // shuffled: 随机打乱集合中的项并创建新的集合
    // take: 获取集合中的前 N 个项
    val bWords = words.filter { it.startsWith("b", ignoreCase = true) }
        .shuffled()
        .take(2)
        .sorted()
    println(bWords)

    val ls = listOf(
        listOf("about", "acute", "awesome"),
        listOf("balloon", "best", "brief"),
        listOf("class", "coffee", "creative")
    )
    println(ls.flatten())

    val states = arrayOf("Starting", "Doing Task 1", "Doing Task 2", "Ending")
//    testThread(states)
    testLaunch(states)
//    testRunBlocking(states)
//    testAsync(states)
}

fun testThread(states: Array<String>) {
    repeat(3) {
        //单线程执行，不会阻塞当前线程
        Thread {
            println("${Thread.currentThread()} has started")
            for (i in states) {
                println("${Thread.currentThread()} - $i")
                sleep(50)
            }
        }.start()
    }
}

fun testLaunch(states: Array<String>) {
    repeat(3) {
        GlobalScope.launch {
            println("${Thread.currentThread()} has started")
            for (i in states) {
                println("${Thread.currentThread()} - $i")
                delay(5000)
            }
        }
    }
}

fun testRunBlocking(states: Array<String>) {
    repeat(3) {
        //runBlocking 会启动新协程并在新协程完成之前阻塞当前线程
        runBlocking {
            println("${Thread.currentThread()} has started")
            for (i in states) {
                println("${Thread.currentThread()} - $i")
                sleep(50)
            }
        }
    }
}

fun testAsync(states: Array<String>) {
    repeat(3) {
        GlobalScope.async {
            println("${Thread.currentThread()} has started")
            for (i in states) {
                println("${Thread.currentThread()} - $i")
            }
        }.invokeOnCompletion { sleep(50) }
    }
}

class Model {
    var name = ""
}

class Dice(private val numSides: Int) {

    fun roll(): Int = (1..numSides).random()
}

