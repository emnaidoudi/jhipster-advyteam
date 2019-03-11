package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Projet01App;

import com.mycompany.myapp.config.SecurityBeanOverrideConfiguration;

import com.mycompany.myapp.domain.Intent;
import com.mycompany.myapp.repository.IntentRepository;
import com.mycompany.myapp.service.IntentService;
import com.mycompany.myapp.service.dto.IntentDTO;
import com.mycompany.myapp.service.mapper.IntentMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the IntentResource REST controller.
 *
 * @see IntentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Projet01App.class, SecurityBeanOverrideConfiguration.class})
public class IntentResourceIntTest {

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    @Autowired
    private IntentRepository intentRepository;

    @Autowired
    private IntentMapper intentMapper;

    @Autowired
    private IntentService intentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restIntentMockMvc;

    private Intent intent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IntentResource intentResource = new IntentResource(intentService);
        this.restIntentMockMvc = MockMvcBuilders.standaloneSetup(intentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intent createEntity() {
        Intent intent = new Intent()
            .tag(DEFAULT_TAG);
        return intent;
    }

    @Before
    public void initTest() {
        intentRepository.deleteAll();
        intent = createEntity();
    }

    @Test
    public void createIntent() throws Exception {
        int databaseSizeBeforeCreate = intentRepository.findAll().size();

        // Create the Intent
        IntentDTO intentDTO = intentMapper.toDto(intent);
        restIntentMockMvc.perform(post("/api/intents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(intentDTO)))
            .andExpect(status().isCreated());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeCreate + 1);
        Intent testIntent = intentList.get(intentList.size() - 1);
        assertThat(testIntent.getTag()).isEqualTo(DEFAULT_TAG);
    }

    @Test
    public void createIntentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = intentRepository.findAll().size();

        // Create the Intent with an existing ID
        intent.setId("existing_id");
        IntentDTO intentDTO = intentMapper.toDto(intent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntentMockMvc.perform(post("/api/intents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(intentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = intentRepository.findAll().size();
        // set the field null
        intent.setTag(null);

        // Create the Intent, which fails.
        IntentDTO intentDTO = intentMapper.toDto(intent);

        restIntentMockMvc.perform(post("/api/intents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(intentDTO)))
            .andExpect(status().isBadRequest());

        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllIntents() throws Exception {
        // Initialize the database
        intentRepository.save(intent);

        // Get all the intentList
        restIntentMockMvc.perform(get("/api/intents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intent.getId())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())));
    }

    @Test
    public void getIntent() throws Exception {
        // Initialize the database
        intentRepository.save(intent);

        // Get the intent
        restIntentMockMvc.perform(get("/api/intents/{id}", intent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(intent.getId()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG.toString()));
    }

    @Test
    public void getNonExistingIntent() throws Exception {
        // Get the intent
        restIntentMockMvc.perform(get("/api/intents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateIntent() throws Exception {
        // Initialize the database
        intentRepository.save(intent);
        int databaseSizeBeforeUpdate = intentRepository.findAll().size();

        // Update the intent
        Intent updatedIntent = intentRepository.findOne(intent.getId());
        updatedIntent
            .tag(UPDATED_TAG);
        IntentDTO intentDTO = intentMapper.toDto(updatedIntent);

        restIntentMockMvc.perform(put("/api/intents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(intentDTO)))
            .andExpect(status().isOk());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeUpdate);
        Intent testIntent = intentList.get(intentList.size() - 1);
        assertThat(testIntent.getTag()).isEqualTo(UPDATED_TAG);
    }

    @Test
    public void updateNonExistingIntent() throws Exception {
        int databaseSizeBeforeUpdate = intentRepository.findAll().size();

        // Create the Intent
        IntentDTO intentDTO = intentMapper.toDto(intent);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIntentMockMvc.perform(put("/api/intents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(intentDTO)))
            .andExpect(status().isCreated());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteIntent() throws Exception {
        // Initialize the database
        intentRepository.save(intent);
        int databaseSizeBeforeDelete = intentRepository.findAll().size();

        // Get the intent
        restIntentMockMvc.perform(delete("/api/intents/{id}", intent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intent.class);
        Intent intent1 = new Intent();
        intent1.setId("id1");
        Intent intent2 = new Intent();
        intent2.setId(intent1.getId());
        assertThat(intent1).isEqualTo(intent2);
        intent2.setId("id2");
        assertThat(intent1).isNotEqualTo(intent2);
        intent1.setId(null);
        assertThat(intent1).isNotEqualTo(intent2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntentDTO.class);
        IntentDTO intentDTO1 = new IntentDTO();
        intentDTO1.setId("id1");
        IntentDTO intentDTO2 = new IntentDTO();
        assertThat(intentDTO1).isNotEqualTo(intentDTO2);
        intentDTO2.setId(intentDTO1.getId());
        assertThat(intentDTO1).isEqualTo(intentDTO2);
        intentDTO2.setId("id2");
        assertThat(intentDTO1).isNotEqualTo(intentDTO2);
        intentDTO1.setId(null);
        assertThat(intentDTO1).isNotEqualTo(intentDTO2);
    }
}
