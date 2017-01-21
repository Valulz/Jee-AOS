package fr.despoval.notifei.repository;

import fr.despoval.notifei.domain.Ingredient;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ingredient entity.
 */
@SuppressWarnings("unused")
public interface IngredientRepository extends JpaRepository<Ingredient,Long> {

}
