package fr.despoval.notifei.repository;

import fr.despoval.notifei.domain.UserType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserType entity.
 */
@SuppressWarnings("unused")
public interface UserTypeRepository extends JpaRepository<UserType,Long> {

}
