package it.trenical.promotion.observer;

import io.grpc.stub.StreamObserver;
import java.util.function.Function;

public class StreamObserverAdapter<T, G> implements Observer<T>{

    private final StreamObserver<G> streamObserver;
    private final Function<T,G> mapFunction;

    public StreamObserverAdapter(StreamObserver<G> streamObserver, Function<T, G> mapFunction) {
        this.streamObserver = streamObserver;
        this.mapFunction = mapFunction;
    }

    @Override
    public void onUpdate(T promotion) {
        streamObserver.onNext(mapFunction.apply(promotion));
    }

    @Override
    public void onClosed() {
        streamObserver.onCompleted();
    }
}
