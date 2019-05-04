package com.example.loginscreen;

/**
 * Stopwatch to keep track of time
 */
public class Stopwatch {
    /**
     * The elapsed time
     */
    private long elapsedTime = 0;
    /**
     * Most recent start time
     */
    private long mostRecentStartTime = -1;
    /**
     * Is stopwatch started
     */
    private boolean started = false;
    /**
     * Is stopwatch running
     */
    private boolean running = false;


    //Resets elapsed time to 0 and starts timer.

    /**
     * Starts the stopwatch
     */
    public void start() {
        if (running) {
            return;
        }
        elapsedTime = 0;
        mostRecentStartTime = System.currentTimeMillis();
        running = true;
        started = true;
    }

    //Stops timer, storing elapsed time.

    /**
     * Stops the stopwatch
     */
    public void stop() {
        if (!running) {
            return;
        }
        elapsedTime += System.currentTimeMillis() - mostRecentStartTime;
        running = false;
    }

    //Restarts timer

    /**
     * Resumes the stopwatch
     */
    public void resume() {
        if (!started) {
            throw new IllegalStateException();
        }
        if (running) {
            return;
        }
        mostRecentStartTime = System.currentTimeMillis();
        running = true;
    }


    //Returns the elapsed time of a stopper timer.

    /**
     * Gets the elapsed time of the timer
     * @return The amount of time that has passed in milliseconds
     */
    public long getElapsedTime() {
        if (!started) {
            throw new IllegalStateException();
        }
        if (running) {
            return System.currentTimeMillis() - mostRecentStartTime + elapsedTime;
        }
        return elapsedTime;
    }

    /**
     * Checks if the timer is running
     * @return True if timer is running, false otherwise
     */
    public boolean isRunning(){return running;}
}
