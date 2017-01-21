package fr.despoval.notifei.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UserAE.
 */
@Entity
@Table(name = "userae")
public class UserAE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 255)
    @Column(name = "region", length = 255)
    private String region;

    @OneToOne
    @JoinColumn(unique = true)
    private User owner;

    @ManyToOne
    @NotNull
    private UserType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public UserAE region(String region) {
        this.region = region;
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public User getOwner() {
        return owner;
    }

    public UserAE owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public UserType getType() {
        return type;
    }

    public UserAE type(UserType userType) {
        this.type = userType;
        return this;
    }

    public void setType(UserType userType) {
        this.type = userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAE userAE = (UserAE) o;
        if (userAE.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userAE.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserAE{" +
            "id=" + id +
            ", region='" + region + "'" +
            '}';
    }
}
