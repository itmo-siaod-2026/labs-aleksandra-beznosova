package ru.itmo.siaod.lab4

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReferenceArray

class ConcurrentHashMap() {
    data class NodeKV(val key: String, var value: String, var link: NodeKV? = null)

    val capacity = 16
    val buckets = AtomicReferenceArray<NodeKV?>(capacity)
    val tableSizeCounter = AtomicInteger(0)
    fun size(): Int {
        return tableSizeCounter.get()
    }
    private fun hash(key: String): Int {
        return key.hashCode() % capacity
    }

    private fun findLast(bucket: NodeKV): NodeKV {
        return bucket
    }

    fun put(pair: NodeKV) {
        // ключа нет
        val hashIndex = hash(pair.key)
        while (true) {
            // Сохраняем состояние для CAS
            val oldHead: NodeKV? = buckets[hashIndex]
            // Текущее берем из сохраненного состояния
            var current: NodeKV? = oldHead

            var newHead: NodeKV? = null
            var newTail: NodeKV? = null
            var found = false
            while (current != null) {
                var copy: NodeKV
                if (newHead == null) { // first step - no old links
                    if (current.key == pair.key) {
                        copy = NodeKV(current.key, pair.value, null)
                        found = true
                    } else {
                        copy = NodeKV(current.key, current.value, null)
                    }
                    newHead = copy
                    newTail = copy
                } else {
                    if (current.key == pair.key) {
                        copy = NodeKV(current.key, pair.value, null)
                        found = true
                    } else {
                        copy = NodeKV(current.key, current.value, null)
                    }
                    newTail!!.link = copy
                    newTail = copy
                }
                current = current.link
            }
            // CAS - Замени buckets[hashIndex] на newHead только если он всё ещё равен
            // oldHead.
            if (found) {
                if (buckets.compareAndSet(hashIndex, oldHead, newHead)) {
                    break
                }
            } else {
                val inserted = NodeKV(pair.key, pair.value, oldHead)
                if (buckets.compareAndSet(hashIndex, oldHead, inserted)) {
                    tableSizeCounter.incrementAndGet()
                    break
                }
            }
        }
    }

    fun get(key: String): String? {
        val hashIndex = hash(key)

        var bucket = buckets[hashIndex]
        while (bucket != null) {
            if (bucket.key == key) {
                return bucket.value
            }
            bucket = bucket.link
        }
        return null
    }

    fun clear() {
        for (i in 0 until capacity) {

            while (true) {
                var oldHead = buckets[i]
                if (oldHead == null) {
                    break
                }
                if (buckets.compareAndSet(i, oldHead, null)) {
                    break
                }
            }
        }
        tableSizeCounter.set(0)
    }

    fun merge(key: String, value: String, merger: (String, String) -> String): String {
        val hashIndex = hash(key)

        while (true) {
            val oldHead = buckets[hashIndex]
            var current = oldHead

            var newHead: NodeKV? = null
            var newTail: NodeKV? = null
            var found = false

            var result: String = value
            while (current != null) {

                val copy: NodeKV
                val oldValue = current.value
                if (newHead == null) {
                    if (current.key == key) {
                        var mergedValue = merger(oldValue, value)
                        result = mergedValue
                        copy = NodeKV(current.key, mergedValue, null)
                        found = true
                    } else {
                        copy = NodeKV(current.key, oldValue, null)
                    }
                    newHead = copy
                    newTail = copy
                } else {
                    if (current.key == key) {
                        var mergedValue = merger(oldValue, value)
                        result = mergedValue
                        copy = NodeKV(current.key, mergedValue, null)
                        found = true
                    } else {
                        copy = NodeKV(current.key, oldValue, null)
                    }
                    newTail!!.link = copy
                    newTail = copy
                }
                current = current.link
            }
            if (found) {
                if (buckets.compareAndSet(hashIndex, oldHead, newHead)) {
                    return result
                }
            } else {
                val inserted = NodeKV(key, value, oldHead)
                if (buckets.compareAndSet(hashIndex, oldHead, inserted)) {
                    tableSizeCounter.incrementAndGet()
                    return result
                }
            }
        }
    }
}
