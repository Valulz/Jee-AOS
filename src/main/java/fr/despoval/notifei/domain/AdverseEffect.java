package fr.despoval.notifei.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A AdverseEffect.
 */
@Entity
@Table(name = "adverse_effect")
public class AdverseEffect implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @OneToMany(mappedBy = "effect")
    @JsonIgnore
    private Set<Notification> notifications = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AdverseEffect name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public AdverseEffect notifications(Set<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    public AdverseEffect addNotifications(Notification notification) {
        notifications.add(notification);
        notification.setEffect(this);
        return this;
    }

    public AdverseEffect removeNotifications(Notification notification) {
        notifications.remove(notification);
        notification.setEffect(null);
        return this;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdverseEffect adverseEffect = (AdverseEffect) o;
        if (adverseEffect.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, adverseEffect.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AdverseEffect{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
