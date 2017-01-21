package fr.despoval.notifei.web.rest;

import fr.despoval.notifei.NotifApp;

import fr.despoval.notifei.domain.AdverseEffect;
import fr.despoval.notifei.repository.AdverseEffectRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AdverseEffectResource REST controller.
 *
 * @see AdverseEffectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotifApp.class)
public class AdverseEffectResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private AdverseEffectRepository adverseEffectRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAdverseEffectMockMvc;

    private AdverseEffect adverseEffect;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdverseEffectResource adverseEffectResource = new AdverseEffectResource();
        ReflectionTestUtils.setField(adverseEffectResource, "adverseEffectRepository", adverseEffectRepository);
        this.restAdverseEffectMockMvc = MockMvcBuilders.standaloneSetup(adverseEffectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdverseEffect createEntity(EntityManager em) {
        AdverseEffect adverseEffect = new AdverseEffect()
                .name(DEFAULT_NAME);
        return adverseEffect;
    }

    @Before
    public void initTest() {
        adverseEffect = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdverseEffect() throws Exception {
        int databaseSizeBeforeCreate = adverseEffectRepository.findAll().size();

        // Create the AdverseEffect

        restAdverseEffectMockMvc.perform(post("/api/adverse-effects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adverseEffect)))
            .andExpect(status().isCreated());

        // Validate the AdverseEffect in the database
        List<AdverseEffect> adverseEffectList = adverseEffectRepository.findAll();
        assertThat(adverseEffectList).hasSize(databaseSizeBeforeCreate + 1);
        AdverseEffect testAdverseEffect = adverseEffectList.get(adverseEffectList.size() - 1);
        assertThat(testAdverseEffect.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createAdverseEffectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adverseEffectRepository.findAll().size();

        // Create the AdverseEffect with an existing ID
        AdverseEffect existingAdverseEffect = new AdverseEffect();
        existingAdverseEffect.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdverseEffectMockMvc.perform(post("/api/adverse-effects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAdverseEffect)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AdverseEffect> adverseEffectList = adverseEffectRepository.findAll();
        assertThat(adverseEffectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = adverseEffectRepository.findAll().size();
        // set the field null
        adverseEffect.setName(null);

        // Create the AdverseEffect, which fails.

        restAdverseEffectMockMvc.perform(post("/api/adverse-effects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adverseEffect)))
            .andExpect(status().isBadRequest());

        List<AdverseEffect> adverseEffectList = adverseEffectRepository.findAll();
        assertThat(adverseEffectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdverseEffects() throws Exception {
        // Initialize the database
        adverseEffectRepository.saveAndFlush(adverseEffect);

        // Get all the adverseEffectList
        restAdverseEffectMockMvc.perform(get("/api/adverse-effects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adverseEffect.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAdverseEffect() throws Exception {
        // Initialize the database
        adverseEffectRepository.saveAndFlush(adverseEffect);

        // Get the adverseEffect
        restAdverseEffectMockMvc.perform(get("/api/adverse-effects/{id}", adverseEffect.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adverseEffect.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdverseEffect() throws Exception {
        // Get the adverseEffect
        restAdverseEffectMockMvc.perform(get("/api/adverse-effects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdverseEffect() throws Exception {
        // Initialize the database
        adverseEffectRepository.saveAndFlush(adverseEffect);
        int databaseSizeBeforeUpdate = adverseEffectRepository.findAll().size();

        // Update the adverseEffect
        AdverseEffect updatedAdverseEffect = adverseEffectRepository.findOne(adverseEffect.getId());
        updatedAdverseEffect
                .name(UPDATED_NAME);

        restAdverseEffectMockMvc.perform(put("/api/adverse-effects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAdverseEffect)))
            .andExpect(status().isOk());

        // Validate the AdverseEffect in the database
        List<AdverseEffect> adverseEffectList = adverseEffectRepository.findAll();
        assertThat(adverseEffectList).hasSize(databaseSizeBeforeUpdate);
        AdverseEffect testAdverseEffect = adverseEffectList.get(adverseEffectList.size() - 1);
        assertThat(testAdverseEffect.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAdverseEffect() throws Exception {
        int databaseSizeBeforeUpdate = adverseEffectRepository.findAll().size();

        // Create the AdverseEffect

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAdverseEffectMockMvc.perform(put("/api/adverse-effects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adverseEffect)))
            .andExpect(status().isCreated());

        // Validate the AdverseEffect in the database
        List<AdverseEffect> adverseEffectList = adverseEffectRepository.findAll();
        assertThat(adverseEffectList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAdverseEffect() throws Exception {
        // Initialize the database
        adverseEffectRepository.saveAndFlush(adverseEffect);
        int databaseSizeBeforeDelete = adverseEffectRepository.findAll().size();

        // Get the adverseEffect
        restAdverseEffectMockMvc.perform(delete("/api/adverse-effects/{id}", adverseEffect.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AdverseEffect> adverseEffectList = adverseEffectRepository.findAll();
        assertThat(adverseEffectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
