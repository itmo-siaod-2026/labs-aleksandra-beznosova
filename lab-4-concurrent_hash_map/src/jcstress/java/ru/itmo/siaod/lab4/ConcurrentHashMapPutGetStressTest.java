/*Одновременный put/get

 */
package ru.itmo.siaod.lab4;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.L_Result;

@JCStressTest
@Outcome(id = "null", expect = Expect.ACCEPTABLE, desc = "get happened before put became visible.")
@Outcome(id = "22", expect = Expect.ACCEPTABLE, desc = "get observed the value inserted by put.")
@State
public class ConcurrentHashMapPutGetStressTest {
    private final ConcurrentHashMap map = new ConcurrentHashMap();

    @Actor
    public void actor1() {
        map.put(new ConcurrentHashMap.NodeKV("cat", "22", null));
    }

    @Actor
    public void actor2(L_Result result) {
        result.r1 = map.get("cat");
    }
}