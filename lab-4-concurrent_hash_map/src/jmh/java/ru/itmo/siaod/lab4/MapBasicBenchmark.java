package ru.itmo.siaod.lab4;

import kotlin.jvm.functions.Function2;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 7, time = 1)
@Measurement(iterations = 15, time = 1)
@Fork(3)
@State(Scope.Thread)
/*concurrentGet vs simpleGet
concurrentPutUpdate vs simplePutUpdate
concurrentMergeUpdate vs simpleMergeUpdate
 */
public class MapBasicBenchmark {
    @Param({"1024", "16384"})
    public int keyCount;

    @Param({"16384"})
    public int capacity;

    private ConcurrentHashMap concurrentMap;
    private SimpleHashMap simpleMap;

    private String[] keys;
    private String[] values;
    private int cursor;

    private static final Function2<String, String, String> TAKE_NEW_VALUE =
            (oldValue, newValue) -> newValue;

    @Setup(Level.Iteration)
    public void setup() {
        concurrentMap = new ConcurrentHashMap(capacity);
        simpleMap = new SimpleHashMap(capacity);

        keys = new String[keyCount];
        values = new String[keyCount];

        for (int i = 0; i < keyCount; i++) {
            keys[i] = "key-" + i;
            values[i] = "value-" + i;

            concurrentMap.put(new ConcurrentHashMap.NodeKV(keys[i], values[i], null));
            simpleMap.put(keys[i], values[i]);
        }

        cursor = 0;
    }

    private int nextIndex() {
        return cursor++ & (keyCount - 1);
    }

//    @Benchmark
//    public String concurrentGet() {
//        int index = nextIndex();
//        return concurrentMap.get(keys[index]);
//    }
//
//    @Benchmark
//    public String simpleGet() {
//        int index = nextIndex();
//        return simpleMap.get(keys[index]);
//    }
//
//    @Benchmark
//    public void concurrentPutUpdate() {
//        int index = nextIndex();
//        concurrentMap.put(new ConcurrentHashMap.NodeKV(keys[index], values[index], null));
//    }

    @Benchmark
    public void simplePutUpdate() {
        int index = nextIndex();
        simpleMap.put(keys[index], values[index]);
    }

//    @Benchmark
//    public String concurrentMergeUpdate() {
//        int index = nextIndex();
//        return concurrentMap.merge(keys[index], values[index], TAKE_NEW_VALUE);
//    }

    @Benchmark
    public String simpleMergeUpdate() {
        int index = nextIndex();
        return simpleMap.merge(keys[index], values[index], TAKE_NEW_VALUE);
    }
}