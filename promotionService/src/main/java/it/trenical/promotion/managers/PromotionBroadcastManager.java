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
    private static final Logger logger = Logger.getLogger(PromotionBroadcastManager.class.getName());

    private final Map<String, Observer<Promotion>> activeListeners = new ConcurrentHashMap<>();
    private final ExecutorService notificationExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void register(String userId, Observer<Promotion> observer) {
        activeListeners.put(userId, observer);
        logger.info("Utente '" + userId + "' ha iniziato l'ascolto. Ascoltatori totali: " + activeListeners.size());
    }

    @Override
    public void broadcast(Promotion promotion) {
        notificationExecutor.submit(() -> {
            logger.info("📢 Avvio broadcast per '" + promotion.getDescription() + "'...");
            activeListeners.forEach((userId, observer) -> {
                try {
                    observer.onUpdate(promotion);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warning("Errore durante notifica a '" + userId + "'. Verrà rimosso.");
                }
            });
        });
    }

    @Override
    public void disconnectUser(String userId) {
        Observer<Promotion> observer = activeListeners.remove(userId);
        if (observer != null) {
            logger.warning("Forzando la disconnessione per l'utente '" + userId);
            observer.onClosed();
        }
    }

}
