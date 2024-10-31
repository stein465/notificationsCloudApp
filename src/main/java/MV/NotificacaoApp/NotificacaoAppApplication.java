package MV.NotificacaoApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NotificacaoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificacaoAppApplication.class, args);
	}

}
