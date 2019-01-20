package at.htl.homeautomation.model;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HA_SENSOR")
@NamedQueries({
        @NamedQuery(name = "Sensor.findAll", query = "select s from Sensor s")
})
public class Sensor extends Device {

    @JsonbTransient
    @XmlTransient
    @OneToMany(mappedBy = "sensor")
    private List<Measurement> measurements = new ArrayList<>();

    public Sensor() {}

    public Sensor(String name, Location location) {
        super(name, location);
    }

    // region Getter and Setter

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    // endregion

}
