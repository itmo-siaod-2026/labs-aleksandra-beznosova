package ru.itmo.siaod.lab4

class SimpleHashMap @JvmOverloads constructor(
    private val capacity: Int = 16
) {
    data class NodeKV(
        val key: String,
        var value: String,
        var link: NodeKV? = null
    )

    private val buckets = arrayOfNulls<NodeKV>(capacity)
    private var tableSize = 0

    fun size(): Int {
        return tableSize
    }

    private fun hash(key: String): Int {
        return (key.hashCode() and Int.MAX_VALUE) % capacity
    }

    fun put(key: String, value: String) {
        val hashIndex = hash(key)

        var current = buckets[hashIndex]

        while (current != null) {
            if (current.key == key) {
                current.value = value
                return
            }

            current = current.link
        }

        val inserted = NodeKV(key, value, buckets[hashIndex])
        buckets[hashIndex] = inserted
        tableSize++
    }

    fun get(key: String): String? {
        val hashIndex = hash(key)

        var current = buckets[hashIndex]

        while (current != null) {
            if (current.key == key) {
                return current.value
            }

            current = current.link
        }

        return null
    }

    fun clear() {
        for (i in 0 until capacity) {
            buckets[i] = null
        }

        tableSize = 0
    }

    fun merge(key: String, value: String, merger: (String, String) -> String): String {
        val hashIndex = hash(key)

        var current = buckets[hashIndex]

        while (current != null) {
            if (current.key == key) {
                val mergedValue = merger(current.value, value)
                current.value = mergedValue
                return mergedValue
            }

            current = current.link
        }

        val inserted = NodeKV(key, value, buckets[hashIndex])
        buckets[hashIndex] = inserted
        tableSize++

        return value
    }
}