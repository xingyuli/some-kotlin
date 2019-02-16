package org.swordess.somekotlin.mybatis.mapper

import com.github.pagehelper.PageRowBounds
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.jdbc.SQL
import org.apache.ibatis.session.RowBounds
import org.swordess.somekotlin.mybatis.model.GradeDef

@Mapper
interface GradeDefMapper {

    @Select("SELECT * FROM grade_def WHERE id = #{id}")
    fun getById(@Param("id") id: Long): GradeDef


    /*
     Ungraceful version

     1. <script> element
     enables dynamic SQL parsing and execution for the annotation

     2. collection attribute's value
     for List<Long>, available parameters are [collection, list]
     for Set<Long>, available parameters are [collection]
     */
    @Select(
        """<script>
        SELECT * FROM grade_def WHERE id IN
          <foreach item="item" collection="collection" open="(" separator="," close=")">
          #{item}
          </foreach>
        </script>"""
    )
    fun getByIds(ids: Set<Long>): List<GradeDef>

    /*
     Graceful version
     */
    @SelectProvider(type = GradeDefSqlBuilder::class, method = "buildGetByIds")
    fun getByIds2(ids: Set<Long>): List<GradeDef>


    // hard code pagination

    @Select("SELECT * FROM grade_def")
    fun findPage(rowBounds: RowBounds): List<GradeDef>

    @Select("SELECT * FROM grade_def")
    fun findPage2(pageRowBounds: PageRowBounds): List<GradeDef>

    // more general and powerful

    @Select("SELECT * FROM grade_def")
    fun find(): List<GradeDef>

}

class GradeDefSqlBuilder {

    companion object {

//        @JvmStatic
//        fun buildGetByIds(collection: Set<Long>): String {
//            return (object : SQL() {
//                init {
//                    SELECT("*")
//                    FROM("grade_def")
//                    WHERE("id IN ${collection.joinToString(prefix = "(", separator = ",", postfix = ")")}")
//                }
//            }).toString()
//        }

        // simplified version
        @JvmStatic
        fun buildGetByIds(collection: Set<Long>): String = SQL()
            .SELECT("*")
            .FROM("grade_def")
            .WHERE("id IN ${collection.joinToString(prefix = "(", separator = ",", postfix = ")")}")
            .toString()
    }

}
