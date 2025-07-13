package it.trenical.promotion.managers;

import it.trenical.promotion.models.Promotion;
import it.trenical.promotion.observer.Broadcast;
import it.trenical.promotion.observer.Observer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class PromotionBroadcastManager implements Broadcast<Promotion> {
    private final Map<String, Observer<Promotion>> activeListeners = new ConcurrentHashMap<>();
    private final ExecutorService notificationExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void register(String userId, Observer<Promotion> observer) {
        activeListeners.put(userId, observer);
    }

    @Override
    public void broadcast(Promotion promotion) {
        notificationExecutor.submit(() -> {
            activeListeners.forEach((userId, observer) -> {
                try {
                    observer.onUpdate(promotion);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public void disconnectUser(String userId) {
        Observer<Promotion> observer = activeListeners.remove(userId);
        if (observer != null) {
            observer.onClosed();
        }
    }

}
