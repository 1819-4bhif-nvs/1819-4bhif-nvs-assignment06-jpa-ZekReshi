package at.htl.homeautomation.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "HA_ACTOR")
@NamedQueries({
        @NamedQuery(name = "Actor.findAll", query = "select a from Actor a")
})
public class Actor extends Device {

    public Actor() {}

    public Actor(String name, Location location) {
        super(name, location);
    }
}
