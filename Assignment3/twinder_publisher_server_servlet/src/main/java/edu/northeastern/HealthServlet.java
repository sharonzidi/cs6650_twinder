package edu.northeastern;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static edu.northeastern.utils.ServletHelper.responseHandler;

@WebServlet(name = "HealthServlet", value = "/HealthServlet")
@Log4j2
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        responseHandler(response, HttpServletResponse.SC_OK, "It works!");
    }
}
