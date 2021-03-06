package fr.despoval.notifei.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "product_ingredients",
               joinColumns = @JoinColumn(name="products_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="ingredients_id", referencedColumnName="ID"))
    private Set<Ingredient> ingredients = new HashSet<>();

    @OneToMany(mappedBy = "product")
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

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public Product ingredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public Product addIngredients(Ingredient ingredient) {
        ingredients.add(ingredient);
        ingredient.getProducts().add(this);
        return this;
    }

    public Product removeIngredients(Ingredient ingredient) {
        ingredients.remove(ingredient);
        ingredient.getProducts().remove(this);
        return this;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public Product notifications(Set<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    public Product addNotifications(Notification notification) {
        notifications.add(notification);
        notification.setProduct(this);
        return this;
    }

    public Product removeNotifications(Notification notification) {
        notifications.remove(notification);
        notification.setProduct(null);
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
        Product product = (Product) o;
        if (product.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
