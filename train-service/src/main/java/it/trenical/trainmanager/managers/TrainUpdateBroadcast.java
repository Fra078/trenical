package it.trenical.trainmanager.managers;

import it.trenical.train.proto.TrainUpdate;
import it.trenical.trainmanager.observer.TrainUpdateObserver;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrainUpdateBroadcast {

    private final CopyOnWriteArrayList<TrainUpdateObserver> observers = new CopyOnWriteArrayList<>();

    public void subscribe(TrainUpdateObserver observer) {
        observers.add(observer);
    }

    public void pushUpdate(TrainUpdate update) {
        Iterator<TrainUpdateObserver> it = observers.iterator();
        while(it.hasNext()){
            TrainUpdateObserver observer = it.next();
            try {
                if (observer.canHandle(update.getTrainId()))
                    observer.onNext(update);
            } catch (Exception e){
                it.remove();
            }
        }
    }

}
