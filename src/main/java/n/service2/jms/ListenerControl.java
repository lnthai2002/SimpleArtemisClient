package n.service2.jms;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class ListenerControl {
    private Long duration = 0L;
    private Integer throwEvery = null;
}
