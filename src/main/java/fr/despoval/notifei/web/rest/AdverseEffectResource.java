package fr.despoval.notifei.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.despoval.notifei.domain.AdverseEffect;

import fr.despoval.notifei.repository.AdverseEffectRepository;
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
 * REST controller for managing AdverseEffect.
 */
@RestController
@RequestMapping("/api")
public class AdverseEffectResource {

    private final Logger log = LoggerFactory.getLogger(AdverseEffectResource.class);
        
    @Inject
    private AdverseEffectRepository adverseEffectRepository;

    /**
     * POST  /adverse-effects : Create a new adverseEffect.
     *
     * @param adverseEffect the adverseEffect to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adverseEffect, or with status 400 (Bad Request) if the adverseEffect has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/adverse-effects")
    @Timed
    public ResponseEntity<AdverseEffect> createAdverseEffect(@Valid @RequestBody AdverseEffect adverseEffect) throws URISyntaxException {
        log.debug("REST request to save AdverseEffect : {}", adverseEffect);
        if (adverseEffect.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("adverseEffect", "idexists", "A new adverseEffect cannot already have an ID")).body(null);
        }
        AdverseEffect result = adverseEffectRepository.save(adverseEffect);
        return ResponseEntity.created(new URI("/api/adverse-effects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("adverseEffect", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /adverse-effects : Updates an existing adverseEffect.
     *
     * @param adverseEffect the adverseEffect to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adverseEffect,
     * or with status 400 (Bad Request) if the adverseEffect is not valid,
     * or with status 500 (Internal Server Error) if the adverseEffect couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/adverse-effects")
    @Timed
    public ResponseEntity<AdverseEffect> updateAdverseEffect(@Valid @RequestBody AdverseEffect adverseEffect) throws URISyntaxException {
        log.debug("REST request to update AdverseEffect : {}", adverseEffect);
        if (adverseEffect.getId() == null) {
            return createAdverseEffect(adverseEffect);
        }
        AdverseEffect result = adverseEffectRepository.save(adverseEffect);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("adverseEffect", adverseEffect.getId().toString()))
            .body(result);
    }

    /**
     * GET  /adverse-effects : get all the adverseEffects.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of adverseEffects in body
     */
    @GetMapping("/adverse-effects")
    @Timed
    public List<AdverseEffect> getAllAdverseEffects() {
        log.debug("REST request to get all AdverseEffects");
        List<AdverseEffect> adverseEffects = adverseEffectRepository.findAll();
        return adverseEffects;
    }

    /**
     * GET  /adverse-effects/:id : get the "id" adverseEffect.
     *
     * @param id the id of the adverseEffect to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adverseEffect, or with status 404 (Not Found)
     */
    @GetMapping("/adverse-effects/{id}")
    @Timed
    public ResponseEntity<AdverseEffect> getAdverseEffect(@PathVariable Long id) {
        log.debug("REST request to get AdverseEffect : {}", id);
        AdverseEffect adverseEffect = adverseEffectRepository.findOne(id);
        return Optional.ofNullable(adverseEffect)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /adverse-effects/:id : delete the "id" adverseEffect.
     *
     * @param id the id of the adverseEffect to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/adverse-effects/{id}")
    @Timed
    public ResponseEntity<Void> deleteAdverseEffect(@PathVariable Long id) {
        log.debug("REST request to delete AdverseEffect : {}", id);
        adverseEffectRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("adverseEffect", id.toString())).build();
    }

}
