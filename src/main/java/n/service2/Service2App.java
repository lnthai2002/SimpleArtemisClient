package n.service2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"n.service2"})
@SpringBootApplication
public class Service2App {

  public static void main(String[] args) {
    SpringApplication.run(Service2App.class, args);
  }
}
