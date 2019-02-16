package org.swordess.somekotlin.mybatis.model

class GradeDef {

    var id: Long? = null
    var area: String? = null
    var gradeValue: Int? = null
    var gradeName: String? = null
    var group: Int? = null

    override fun toString(): String {
        return "GradeDef(id=$id, area=$area, gradeValue=$gradeValue, gradeName=$gradeName, group=$group)"
    }

}
