package fr.despoval.notifei.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.despoval.notifei.domain.UserAE;

import fr.despoval.notifei.repository.UserAERepository;
import fr.despoval.notifei.web.rest.util.HeaderUtil;
import fr.despoval.notifei.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing UserAE.
 */
@RestController
@RequestMapping("/api")
public class UserAEResource {

    private final Logger log = LoggerFactory.getLogger(UserAEResource.class);
        
    @Inject
    private UserAERepository userAERepository;

    /**
     * POST  /user-aes : Create a new userAE.
     *
     * @param userAE the userAE to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userAE, or with status 400 (Bad Request) if the userAE has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-aes")
    @Timed
    public ResponseEntity<UserAE> createUserAE(@Valid @RequestBody UserAE userAE) throws URISyntaxException {
        log.debug("REST request to save UserAE : {}", userAE);
        if (userAE.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userAE", "idexists", "A new userAE cannot already have an ID")).body(null);
        }
        UserAE result = userAERepository.save(userAE);
        return ResponseEntity.created(new URI("/api/user-aes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userAE", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-aes : Updates an existing userAE.
     *
     * @param userAE the userAE to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userAE,
     * or with status 400 (Bad Request) if the userAE is not valid,
     * or with status 500 (Internal Server Error) if the userAE couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-aes")
    @Timed
    public ResponseEntity<UserAE> updateUserAE(@Valid @RequestBody UserAE userAE) throws URISyntaxException {
        log.debug("REST request to update UserAE : {}", userAE);
        if (userAE.getId() == null) {
            return createUserAE(userAE);
        }
        UserAE result = userAERepository.save(userAE);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userAE", userAE.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-aes : get all the userAES.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userAES in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/user-aes")
    @Timed
    public ResponseEntity<List<UserAE>> getAllUserAES(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserAES");
        Page<UserAE> page = userAERepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-aes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-aes/:id : get the "id" userAE.
     *
     * @param id the id of the userAE to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userAE, or with status 404 (Not Found)
     */
    @GetMapping("/user-aes/{id}")
    @Timed
    public ResponseEntity<UserAE> getUserAE(@PathVariable Long id) {
        log.debug("REST request to get UserAE : {}", id);
        UserAE userAE = userAERepository.findOne(id);
        return Optional.ofNullable(userAE)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-aes/:id : delete the "id" userAE.
     *
     * @param id the id of the userAE to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-aes/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserAE(@PathVariable Long id) {
        log.debug("REST request to delete UserAE : {}", id);
        userAERepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userAE", id.toString())).build();
    }

}
