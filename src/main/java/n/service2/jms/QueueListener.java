package n.service2.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class QueueListener {

  @Autowired
  private ListenerControl listenerControl;

  @JmsListener(id = "queueListener0", destination = "QUEUE.service2.notification")
  public void add(String message, @Header("sentBy") String sentBy, @Header("sentFrom") String sentFrom, @Header("sentAt") Long sentAt) throws Throwable {

    String attempt = UUID.randomUUID().toString();
    log.info(String.format("---Start attempt %s", attempt));

    log.info("---QUEUE[notification]:  message={}, sentBy={}, sentFrom={}, sentAt={}",
                message, sentBy, sentFrom, sentAt);

    log.info(String.format("---Attempt %s saved, sleep now", attempt));

    TimeUnit.MILLISECONDS.sleep(listenerControl.getDuration());

    Integer throwInterval = listenerControl.getThrowEvery();
    if (throwInterval != null) {
      mayThrowRuntimeException(message, throwInterval);
    }

    log.info(String.format("---Attempt %s processed", attempt));
  }

  private void mayThrowRuntimeException(String message, Integer interval) {
    String[] parts = message.split("-");
    int number = Integer.parseInt(parts[parts.length - 1]);
    if (interval != 0 && number % interval == 0) {
      throw new RuntimeException("Intentional throw");
    }
  }
}
