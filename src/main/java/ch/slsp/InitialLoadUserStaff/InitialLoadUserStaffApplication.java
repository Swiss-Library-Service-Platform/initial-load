package ch.slsp.InitialLoadUserStaff;

import ch.slsp.InitialLoadUserStaff.rest.Command;
import ch.slsp.InitialLoadUserStaff.util.LoggerFactory;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class InitialLoadUserStaffApplication {


	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(InitialLoadUserStaffApplication.class, args);
	}

	@Component
	public static class CommandExecutor implements CommandLineRunner {
		private static final Logger LOG = LoggerFactory.getLogger();

		@Autowired
		private Command command;

		@Override
		public void run(String... args) throws Exception {
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			LOG.info("Start executing command {}", command.getClass().getName());
			try {
				command.execute(args);
			} catch (Exception e) {
				LOG.error("Unknown error occures: ", e);
			}
		}
	}
}
