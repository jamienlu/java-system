package springevent.domain;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class TemperatureBus {
	private final ApplicationEventPublisher publisher;
	private final Random random = new SecureRandom();
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	public TemperatureBus(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@PostConstruct
	public void process() {
		this.executorService.schedule(this::probe, 1, TimeUnit.SECONDS);
	}

	private void probe() {
		double value = 10 + random.nextGaussian() * 10;
		publisher.publishEvent(new Temperature(value));
		executorService.schedule(this :: probe, random.nextInt(5000), TimeUnit.MILLISECONDS);
	}

}
