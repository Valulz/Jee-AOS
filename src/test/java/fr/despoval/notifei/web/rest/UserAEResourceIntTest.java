package fr.despoval.notifei.web.rest;

import fr.despoval.notifei.NotifApp;

import fr.despoval.notifei.domain.UserAE;
import fr.despoval.notifei.domain.UserType;
import fr.despoval.notifei.repository.UserAERepository;

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
 * Test class for the UserAEResource REST controller.
 *
 * @see UserAEResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotifApp.class)
public class UserAEResourceIntTest {

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    @Inject
    private UserAERepository userAERepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserAEMockMvc;

    private UserAE userAE;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserAEResource userAEResource = new UserAEResource();
        ReflectionTestUtils.setField(userAEResource, "userAERepository", userAERepository);
        this.restUserAEMockMvc = MockMvcBuilders.standaloneSetup(userAEResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAE createEntity(EntityManager em) {
        UserAE userAE = new UserAE()
                .region(DEFAULT_REGION);
        // Add required entity
        UserType type = UserTypeResourceIntTest.createEntity(em);
        em.persist(type);
        em.flush();
        userAE.setType(type);
        return userAE;
    }

    @Before
    public void initTest() {
        userAE = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserAE() throws Exception {
        int databaseSizeBeforeCreate = userAERepository.findAll().size();

        // Create the UserAE

        restUserAEMockMvc.perform(post("/api/user-aes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userAE)))
            .andExpect(status().isCreated());

        // Validate the UserAE in the database
        List<UserAE> userAEList = userAERepository.findAll();
        assertThat(userAEList).hasSize(databaseSizeBeforeCreate + 1);
        UserAE testUserAE = userAEList.get(userAEList.size() - 1);
        assertThat(testUserAE.getRegion()).isEqualTo(DEFAULT_REGION);
    }

    @Test
    @Transactional
    public void createUserAEWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userAERepository.findAll().size();

        // Create the UserAE with an existing ID
        UserAE existingUserAE = new UserAE();
        existingUserAE.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAEMockMvc.perform(post("/api/user-aes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingUserAE)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserAE> userAEList = userAERepository.findAll();
        assertThat(userAEList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserAES() throws Exception {
        // Initialize the database
        userAERepository.saveAndFlush(userAE);

        // Get all the userAEList
        restUserAEMockMvc.perform(get("/api/user-aes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAE.getId().intValue())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())));
    }

    @Test
    @Transactional
    public void getUserAE() throws Exception {
        // Initialize the database
        userAERepository.saveAndFlush(userAE);

        // Get the userAE
        restUserAEMockMvc.perform(get("/api/user-aes/{id}", userAE.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userAE.getId().intValue()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserAE() throws Exception {
        // Get the userAE
        restUserAEMockMvc.perform(get("/api/user-aes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserAE() throws Exception {
        // Initialize the database
        userAERepository.saveAndFlush(userAE);
        int databaseSizeBeforeUpdate = userAERepository.findAll().size();

        // Update the userAE
        UserAE updatedUserAE = userAERepository.findOne(userAE.getId());
        updatedUserAE
                .region(UPDATED_REGION);

        restUserAEMockMvc.perform(put("/api/user-aes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserAE)))
            .andExpect(status().isOk());

        // Validate the UserAE in the database
        List<UserAE> userAEList = userAERepository.findAll();
        assertThat(userAEList).hasSize(databaseSizeBeforeUpdate);
        UserAE testUserAE = userAEList.get(userAEList.size() - 1);
        assertThat(testUserAE.getRegion()).isEqualTo(UPDATED_REGION);
    }

    @Test
    @Transactional
    public void updateNonExistingUserAE() throws Exception {
        int databaseSizeBeforeUpdate = userAERepository.findAll().size();

        // Create the UserAE

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserAEMockMvc.perform(put("/api/user-aes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userAE)))
            .andExpect(status().isCreated());

        // Validate the UserAE in the database
        List<UserAE> userAEList = userAERepository.findAll();
        assertThat(userAEList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserAE() throws Exception {
        // Initialize the database
        userAERepository.saveAndFlush(userAE);
        int databaseSizeBeforeDelete = userAERepository.findAll().size();

        // Get the userAE
        restUserAEMockMvc.perform(delete("/api/user-aes/{id}", userAE.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserAE> userAEList = userAERepository.findAll();
        assertThat(userAEList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
