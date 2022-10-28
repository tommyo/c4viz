package com.morch.c4viz;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

    @MessageMapping("/list")
    @SendTo("/topic/list")
    public String[] list() throws Exception {
        return FileList.list();
    }

    @MessageExceptionHandler
    @SendTo("/topic/error")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
