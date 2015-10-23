package Utils;
// CREDITS: http://stackoverflow.com/a/6270150

/*
 * 
 * Utility class for firing and attaching EventListeners.
 * 
 * Java 1.7
 * 
 */

import java.util.*;

// The event caller
public class EventInitiater {
    private List<EventListener> listeners = new ArrayList<EventListener>();

    public void addListener(EventListener eventListener) {
        listeners.add(eventListener);
    }

    public void fire() {
        // Notify everybody that may be interested.
        for (EventListener hl : listeners)
            hl.event();
    }
    
    public void fire(Object... args) {
    	// Notify everybody that may be interested.
    	for (EventListener hl : listeners)
    		hl.event(args);
    }
}