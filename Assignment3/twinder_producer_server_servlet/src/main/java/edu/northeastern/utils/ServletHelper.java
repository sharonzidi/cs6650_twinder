package edu.northeastern.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class ServletHelper {
    public static String parsePayloadString(final HttpServletRequest request) {
        try {
            final StringBuilder sb = new StringBuilder();
            String s;
            while ((s = request.getReader().readLine()) != null) {
                sb.append(s);
            }
            return sb.toString();
        } catch (Exception exception) {
            log.warn("Unable to handle payload parsing: " + exception.getMessage());
            return null;
        }
    }
}
