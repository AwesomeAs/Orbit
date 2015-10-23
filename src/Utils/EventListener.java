package Utils;

/*
 * 
 * Utility interface for defining an event.
 * 
 * Java 1.7
 * 
 */

//An interface to be implemented by everyone interested in generic events
public interface EventListener {
    void event();
    void event(Object[] args);
}

