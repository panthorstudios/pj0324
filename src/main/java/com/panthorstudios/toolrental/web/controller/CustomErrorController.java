package com.panthorstudios.toolrental.web.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        logger.error("Error status: {}", status);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            logger.error("Status code: {}", statusCode);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "404";
            }
            return "error";
        }

        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
}