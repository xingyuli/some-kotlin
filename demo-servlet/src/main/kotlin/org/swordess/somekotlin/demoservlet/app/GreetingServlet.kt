package org.swordess.somekotlin.demoservlet.app

import org.springframework.web.context.support.WebApplicationContextUtils
import org.swordess.somekotlin.demoservlet.common.Config
import org.swordess.somekotlin.demoservlet.common.greeting
import javax.servlet.ServletConfig
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class GreetingServlet : HttpServlet() {

    private lateinit var config: Config

    override fun init(servletConfig: ServletConfig?) {
        val ctx = WebApplicationContextUtils.getWebApplicationContext(servletConfig?.servletContext)
        config = ctx.getBean("config") as Config
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        println("[DEBUG] Config(host=${config.host}, port=${config.port})")
        resp!!.writer.write(greeting())
    }

}