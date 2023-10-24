package com.easyjob.support.http;

import com.easyjob.util.Utils;
import org.springframework.beans.factory.BeanFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class JobViewServlet extends HttpServlet {


    private BeanFactory beanFactory;

    private JobJsonResourceHandler jsonResourceHandler;


    private static final String RESOURCE_ROOT_PATH = "support/http/job";

    public JobViewServlet(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        jsonResourceHandler = new JobJsonResourceHandler(beanFactory);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //beanFactory.get
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        response.setCharacterEncoding("utf-8");

        if (contextPath == null) { // root context
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String fileName = requestURI.substring(contextPath.length() + servletPath.length());

        if(fileName.equals("")) {
            response.sendRedirect("com/easyjob/index.html");
        }
        if(fileName.equals("/")) {
            response.sendRedirect("index.html");
        }

        //处理json
        if(fileName.contains(".json")) {
            //
            try {
                jsonResourceHandler.handle( fileName, request, response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        String filePath = RESOURCE_ROOT_PATH + fileName;
        if (filePath.endsWith(".html")) {
            response.setContentType("text/html; charset=utf-8");
        }
        if (fileName.endsWith(".jpg")) {
            byte[] bytes = Utils.readByteArrayFromResource(filePath);
            if (bytes != null) {
                response.getOutputStream().write(bytes);
            }

            return;
        }

        String text = Utils.readFromResource(filePath);
        if (text == null) {
            return;
        }
        if (fileName.endsWith(".css")) {
            response.setContentType("text/css;charset=utf-8");
        } else if (fileName.endsWith(".js")) {
            response.setContentType("text/javascript;charset=utf-8");
        }
        response.getWriter().write(text);

    }
}
