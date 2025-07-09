package it.trenical.promotion.observer;


public interface Observer<T> {

    void onUpdate(T promotion);

    void onClosed();

}
