package fr.despoval.notifei.repository;

import fr.despoval.notifei.domain.UserAE;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserAE entity.
 */
@SuppressWarnings("unused")
public interface UserAERepository extends JpaRepository<UserAE,Long> {

}
