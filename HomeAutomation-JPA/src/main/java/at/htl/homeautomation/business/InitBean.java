package at.htl.homeautomation.business;

import at.htl.homeautomation.model.*;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Startup
@Singleton
public class InitBean {

    @PersistenceContext
    EntityManager em;

    private InitBean() {
    }

    @PostConstruct
    public void init() {
        System.err.println("********** hello");

        Location livingL = new Location("Wohnzimmer");
        Location bathL = new Location("Badezimmer");
        Location bedL = new Location("Schlafzimmer");
        List<Location> locations = new LinkedList<>();
        locations.add(livingL);
        locations.add(bathL);
        locations.add(bedL);
        persistCollection(locations);

        Sensor livingTempS = new Sensor("TEMP1", livingL);
        Sensor livingLightS = new Sensor("LIGHT1", livingL);
        Sensor bathHumidS = new Sensor("HUMID1", bathL);
        Sensor bedTempS = new Sensor("TEMP2", bedL);
        List<Sensor> sensors = new LinkedList<>();
        sensors.add(livingTempS);
        sensors.add(livingLightS);
        sensors.add(bathHumidS);
        sensors.add(bedTempS);
        persistCollection(sensors);

        Measurement tempST = new Measurement(22.1d, "degC", LocalDateTime.parse("2018-12-08T12:04:32"), livingTempS);
        Measurement humidST = new Measurement(55d, "%", LocalDateTime.parse("2018-12-08T12:01:58"), bathHumidS);
        Measurement lightST = new Measurement(25d, "ca", LocalDateTime.parse("2018-12-06T12:05:34"), livingLightS);
        List<Measurement> measurements = new LinkedList<>();
        measurements.add(tempST);
        measurements.add(humidST);
        measurements.add(lightST);
        persistCollection(measurements);

        Actor livingHeaterA = new Actor("Heizung", livingL);
        Actor bathLightA = new Actor("Beleuchtung", bathL);
        Actor bedLampA = new Actor("Lampe", bedL);
        List<Actor> actors = new LinkedList<>();
        actors.add(livingHeaterA);
        actors.add(bathLightA);
        actors.add(bedLampA);
        persistCollection(actors);
    }

    private <T> void persistCollection(Collection<T> coll){
        for (T obj : coll) {
            em.persist(obj);
        }
    }

}
