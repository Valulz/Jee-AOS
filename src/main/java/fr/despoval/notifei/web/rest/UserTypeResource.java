package fr.despoval.notifei.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.despoval.notifei.domain.UserType;

import fr.despoval.notifei.repository.UserTypeRepository;
import fr.despoval.notifei.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserType.
 */
@RestController
@RequestMapping("/api")
public class UserTypeResource {

    private final Logger log = LoggerFactory.getLogger(UserTypeResource.class);
        
    @Inject
    private UserTypeRepository userTypeRepository;

    /**
     * POST  /user-types : Create a new userType.
     *
     * @param userType the userType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userType, or with status 400 (Bad Request) if the userType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-types")
    @Timed
    public ResponseEntity<UserType> createUserType(@Valid @RequestBody UserType userType) throws URISyntaxException {
        log.debug("REST request to save UserType : {}", userType);
        if (userType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userType", "idexists", "A new userType cannot already have an ID")).body(null);
        }
        UserType result = userTypeRepository.save(userType);
        return ResponseEntity.created(new URI("/api/user-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-types : Updates an existing userType.
     *
     * @param userType the userType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userType,
     * or with status 400 (Bad Request) if the userType is not valid,
     * or with status 500 (Internal Server Error) if the userType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-types")
    @Timed
    public ResponseEntity<UserType> updateUserType(@Valid @RequestBody UserType userType) throws URISyntaxException {
        log.debug("REST request to update UserType : {}", userType);
        if (userType.getId() == null) {
            return createUserType(userType);
        }
        UserType result = userTypeRepository.save(userType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userType", userType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-types : get all the userTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userTypes in body
     */
    @GetMapping("/user-types")
    @Timed
    public List<UserType> getAllUserTypes() {
        log.debug("REST request to get all UserTypes");
        List<UserType> userTypes = userTypeRepository.findAll();
        return userTypes;
    }

    /**
     * GET  /user-types/:id : get the "id" userType.
     *
     * @param id the id of the userType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userType, or with status 404 (Not Found)
     */
    @GetMapping("/user-types/{id}")
    @Timed
    public ResponseEntity<UserType> getUserType(@PathVariable Long id) {
        log.debug("REST request to get UserType : {}", id);
        UserType userType = userTypeRepository.findOne(id);
        return Optional.ofNullable(userType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-types/:id : delete the "id" userType.
     *
     * @param id the id of the userType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserType(@PathVariable Long id) {
        log.debug("REST request to delete UserType : {}", id);
        userTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userType", id.toString())).build();
    }

}
