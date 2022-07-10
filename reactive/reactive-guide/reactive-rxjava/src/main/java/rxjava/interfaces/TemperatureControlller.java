package rxjava.interfaces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rxjava.domain.TemperatureBus;


@RestController
public class TemperatureControlller {

	private final TemperatureBus temperatureBus;

	public TemperatureControlller(TemperatureBus temperatureBus) {
		this.temperatureBus = temperatureBus;
	}

	@GetMapping("/temperature-stream")
	public SseEmitter addEvents() {
		RxSseEmitter rxSseEmitter = new RxSseEmitter();
		temperatureBus.temperatureStream().subscribe(rxSseEmitter.getSubscriber());
		return rxSseEmitter;
	}


}
