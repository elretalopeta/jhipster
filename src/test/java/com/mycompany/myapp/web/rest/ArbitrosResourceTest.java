package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Arbitros;
import com.mycompany.myapp.repository.ArbitrosRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ArbitrosResource REST controller.
 *
 * @see ArbitrosResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ArbitrosResourceTest {

    private static final String DEFAULT_NOMBRE = "SAMPLE_TEXT";
    private static final String UPDATED_NOMBRE = "UPDATED_TEXT";

    @Inject
    private ArbitrosRepository arbitrosRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restArbitrosMockMvc;

    private Arbitros arbitros;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArbitrosResource arbitrosResource = new ArbitrosResource();
        ReflectionTestUtils.setField(arbitrosResource, "arbitrosRepository", arbitrosRepository);
        this.restArbitrosMockMvc = MockMvcBuilders.standaloneSetup(arbitrosResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        arbitros = new Arbitros();
        arbitros.setNombre(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createArbitros() throws Exception {
        int databaseSizeBeforeCreate = arbitrosRepository.findAll().size();

        // Create the Arbitros

        restArbitrosMockMvc.perform(post("/api/arbitross")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(arbitros)))
                .andExpect(status().isCreated());

        // Validate the Arbitros in the database
        List<Arbitros> arbitross = arbitrosRepository.findAll();
        assertThat(arbitross).hasSize(databaseSizeBeforeCreate + 1);
        Arbitros testArbitros = arbitross.get(arbitross.size() - 1);
        assertThat(testArbitros.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllArbitross() throws Exception {
        // Initialize the database
        arbitrosRepository.saveAndFlush(arbitros);

        // Get all the arbitross
        restArbitrosMockMvc.perform(get("/api/arbitross"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(arbitros.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getArbitros() throws Exception {
        // Initialize the database
        arbitrosRepository.saveAndFlush(arbitros);

        // Get the arbitros
        restArbitrosMockMvc.perform(get("/api/arbitross/{id}", arbitros.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(arbitros.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArbitros() throws Exception {
        // Get the arbitros
        restArbitrosMockMvc.perform(get("/api/arbitross/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArbitros() throws Exception {
        // Initialize the database
        arbitrosRepository.saveAndFlush(arbitros);

		int databaseSizeBeforeUpdate = arbitrosRepository.findAll().size();

        // Update the arbitros
        arbitros.setNombre(UPDATED_NOMBRE);
        

        restArbitrosMockMvc.perform(put("/api/arbitross")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(arbitros)))
                .andExpect(status().isOk());

        // Validate the Arbitros in the database
        List<Arbitros> arbitross = arbitrosRepository.findAll();
        assertThat(arbitross).hasSize(databaseSizeBeforeUpdate);
        Arbitros testArbitros = arbitross.get(arbitross.size() - 1);
        assertThat(testArbitros.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void deleteArbitros() throws Exception {
        // Initialize the database
        arbitrosRepository.saveAndFlush(arbitros);

		int databaseSizeBeforeDelete = arbitrosRepository.findAll().size();

        // Get the arbitros
        restArbitrosMockMvc.perform(delete("/api/arbitross/{id}", arbitros.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Arbitros> arbitross = arbitrosRepository.findAll();
        assertThat(arbitross).hasSize(databaseSizeBeforeDelete - 1);
    }
}
