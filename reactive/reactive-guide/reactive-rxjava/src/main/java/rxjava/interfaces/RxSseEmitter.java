package rxjava.interfaces;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rxjava.domain.Temperature;

import java.io.IOException;

@Slf4j
@Data
public class RxSseEmitter extends SseEmitter {
	private Observer<Temperature> subscriber;

	public RxSseEmitter() {
		super(30 * 60 * 1000L);
		subscriber = new Observer<>() {
			private Disposable disposable;
			@Override
			public void onSubscribe(@NonNull Disposable d) {
				disposable = d;
			}
			@Override
			public void onNext(@NonNull Temperature temperature) {
				try {
					RxSseEmitter.this.send(temperature);
				} catch (IOException e) {
					log.error("subscriber onNext error", e);
					onComplete();
				}
			}

			@Override
			public void onError(@NonNull Throwable e) {
				disposable.dispose();
			}

			@Override
			public void onComplete() {
				if (disposable != null) {
					disposable.dispose();
				}
			}
		};
		onCompletion(() -> subscriber.onComplete());
		onTimeout(()  -> subscriber.onComplete());
	}
}
