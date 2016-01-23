package org.swordess.toy.kotlin.misc

import org.junit.Test

class DigOutBackingField(
        val name: String, var desc: String,
        private val age: Int, private var level: Int
) {
    constructor(extra: String) : this("", "", 0, 0)
}

/*
javap -private org.swordess.toy.kotlin.misc.DigOutBackingField
Compiled from "BackingFieldTest.kt"
public final class org.swordess.toy.kotlin.misc.DigOutBackingField {
  private final java.lang.String name;
  private java.lang.String desc;
  private final int age;
  private int level;
  public final java.lang.String getName();
  public final java.lang.String getDesc();
  public final void setDesc(java.lang.String);
  public org.swordess.toy.kotlin.misc.DigOutBackingField(java.lang.String, java.lang.String, int, int);
  public org.swordess.toy.kotlin.misc.DigOutBackingField(java.lang.String);
}
*/

class BackingFieldTest {

    private val bean = DigOutBackingField("name value", "desc value", 20, 10)

    @Test
    fun test() {
        bean.name
        bean.desc
        // private age, level is not visible
    }

}