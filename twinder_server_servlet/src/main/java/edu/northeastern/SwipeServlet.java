package edu.northeastern;

import com.google.gson.Gson;
import edu.northeastern.models.StatusResponse;
import edu.northeastern.models.SwipeDetailsRequest;
import edu.northeastern.utils.ServletHelper;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet(name = "SwipeServlet", value = "/SwipeServlet")
@Log4j2
public class SwipeServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        // check we have a URL!
        final String urlPath = request.getPathInfo();
        if (urlPath == null || urlPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("missing parameters");
            return;
        }

        String[] urlParts = urlPath.split("/");
        if (!isUrlValid(urlParts)) {
            responseHandler(response, HttpServletResponse.SC_NOT_FOUND, "Required path parameter is bad.");
        } else {
            responseHandler(response, HttpServletResponse.SC_OK, "It works!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Handling SwipeServlet post request...");
        response.setContentType("application/json");

        // validate path parameter existence
        final String urlPath = request.getPathInfo();
        if (urlPath == null || urlPath.isEmpty()) {
            responseHandler(response, HttpServletResponse.SC_NOT_FOUND, "Required path parameter is missing.");
            return;
        }

        // validate path parameter
        final String[] urlParts = urlPath.split("/");
        if (!isUrlValid(urlParts)) {
            responseHandler(response, HttpServletResponse.SC_NOT_FOUND, "Required path parameter is bad.");
            return;
        }

        // validate string payload parsing
        final String payload = ServletHelper.parsePayloadString(request);

        if (payload == null) {
            responseHandler(response, HttpServletResponse.SC_BAD_REQUEST, "Required payload format is bad.");
            return;
        }

        // validate SwipeDetails payload body object
        final SwipeDetailsRequest swipeDetailsRequest = gson.fromJson(payload, SwipeDetailsRequest.class);

        if (Objects.isNull(swipeDetailsRequest)
                || Objects.isNull(swipeDetailsRequest.getSwiper())
                || Objects.isNull(swipeDetailsRequest.getSwipee())
                || Objects.isNull(swipeDetailsRequest.getComment())) {
            responseHandler(response, HttpServletResponse.SC_BAD_REQUEST, "Required payload data is bad.");
            return;
        }

        final StatusResponse<SwipeDetailsRequest> statusResponse = StatusResponse.<SwipeDetailsRequest>builder()
                .data(swipeDetailsRequest)
                .build();

        responseHandler(response, HttpServletResponse.SC_OK, gson.toJson(statusResponse.getData()));
    }

    private boolean isUrlValid(String[] parts) {
        final String leftOrRight = parts[1];
        return leftOrRight.equals("left") || leftOrRight.equals("right");
    }

    private void responseHandler(final HttpServletResponse response,
                                 final int statusCode,
                                 final String outputBody) throws IOException {
        response.setStatus(statusCode);
        response.getOutputStream().print(outputBody);
        response.getOutputStream().flush();
    }
}
