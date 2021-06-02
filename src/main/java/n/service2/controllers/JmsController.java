package n.service2.controllers;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.apache.activemq.artemis.api.core.Message.HDR_DUPLICATE_DETECTION_ID;

@RestController
@Transactional(rollbackFor = Throwable.class)
public class JmsController {

  @Autowired
  private JmsTemplate jmsTemplate;

  @GetMapping("/v1/jms/sendToQueue/{queue}/count/{count}/interval/{interval}")
  public ResponseEntity<String> sendToQueue(@PathVariable String queue, @PathVariable int count, @PathVariable int interval, @RequestParam String message)
      throws InterruptedException {

    for (int i = 0; i < count; i++) {
      jmsTemplate.convertAndSend(queue, message + "-" + i, m -> {

        String uuid = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();

        m.setJMSCorrelationID(UUID.randomUUID().toString());
        m.setJMSMessageID("ID:" + uuid);

        Destination dest = new ActiveMQQueue(queue);

        m.setJMSDestination(dest);
        m.setJMSReplyTo(dest);
        m.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
        m.setJMSPriority(Message.DEFAULT_PRIORITY);
        m.setJMSTimestamp(System.nanoTime());
        m.setJMSType("test");

        m.setStringProperty(HDR_DUPLICATE_DETECTION_ID.toString(), uuid);
        m.setStringProperty("sentBy", "sender");
        m.setStringProperty("sentFrom", "source");
        m.setLongProperty("sentAt", timestamp);

        return m;
      });
      TimeUnit.MILLISECONDS.sleep(interval);
    }

    return ResponseEntity.status(HttpStatus.OK).body(null);

  }
}
