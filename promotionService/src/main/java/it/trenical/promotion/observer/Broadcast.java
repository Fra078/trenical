package it.trenical.promotion.observer;

public interface Broadcast<T> {
    void register(String userId, Observer<T> observer);
    void broadcast(T promotion);
    void disconnectUser(String userId);
}