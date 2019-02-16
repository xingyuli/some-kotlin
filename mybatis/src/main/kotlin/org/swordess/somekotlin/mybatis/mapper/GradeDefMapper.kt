package org.swordess.somekotlin.mybatis.mapper

import com.github.pagehelper.PageRowBounds
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.session.RowBounds
import org.swordess.somekotlin.mybatis.model.GradeDef

@Mapper
interface GradeDefMapper {

    @Select("SELECT * FROM grade_def WHERE id = #{id}")
    fun getById(@Param("id") id: Long): GradeDef

    // hard code pagination

    @Select("SELECT * FROM grade_def")
    fun findPage(rowBounds: RowBounds): List<GradeDef>

    @Select("SELECT * FROM grade_def")
    fun findPage2(pageRowBounds: PageRowBounds): List<GradeDef>

    // more general and powerful

    @Select("SELECT * FROM grade_def")
    fun find(): List<GradeDef>

}
