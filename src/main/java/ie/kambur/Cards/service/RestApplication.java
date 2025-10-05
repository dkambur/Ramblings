package ie.kambur.Cards.service;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Set;
import java.util.HashSet;

@ApplicationPath("")
public class RestApplication extends Application {


    /**
     * Initialisation code
     * @return
     */
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(DeckApiServiceImpl.class);  // Add your controller here
        classes.add(Health.class);
        return classes;
    }
}