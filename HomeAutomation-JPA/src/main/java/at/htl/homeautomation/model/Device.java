package at.htl.homeautomation.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name = "HA_DEVICE")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NamedQueries({
        @NamedQuery(name = "Device.findAll", query = "select d from Device d")
})
public abstract class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Version
    @Column(name = "version")
    private int version;
    private String name;
    @ManyToOne
    private Location location;

    public Device(){}

    public Device(String name, Location location) {
        this.name = name;
        this.location = location;
        this.location.getDevices().add(this);
    }

    // region Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (this.location != null) {
            this.location.getDevices().remove(this);
        }
        this.location = location;
        if (this.location != null) {
            this.location.getDevices().add(this);
        }
    }

    //endregion

}
