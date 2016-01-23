package org.swordess.toy.kotlin.misc;

import org.junit.Assert;
import org.junit.Test;

public class UseSingletonInJavaTest {

    @Test
    public void testCompanionSingleton() {
        CompanionSingleton first = CompanionSingleton.Companion.getInstance();
        CompanionSingleton second = CompanionSingleton.Companion.getInstance();
        Assert.assertSame(first, second);
    }

    @Test
    public void testStandardSingleton() {
        StandardSingleton first = StandardSingleton.getInstance();
        StandardSingleton second = StandardSingleton.getInstance();
        Assert.assertSame(first, second);
    }

}
