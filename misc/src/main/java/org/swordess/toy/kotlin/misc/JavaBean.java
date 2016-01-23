package org.swordess.toy.kotlin.misc;

public class JavaBean {

    /* readable / writable fields */

    // name: get, set
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    // hasName: isHas, setHas
    private boolean hasName;
    public boolean isHasName() {
        return hasName;
    }
    public void setHasName(boolean hasName) {
        this.hasName = hasName;
    }


    // hasAge: getHas, setHas
    private boolean hasAge;
    public boolean getHasAge() {
        return hasAge;
    }
    public void setHasAge(boolean hasAge) {
        this.hasAge = hasAge;
    }


    // isValid: isIs, setIs
    private boolean isValid;
    public boolean isIsValid() {
        return isValid;
    }
    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }


    // active: get, set
    private boolean active;
    public boolean getActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }


    // used: is, set
    private boolean used;
    public boolean isUsed() {
        return used;
    }
    public void setUsed(boolean used) {
        this.used = used;
    }


    /* readable fields */

    // age: get
    private int age = 90;
    public int getAge() {
        return age;
    }


    // hasPressure: getHasPressure
    private boolean hasPressure = true;
    public boolean getHasPressure() {
        return hasPressure;
    }


    // old: is
    private boolean isOld = true;
    public boolean isOld() {
        return isOld;
    }


    // fantasy: is
    private boolean fantasy = true;
    public boolean isFantasy() {
        return fantasy;
    }

}
