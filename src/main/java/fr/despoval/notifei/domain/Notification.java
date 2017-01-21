package fr.despoval.notifei.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @ManyToOne
    @NotNull
    private UserAE user;

    @ManyToOne
    @NotNull
    private Product product;

    @ManyToOne
    @NotNull
    private AdverseEffect effect;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Notification date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserAE getUser() {
        return user;
    }

    public Notification user(UserAE userAE) {
        this.user = userAE;
        return this;
    }

    public void setUser(UserAE userAE) {
        this.user = userAE;
    }

    public Product getProduct() {
        return product;
    }

    public Notification product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public AdverseEffect getEffect() {
        return effect;
    }

    public Notification effect(AdverseEffect adverseEffect) {
        this.effect = adverseEffect;
        return this;
    }

    public void setEffect(AdverseEffect adverseEffect) {
        this.effect = adverseEffect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notification notification = (Notification) o;
        if (notification.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notification.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Notification{" +
            "id=" + id +
            ", date='" + date + "'" +
            '}';
    }
}
