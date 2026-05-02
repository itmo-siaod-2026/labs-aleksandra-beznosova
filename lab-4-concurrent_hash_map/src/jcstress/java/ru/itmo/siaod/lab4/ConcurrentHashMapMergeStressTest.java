package ru.itmo.siaod.lab4;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.I_Result;

@JCStressTest
//Если будет 1 то ошибка lost update
@Outcome(id = "2", expect = Expect.ACCEPTABLE, desc = "Both merge operations were applied.")
@State
public class ConcurrentHashMapMergeStressTest {
    private final ConcurrentHashMap map = new ConcurrentHashMap();

    public ConcurrentHashMapMergeStressTest() {
        map.put(new ConcurrentHashMap.NodeKV("counter", "0", null));
    }

    //каждый поток хочет сделать counter=counter+1
    @Actor
    public void actor1() {
        map.merge("counter", "1", (oldValue, newValue) ->
                String.valueOf(Integer.parseInt(oldValue) + Integer.parseInt(newValue))
        );
    }

    @Actor
    public void actor2() {
        map.merge("counter", "1", (oldValue, newValue) ->
                String.valueOf(Integer.parseInt(oldValue) + Integer.parseInt(newValue))
        );
    }

    @Arbiter
    public void arbiter(I_Result result) {
        String value = map.get("counter");
        result.r1 = value == null ? -1 : Integer.parseInt(value);
    }
}