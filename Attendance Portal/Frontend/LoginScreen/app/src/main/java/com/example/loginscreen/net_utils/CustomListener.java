package com.example.loginscreen.net_utils;

/**
 * Listener interface
 * @param <T> Can listen to any object
 */
public interface CustomListener<T> {
    /**
     * Gets the result of the listener
     * @param object Object you are checking
     */
    public void getResult(T object);
}
