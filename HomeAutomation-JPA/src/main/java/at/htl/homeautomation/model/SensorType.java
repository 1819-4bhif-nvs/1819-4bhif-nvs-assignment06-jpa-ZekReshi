package at.htl.homeautomation.model;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HA_SENSORTYPE")
@NamedQueries({
        @NamedQuery(name = "SensorType.findAll", query = "select s from SensorType s"),
})
public class SensorType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Version
    @Column(name = "version")
    private int version;
    private String name;
    private String unit;
    @JsonbTransient
    @XmlTransient
    @OneToMany(mappedBy = "type")
    private List<Sensor> sensors = new ArrayList<>();

    public SensorType() {
    }

    public SensorType(String name, String unit) {
        this.name = name;
        this.unit = unit;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    // endregion

}
