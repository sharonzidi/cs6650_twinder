package edu.northeastern;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/hello")
@Log4j2
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        responseHandler(response, HttpServletResponse.SC_OK, "It works!");
    }

    private void responseHandler(final HttpServletResponse response,
                                 final int statusCode,
                                 final String outputBody) throws IOException {
        response.setStatus(statusCode);
        response.getOutputStream().print(outputBody);
        response.getOutputStream().flush();
    }
}
