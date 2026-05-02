package ru.itmo.siaod.lab4;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.I_Result;

@JCStressTest
@Outcome(id = "2", expect = Expect.ACCEPTABLE, desc = "Both put operations were applied.")
@State
public class ConcurrentHashMapPutStressTest {
    private final ConcurrentHashMap map = new ConcurrentHashMap();

    @Actor
    public void actor1() {
        map.put(new ConcurrentHashMap.NodeKV("cat", "22", null));
    }

    @Actor
    public void actor2() {
        map.put(new ConcurrentHashMap.NodeKV("dog", "33", null));
    }

    @Arbiter
    public void arbiter(I_Result result) {
        result.r1 = map.size();
    }
}