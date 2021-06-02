package n.service2.controllers;

import n.service2.jms.ListenerControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/v1/messaging")
@RestController
public class MessagingController {

    private final JmsListenerEndpointRegistry registry;
    private final ListenerControl listenerControl;


    public MessagingController(JmsListenerEndpointRegistry registry, ListenerControl listenerControl) {
        this.registry = registry;
        this.listenerControl = listenerControl;
    }

    @GetMapping("/adjustListeners")
    public ResponseEntity<String> setListenersDelay(@RequestParam(required = false) Long delay, @RequestParam(required = false) Integer throwEvery) {
        if (delay != null) {
            listenerControl.setDuration(delay);

        }
        if (throwEvery != null) {
            listenerControl.setThrowEvery(throwEvery);
        }
        return ResponseEntity.ok(null);
    }
}
