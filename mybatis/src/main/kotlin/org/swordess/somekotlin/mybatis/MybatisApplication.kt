package org.swordess.somekotlin.mybatis

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageRowBounds
import org.apache.ibatis.session.RowBounds
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.swordess.somekotlin.mybatis.mapper.GradeDefMapper
import org.swordess.somekotlin.mybatis.model.GradeDef

@SpringBootApplication
open class MybatisApplication(private val gradeDefMapper: GradeDefMapper) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(MybatisApplication::class.java)

    override fun run(vararg args: String?) {
        // println(gradeDefMapper.getById(1))
        // hardCodePagination()
        generalPagination()
    }

    private fun hardCodePagination() {
        val firstPageWithoutCount = gradeDefMapper.findPage(RowBounds(0, 10))
        println(firstPageWithoutCount)

        val firstPageWithCount = gradeDefMapper.findPage2(PageRowBounds(0, 10))
        println(firstPageWithCount)
    }

    private fun generalPagination() {
        log.info(">>> allRows")
        val allRows = gradeDefMapper.find()
        println(allRows)

        log.info(">>> firstPageWithoutCount")
        val firstPageWithoutCount = PageHelper
            .startPage<GradeDef>(1, 10, false)
            .doSelectPage<GradeDef> { gradeDefMapper.find() }
        println(firstPageWithoutCount)

        log.info(">>> firstPageWithCount")
        val firstPageWithCount = PageHelper.startPage<GradeDef>(1, 10, true).doSelectPage<GradeDef> {
            gradeDefMapper.find()
        }
        println(firstPageWithCount)
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(MybatisApplication::class.java, *args)
        }

    }

}