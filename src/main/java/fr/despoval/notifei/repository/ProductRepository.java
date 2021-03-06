package fr.despoval.notifei.repository;

import fr.despoval.notifei.domain.AdverseEffect;
import fr.despoval.notifei.domain.Product;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select distinct product from Product product left join fetch product.ingredients")
    List<Product> findAllWithEagerRelationships();

    @Query("select product from Product product left join fetch product.ingredients where product.id =:id")
    Product findOneWithEagerRelationships(@Param("id") Long id);

    @Query("SELECT effect FROM AdverseEffect effect JOIN Notification notif " +
        "ON effect.id = notif.effect.id WHERE notif.product.id = :id")
    List<AdverseEffect> findAdverseEffectByProductId(@Param("id") Long id);
}
