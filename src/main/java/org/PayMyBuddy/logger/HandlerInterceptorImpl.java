package org.PayMyBuddy.logger;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.PayMyBuddy.constant.UriToIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerInterceptorImpl implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(HandlerInterceptorImpl.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURL = java.net.URLDecoder.decode(request.getRequestURL().toString(), StandardCharsets.UTF_8);

        if (!UriToIgnore.uriToIgnore.contains(request.getRequestURI())) {
            if (!request.getParameterMap().isEmpty()) {
                String requestParameters = "?" + request.getParameterMap().entrySet().stream()
                        .map(e -> e.getKey() + "=" + String.join(", ", e.getValue())).collect(Collectors.joining("&"));

                logger.info("URL requested : {} {}{}", request.getMethod(), requestURL, requestParameters);
            } else {
                logger.info("Endpoint requested : {} {}", request.getMethod(), requestURL);
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception exception) throws Exception {
        int responseStatus = response.getStatus();

        if (!UriToIgnore.uriToIgnore.contains(request.getRequestURI())) {
            switch (responseStatus) {
                case 200:
                    logger.info("Response : Status {} OK", responseStatus);
                    break;
                case 201:
                    logger.info("Response : Status {} Created", responseStatus);
                    break;
                case 204:
                    logger.info("Response : Status {} No Content", responseStatus);
                    break;
                case 302:
                    logger.info("Response : Status {} Found", responseStatus);
                    break;
                case 400:
                    logger.error("Response : Status {} Bad Request", responseStatus);
                    break;
                case 404:
                    logger.error("Response : Status {} Not Found", responseStatus);
                    break;
                case 500:
                    logger.error("Response : Status {} Internal Server Error", responseStatus);
                    break;
                default:
                    logger.error("Status unknown");
            }
        }
    }
}