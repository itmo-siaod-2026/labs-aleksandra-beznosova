package ru.itmo.siaod.lab4;

import kotlin.jvm.functions.Function2;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 7, time = 1)
@Measurement(iterations = 15, time = 1)
@Fork(3)
@State(Scope.Benchmark)
/*distinctKeys = 1     максимальный contention
distinctKeys = 16    средний contention
distinctKeys = 1024  низкий contention
 */
public class ConcurrentContentionBenchmark {
    @Param({"1024"})
    public int distinctKeys;

    @Param({"16384"})
    public int capacity;

    private ConcurrentHashMap map;
    private String[] keys;
    private String[] values;
    private AtomicInteger cursor;

    private static final Function2<String, String, String> TAKE_NEW_VALUE =
            (oldValue, newValue) -> newValue;

    @Setup(Level.Iteration)
    public void setup() {
        map = new ConcurrentHashMap(capacity);
        keys = new String[distinctKeys];
        values = new String[distinctKeys];

        for (int i = 0; i < distinctKeys; i++) {
            keys[i] = "key-" + i;
            values[i] = "value-" + i;
            map.put(new ConcurrentHashMap.NodeKV(keys[i], values[i], null));
        }

        cursor = new AtomicInteger(0);
    }

    private int nextIndex() {
        return Math.floorMod(cursor.getAndIncrement(), distinctKeys);
    }

//    @Benchmark
//    @Threads(8)
//    public void concurrentPutWithContention() {
//        int index = nextIndex();
//        map.put(new ConcurrentHashMap.NodeKV(keys[index], values[index], null));
//    }

    @Benchmark
    @Threads(8)
    public String concurrentMergeWithContention() {
        int index = nextIndex();
        return map.merge(keys[index], values[index], TAKE_NEW_VALUE);
    }
}