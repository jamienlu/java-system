package rxjava.domain;

import io.reactivex.rxjava3.core.Observable;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class TemperatureBus {
	private final Random random = new SecureRandom();
	private final Observable<Temperature> stream = Observable.range(0, 20)
			.concatMap(ti -> Observable.just(ti).delay(random.nextInt(3000), TimeUnit.MILLISECONDS)
			.map(tivalue -> this.probe())).publish().refCount();

	public Observable<Temperature> temperatureStream() {
		return stream;
	}

	private Temperature probe() {
		return new Temperature(16 + random.nextGaussian() * 10);
	}

}
