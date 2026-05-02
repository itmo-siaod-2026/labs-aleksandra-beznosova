/* Домашнее задание 4: concurrent hash-map

Требуется реализовать thread-safe хеш-таблицу с закрытой адресацией.

Требования к структуре:
🔤минимально необходимый набор операций: put(key: K, value: V), get(key: K) -> V, size() -> usize, clear(), merge(key: K, value: V, merger: Fn(V, V) -> V) -> V, итератор по парам ключ-значения
🔤(почти) никогда не блокирующие операции чтения
🔤однозначный наблюдаемый порядок между завершёнными операциями
За более формальной спецификацией см. Javadoc к ConcurrentHashMap (https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html) в JDK.
При этом не требуется реализовывать конкретный интерфейс для таблицы из выбранного ЯП, это просто референс.

В работе также требуется:
🔤Написать бенчмарки (сравнивать перф уместно с не-thread-safe версией)
🔤Написать concurrency-тесты с использованием специализированного инструментария (например, если Java, jcstress)
🔤Нарисовать графики по числовым результатам
🔤Объяснить интересные результаты в отчётё
 */
package ru.itmo.siaod.lab4

fun main() {
    val emptyMap = ConcurrentHashMap()

    println("=== Empty map iterator ===")
    val emptyIterator = emptyMap.iterator()
    println("hasNext = ${emptyIterator.hasNext()}") // false

    val map = ConcurrentHashMap()

    map.put(ConcurrentHashMap.NodeKV("cat", "22"))
    map.put(ConcurrentHashMap.NodeKV("dog", "33"))
    map.put(ConcurrentHashMap.NodeKV("fox", "55"))

    // update existing key: iterator должен показать fox = 91, а не fox = 55
    map.put(ConcurrentHashMap.NodeKV("fox", "91"))

    // collision case: "Aa" и "BB" имеют одинаковый hashCode в JVM
    map.put(ConcurrentHashMap.NodeKV("Aa", "first"))
    map.put(ConcurrentHashMap.NodeKV("BB", "second"))

    println()
    println("=== Filled map iterator ===")
    println("size = ${map.size()}") // 5

    val iterator = map.iterator()
    while (iterator.hasNext()) {
        val entry = iterator.next()
        println("${entry.key} = ${entry.value}")
    }

    println()
    println("=== Check updated value ===")
    println("fox = ${map.get("fox")}") // 91

    println()
    println("=== After clear iterator ===")
    map.clear()
    println("size = ${map.size()}") // 0

    val afterClearIterator = map.iterator()
    println("hasNext = ${afterClearIterator.hasNext()}") // false
}

