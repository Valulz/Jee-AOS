package fr.despoval.notifei.repository;

import fr.despoval.notifei.domain.AdverseEffect;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AdverseEffect entity.
 */
@SuppressWarnings("unused")
public interface AdverseEffectRepository extends JpaRepository<AdverseEffect,Long> {

}
