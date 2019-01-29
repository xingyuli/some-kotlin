package org.swordess.somekotlin.misc.kvsj;

import org.junit.Test;
import org.swordess.somekotlin.misc.kvsj.UseKotlinAnyKt;

public class UseKotlinAnyTest {

    @Test
    public void test() {
        // Any -> Object
        UseKotlinAnyKt.doSomething(new Object());
    }

}
