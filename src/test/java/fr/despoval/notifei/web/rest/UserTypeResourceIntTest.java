package fr.despoval.notifei.web.rest;

import fr.despoval.notifei.NotifApp;

import fr.despoval.notifei.domain.UserType;
import fr.despoval.notifei.repository.UserTypeRepository;

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
 * Test class for the UserTypeResource REST controller.
 *
 * @see UserTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotifApp.class)
public class UserTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_WEIGHT = 0D;
    private static final Double UPDATED_WEIGHT = 1D;

    @Inject
    private UserTypeRepository userTypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserTypeMockMvc;

    private UserType userType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserTypeResource userTypeResource = new UserTypeResource();
        ReflectionTestUtils.setField(userTypeResource, "userTypeRepository", userTypeRepository);
        this.restUserTypeMockMvc = MockMvcBuilders.standaloneSetup(userTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserType createEntity(EntityManager em) {
        UserType userType = new UserType()
                .name(DEFAULT_NAME)
                .weight(DEFAULT_WEIGHT);
        return userType;
    }

    @Before
    public void initTest() {
        userType = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserType() throws Exception {
        int databaseSizeBeforeCreate = userTypeRepository.findAll().size();

        // Create the UserType

        restUserTypeMockMvc.perform(post("/api/user-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userType)))
            .andExpect(status().isCreated());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeCreate + 1);
        UserType testUserType = userTypeList.get(userTypeList.size() - 1);
        assertThat(testUserType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserType.getWeight()).isEqualTo(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void createUserTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userTypeRepository.findAll().size();

        // Create the UserType with an existing ID
        UserType existingUserType = new UserType();
        existingUserType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserTypeMockMvc.perform(post("/api/user-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingUserType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTypeRepository.findAll().size();
        // set the field null
        userType.setName(null);

        // Create the UserType, which fails.

        restUserTypeMockMvc.perform(post("/api/user-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userType)))
            .andExpect(status().isBadRequest());

        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTypeRepository.findAll().size();
        // set the field null
        userType.setWeight(null);

        // Create the UserType, which fails.

        restUserTypeMockMvc.perform(post("/api/user-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userType)))
            .andExpect(status().isBadRequest());

        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserTypes() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get all the userTypeList
        restUserTypeMockMvc.perform(get("/api/user-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())));
    }

    @Test
    @Transactional
    public void getUserType() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);

        // Get the userType
        restUserTypeMockMvc.perform(get("/api/user-types/{id}", userType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserType() throws Exception {
        // Get the userType
        restUserTypeMockMvc.perform(get("/api/user-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserType() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);
        int databaseSizeBeforeUpdate = userTypeRepository.findAll().size();

        // Update the userType
        UserType updatedUserType = userTypeRepository.findOne(userType.getId());
        updatedUserType
                .name(UPDATED_NAME)
                .weight(UPDATED_WEIGHT);

        restUserTypeMockMvc.perform(put("/api/user-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserType)))
            .andExpect(status().isOk());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeUpdate);
        UserType testUserType = userTypeList.get(userTypeList.size() - 1);
        assertThat(testUserType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserType.getWeight()).isEqualTo(UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void updateNonExistingUserType() throws Exception {
        int databaseSizeBeforeUpdate = userTypeRepository.findAll().size();

        // Create the UserType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserTypeMockMvc.perform(put("/api/user-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userType)))
            .andExpect(status().isCreated());

        // Validate the UserType in the database
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserType() throws Exception {
        // Initialize the database
        userTypeRepository.saveAndFlush(userType);
        int databaseSizeBeforeDelete = userTypeRepository.findAll().size();

        // Get the userType
        restUserTypeMockMvc.perform(delete("/api/user-types/{id}", userType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserType> userTypeList = userTypeRepository.findAll();
        assertThat(userTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
