package org.swordess.toy.kotlin.misc;

import org.junit.Test;

public class KotlinBeanAccessorTest {

    private KotlinBean bean = new KotlinBean();

    @Test
    public void test() {
        // property 'name'
        bean.setName("name");
        System.out.println(bean.getName());

        // property 'hasName'
        bean.setHasName(true);
        System.out.println(bean.getHasName());

        // property 'isValid'
        bean.setValid(true);
        System.out.println(bean.isValid());

        // property 'active'
        bean.setActive(true);
        System.out.println(bean.getActive());

        System.out.println(bean.getAge());
        System.out.println(bean.getHasPressure());
        System.out.println(bean.isOld());
        System.out.println(bean.getFantasy());
    }

}
