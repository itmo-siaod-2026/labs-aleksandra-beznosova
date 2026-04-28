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
    val map = ConcurrentHashMap()

    map.put(ConcurrentHashMap.NodeKV("cat", "22"))
    map.put(ConcurrentHashMap.NodeKV("dog", "33"))
    map.put(ConcurrentHashMap.NodeKV("fox", "55"))
    map.put(ConcurrentHashMap.NodeKV("moose", "67"))
    map.put(ConcurrentHashMap.NodeKV("bear", "81"))

    println("size after insert = ${map.size()}")

    map.put(ConcurrentHashMap.NodeKV("fox", "91"))
    map.put(ConcurrentHashMap.NodeKV("cat", "100"))

    println("size after updates = ${map.size()}")
    println(map.get("fox"))
}
