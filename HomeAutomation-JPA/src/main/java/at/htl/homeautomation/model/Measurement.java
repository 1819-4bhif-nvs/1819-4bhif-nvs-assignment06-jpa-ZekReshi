package at.htl.homeautomation.model;

import at.htl.homeautomation.business.LocalDateTimeAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Entity
@Table(name = "HA_MEASUREMENT")
@NamedQueries({
        @NamedQuery(name = "Measurement.findAll", query = "select m from Measurement m"),
})
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Version
    @Column(name = "version")
    private int version;
    private Double value;
    private String unit;
    @ManyToOne
    private Sensor sensor;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime time;

    public Measurement() {
    }

    public Measurement(Double value, String unit, LocalDateTime time, Sensor sensor) {
        this.value = value;
        this.unit = unit;
        this.time = time;
        this.sensor = sensor;
        this.sensor.getMeasurements().add(this);
    }

    // region Getter and Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        if (this.sensor != null) {
            this.sensor.getMeasurements().remove(this);
        }
        this.sensor = sensor;
        if (this.sensor != null) {
            this.sensor.getMeasurements().add(this);
        }
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    // endregion

}
