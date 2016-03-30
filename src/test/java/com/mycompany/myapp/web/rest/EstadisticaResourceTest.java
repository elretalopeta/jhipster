package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Estadistica;
import com.mycompany.myapp.repository.EstadisticaRepository;

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
 * Test class for the EstadisticaResource REST controller.
 *
 * @see EstadisticaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EstadisticaResourceTest {


    private static final Integer DEFAULT_ASISTENCIAS = 0;
    private static final Integer UPDATED_ASISTENCIAS = 1;

    private static final Integer DEFAULT_CANASTAS = 0;
    private static final Integer UPDATED_CANASTAS = 1;

    private static final Integer DEFAULT_FALTAS = 0;
    private static final Integer UPDATED_FALTAS = 1;

    @Inject
    private EstadisticaRepository estadisticaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restEstadisticaMockMvc;

    private Estadistica estadistica;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EstadisticaResource estadisticaResource = new EstadisticaResource();
        ReflectionTestUtils.setField(estadisticaResource, "estadisticaRepository", estadisticaRepository);
        this.restEstadisticaMockMvc = MockMvcBuilders.standaloneSetup(estadisticaResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        estadistica = new Estadistica();
        estadistica.setAsistencias(DEFAULT_ASISTENCIAS);
        estadistica.setCanastas(DEFAULT_CANASTAS);
        estadistica.setFaltas(DEFAULT_FALTAS);
    }

    @Test
    @Transactional
    public void createEstadistica() throws Exception {
        int databaseSizeBeforeCreate = estadisticaRepository.findAll().size();

        // Create the Estadistica

        restEstadisticaMockMvc.perform(post("/api/estadisticas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estadistica)))
                .andExpect(status().isCreated());

        // Validate the Estadistica in the database
        List<Estadistica> estadisticas = estadisticaRepository.findAll();
        assertThat(estadisticas).hasSize(databaseSizeBeforeCreate + 1);
        Estadistica testEstadistica = estadisticas.get(estadisticas.size() - 1);
        assertThat(testEstadistica.getAsistencias()).isEqualTo(DEFAULT_ASISTENCIAS);
        assertThat(testEstadistica.getCanastas()).isEqualTo(DEFAULT_CANASTAS);
        assertThat(testEstadistica.getFaltas()).isEqualTo(DEFAULT_FALTAS);
    }

    @Test
    @Transactional
    public void getAllEstadisticas() throws Exception {
        // Initialize the database
        estadisticaRepository.saveAndFlush(estadistica);

        // Get all the estadisticas
        restEstadisticaMockMvc.perform(get("/api/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(estadistica.getId().intValue())))
                .andExpect(jsonPath("$.[*].asistencias").value(hasItem(DEFAULT_ASISTENCIAS)))
                .andExpect(jsonPath("$.[*].canastas").value(hasItem(DEFAULT_CANASTAS)))
                .andExpect(jsonPath("$.[*].faltas").value(hasItem(DEFAULT_FALTAS)));
    }

    @Test
    @Transactional
    public void getEstadistica() throws Exception {
        // Initialize the database
        estadisticaRepository.saveAndFlush(estadistica);

        // Get the estadistica
        restEstadisticaMockMvc.perform(get("/api/estadisticas/{id}", estadistica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(estadistica.getId().intValue()))
            .andExpect(jsonPath("$.asistencias").value(DEFAULT_ASISTENCIAS))
            .andExpect(jsonPath("$.canastas").value(DEFAULT_CANASTAS))
            .andExpect(jsonPath("$.faltas").value(DEFAULT_FALTAS));
    }

    @Test
    @Transactional
    public void getNonExistingEstadistica() throws Exception {
        // Get the estadistica
        restEstadisticaMockMvc.perform(get("/api/estadisticas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstadistica() throws Exception {
        // Initialize the database
        estadisticaRepository.saveAndFlush(estadistica);

		int databaseSizeBeforeUpdate = estadisticaRepository.findAll().size();

        // Update the estadistica
        estadistica.setAsistencias(UPDATED_ASISTENCIAS);
        estadistica.setCanastas(UPDATED_CANASTAS);
        estadistica.setFaltas(UPDATED_FALTAS);
        

        restEstadisticaMockMvc.perform(put("/api/estadisticas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estadistica)))
                .andExpect(status().isOk());

        // Validate the Estadistica in the database
        List<Estadistica> estadisticas = estadisticaRepository.findAll();
        assertThat(estadisticas).hasSize(databaseSizeBeforeUpdate);
        Estadistica testEstadistica = estadisticas.get(estadisticas.size() - 1);
        assertThat(testEstadistica.getAsistencias()).isEqualTo(UPDATED_ASISTENCIAS);
        assertThat(testEstadistica.getCanastas()).isEqualTo(UPDATED_CANASTAS);
        assertThat(testEstadistica.getFaltas()).isEqualTo(UPDATED_FALTAS);
    }

    @Test
    @Transactional
    public void deleteEstadistica() throws Exception {
        // Initialize the database
        estadisticaRepository.saveAndFlush(estadistica);

		int databaseSizeBeforeDelete = estadisticaRepository.findAll().size();

        // Get the estadistica
        restEstadisticaMockMvc.perform(delete("/api/estadisticas/{id}", estadistica.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Estadistica> estadisticas = estadisticaRepository.findAll();
        assertThat(estadisticas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
