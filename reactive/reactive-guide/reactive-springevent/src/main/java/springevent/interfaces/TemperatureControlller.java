package springevent.interfaces;

import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springevent.domain.Temperature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class TemperatureControlller {
	private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

	@GetMapping("/temperature-stream")
	public SseEmitter addEvents() {
		SseEmitter sseEmitter = new SseEmitter();
		clients.add(sseEmitter);
		sseEmitter.onTimeout(() -> clients.remove(sseEmitter));
		sseEmitter.onCompletion(() -> clients.remove(sseEmitter));
		return sseEmitter;
	}

	@Async
	@EventListener
	public void handle(Temperature temperature) {
		List<SseEmitter> deadEmitter = new ArrayList<>();
		clients.forEach(x -> {
			try {
				x.send(temperature, MediaType.APPLICATION_JSON);
			} catch (IOException e) {
				deadEmitter.add(x);
				e.printStackTrace();
			}
		});
		clients.remove(deadEmitter);
	}
}
