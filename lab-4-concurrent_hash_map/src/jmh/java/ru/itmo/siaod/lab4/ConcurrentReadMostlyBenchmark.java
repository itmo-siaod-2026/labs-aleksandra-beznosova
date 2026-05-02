package ru.itmo.siaod.lab4;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 7, time = 1)
@Measurement(iterations = 15, time = 1)
@Fork(3)
@State(Scope.Benchmark)
public class ConcurrentReadMostlyBenchmark {
    private static final int OPERATIONS_PER_INVOCATION = 10;

    @Param({ "16384"})
    public int keyCount;

    @Param({"16384"})
    public int capacity;

    private ConcurrentHashMap map;
    private String[] keys;
    private String[] values;
    private AtomicInteger cursor;

    @Setup(Level.Iteration)
    public void setup() {
        map = new ConcurrentHashMap(capacity);
        keys = new String[keyCount];
        values = new String[keyCount];

        for (int i = 0; i < keyCount; i++) {
            keys[i] = "key-" + i;
            values[i] = "value-" + i;
            map.put(new ConcurrentHashMap.NodeKV(keys[i], values[i], null));
        }

        cursor = new AtomicInteger(0);
    }

    private int index(int value) {
        return value & (keyCount - 1);
    }

    @Benchmark
    @Threads(8)
    public String concurrentGetShared() {
        int current = cursor.getAndIncrement();
        return map.get(keys[index(current)]);
    }

//    @Benchmark
//    @Threads(8)
//    @OperationsPerInvocation(OPERATIONS_PER_INVOCATION)
//    public void readMostlyWorkload(Blackhole blackhole) {
//        int start = cursor.getAndAdd(OPERATIONS_PER_INVOCATION);
//
//        for (int i = 0; i < 9; i++) {
//            int index = index(start + i);
//            blackhole.consume(map.get(keys[index]));
//        }
//
//        int writeIndex = index(start + 9);
//        map.put(new ConcurrentHashMap.NodeKV(keys[writeIndex], values[writeIndex], null));
//    }
}