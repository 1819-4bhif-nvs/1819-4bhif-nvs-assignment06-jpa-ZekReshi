package at.htl.homeautomation.model;

import at.htl.homeautomation.business.LocalDateTimeAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Entity
@Table(name = "HA_SENSOR")
@NamedQueries({
        @NamedQuery(name = "Sensor.findAll", query = "select s from Sensor s")
})
public class Sensor extends Device {

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime lastMeasurement;
    @ManyToOne
    private SensorType type;

    public Sensor() {}

    public Sensor(String name, LocalDateTime lastMeasurement, Location location, SensorType type) {
        super(name, location);
        this.lastMeasurement = lastMeasurement;
        this.type = type;
        this.type.getSensors().add(this);
    }

    // region Getter and Setter

    public LocalDateTime getLastMeasurement() {
        return lastMeasurement;
    }

    public void setLastMeasurement(LocalDateTime lastMeasurement) {
        this.lastMeasurement = lastMeasurement;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type.getSensors().remove(this);
        this.type = type;
        this.type.getSensors().add(this);
    }

    // endregion

}
