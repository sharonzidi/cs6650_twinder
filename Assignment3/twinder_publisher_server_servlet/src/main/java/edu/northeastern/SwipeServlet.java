package edu.northeastern;

import com.google.gson.Gson;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import edu.northeastern.models.StatusResponse;
import edu.northeastern.models.SwipeDetailsMessage;
import edu.northeastern.models.SwipeDetailsRequest;
import edu.northeastern.utils.RMQConnection;
import edu.northeastern.utils.ServletHelper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static edu.northeastern.utils.Constants.RMQ_EXCHANGE_NAME;
import static edu.northeastern.utils.Constants.RMQ_QUEUE_MATCHES;
import static edu.northeastern.utils.Constants.RMQ_QUEUE_STATS;
import static edu.northeastern.utils.ServletHelper.responseHandler;

@WebServlet(name = "SwipeServlet", value = "/SwipeServlet")
@Slf4j
public class SwipeServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Handling SwipeServlet post request...");

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

        final String leftOrRight = urlParts[1];

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

        final SwipeDetailsMessage swipeDetailsMessage = SwipeDetailsMessage.builder()
                .swipee(swipeDetailsRequest.getSwipee())
                .swiper(swipeDetailsRequest.getSwiper())
                .comment(swipeDetailsRequest.getComment())
                .leftOrRight(leftOrRight)
                .build();

        final StatusResponse<SwipeDetailsRequest> statusResponse = StatusResponse.<SwipeDetailsRequest>builder()
                .data(swipeDetailsRequest)
                .build();

        // add to RMQ producer
        try {
            final String swipeMessage = gson.toJson(swipeDetailsMessage);
            log.info("Sending message to RMQ: " + swipeMessage);
            sendToRabbitMQ(swipeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseHandler(response, HttpServletResponse.SC_OK, gson.toJson(statusResponse.getData()));
    }

    private void sendToRabbitMQ(String jsonPayload) throws IOException, TimeoutException {
        final Connection connection = RMQConnection.getConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(RMQ_EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true);

        channel.queueDeclare(RMQ_QUEUE_STATS, false, false, false, null);
        channel.queueBind(RMQ_QUEUE_STATS, RMQ_EXCHANGE_NAME, "");

        channel.queueDeclare(RMQ_QUEUE_MATCHES, false, false, false, null);
        channel.queueBind(RMQ_QUEUE_MATCHES, RMQ_EXCHANGE_NAME, "");

        channel.basicPublish(RMQ_EXCHANGE_NAME, "", null, jsonPayload.getBytes(StandardCharsets.UTF_8));

        channel.close();
    }

    private boolean isUrlValid(String[] parts) {
        final String leftOrRight = parts[1];
        return leftOrRight.equals("left") || leftOrRight.equals("right");
    }
}
