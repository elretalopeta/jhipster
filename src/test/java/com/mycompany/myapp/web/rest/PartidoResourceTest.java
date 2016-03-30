package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Partido;
import com.mycompany.myapp.repository.PartidoRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PartidoResource REST controller.
 *
 * @see PartidoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PartidoResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_HORA_INICIO = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_HORA_INICIO = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_HORA_INICIO_STR = dateTimeFormatter.print(DEFAULT_HORA_INICIO);

    private static final DateTime DEFAULT_HORA_FINAL = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_HORA_FINAL = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_HORA_FINAL_STR = dateTimeFormatter.print(DEFAULT_HORA_FINAL);
    private static final String DEFAULT_EQUIPO_LOCAL = "SAMPLE_TEXT";
    private static final String UPDATED_EQUIPO_LOCAL = "UPDATED_TEXT";
    private static final String DEFAULT_EQUIPO_VISITANTE = "SAMPLE_TEXT";
    private static final String UPDATED_EQUIPO_VISITANTE = "UPDATED_TEXT";
    private static final String DEFAULT_ARBITRO = "SAMPLE_TEXT";
    private static final String UPDATED_ARBITRO = "UPDATED_TEXT";

    private static final Integer DEFAULT_RESULTADO = 1;
    private static final Integer UPDATED_RESULTADO = 2;

    @Inject
    private PartidoRepository partidoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPartidoMockMvc;

    private Partido partido;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PartidoResource partidoResource = new PartidoResource();
        ReflectionTestUtils.setField(partidoResource, "partidoRepository", partidoRepository);
        this.restPartidoMockMvc = MockMvcBuilders.standaloneSetup(partidoResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        partido = new Partido();
        partido.setHora_inicio(DEFAULT_HORA_INICIO);
        partido.setHora_final(DEFAULT_HORA_FINAL);
        partido.setEquipo_local(DEFAULT_EQUIPO_LOCAL);
        partido.setEquipo_visitante(DEFAULT_EQUIPO_VISITANTE);
        partido.setArbitro(DEFAULT_ARBITRO);
        partido.setResultado(DEFAULT_RESULTADO);
    }

    @Test
    @Transactional
    public void createPartido() throws Exception {
        int databaseSizeBeforeCreate = partidoRepository.findAll().size();

        // Create the Partido

        restPartidoMockMvc.perform(post("/api/partidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partido)))
                .andExpect(status().isCreated());

        // Validate the Partido in the database
        List<Partido> partidos = partidoRepository.findAll();
        assertThat(partidos).hasSize(databaseSizeBeforeCreate + 1);
        Partido testPartido = partidos.get(partidos.size() - 1);
        assertThat(testPartido.getHora_inicio().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_HORA_INICIO);
        assertThat(testPartido.getHora_final().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_HORA_FINAL);
        assertThat(testPartido.getEquipo_local()).isEqualTo(DEFAULT_EQUIPO_LOCAL);
        assertThat(testPartido.getEquipo_visitante()).isEqualTo(DEFAULT_EQUIPO_VISITANTE);
        assertThat(testPartido.getArbitro()).isEqualTo(DEFAULT_ARBITRO);
        assertThat(testPartido.getResultado()).isEqualTo(DEFAULT_RESULTADO);
    }

    @Test
    @Transactional
    public void checkHora_inicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = partidoRepository.findAll().size();
        // set the field null
        partido.setHora_inicio(null);

        // Create the Partido, which fails.

        restPartidoMockMvc.perform(post("/api/partidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partido)))
                .andExpect(status().isBadRequest());

        List<Partido> partidos = partidoRepository.findAll();
        assertThat(partidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHora_finalIsRequired() throws Exception {
        int databaseSizeBeforeTest = partidoRepository.findAll().size();
        // set the field null
        partido.setHora_final(null);

        // Create the Partido, which fails.

        restPartidoMockMvc.perform(post("/api/partidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partido)))
                .andExpect(status().isBadRequest());

        List<Partido> partidos = partidoRepository.findAll();
        assertThat(partidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipo_localIsRequired() throws Exception {
        int databaseSizeBeforeTest = partidoRepository.findAll().size();
        // set the field null
        partido.setEquipo_local(null);

        // Create the Partido, which fails.

        restPartidoMockMvc.perform(post("/api/partidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partido)))
                .andExpect(status().isBadRequest());

        List<Partido> partidos = partidoRepository.findAll();
        assertThat(partidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEquipo_visitanteIsRequired() throws Exception {
        int databaseSizeBeforeTest = partidoRepository.findAll().size();
        // set the field null
        partido.setEquipo_visitante(null);

        // Create the Partido, which fails.

        restPartidoMockMvc.perform(post("/api/partidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partido)))
                .andExpect(status().isBadRequest());

        List<Partido> partidos = partidoRepository.findAll();
        assertThat(partidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArbitroIsRequired() throws Exception {
        int databaseSizeBeforeTest = partidoRepository.findAll().size();
        // set the field null
        partido.setArbitro(null);

        // Create the Partido, which fails.

        restPartidoMockMvc.perform(post("/api/partidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partido)))
                .andExpect(status().isBadRequest());

        List<Partido> partidos = partidoRepository.findAll();
        assertThat(partidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResultadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = partidoRepository.findAll().size();
        // set the field null
        partido.setResultado(null);

        // Create the Partido, which fails.

        restPartidoMockMvc.perform(post("/api/partidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partido)))
                .andExpect(status().isBadRequest());

        List<Partido> partidos = partidoRepository.findAll();
        assertThat(partidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPartidos() throws Exception {
        // Initialize the database
        partidoRepository.saveAndFlush(partido);

        // Get all the partidos
        restPartidoMockMvc.perform(get("/api/partidos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(partido.getId().intValue())))
                .andExpect(jsonPath("$.[*].hora_inicio").value(hasItem(DEFAULT_HORA_INICIO_STR)))
                .andExpect(jsonPath("$.[*].hora_final").value(hasItem(DEFAULT_HORA_FINAL_STR)))
                .andExpect(jsonPath("$.[*].equipo_local").value(hasItem(DEFAULT_EQUIPO_LOCAL.toString())))
                .andExpect(jsonPath("$.[*].equipo_visitante").value(hasItem(DEFAULT_EQUIPO_VISITANTE.toString())))
                .andExpect(jsonPath("$.[*].arbitro").value(hasItem(DEFAULT_ARBITRO.toString())))
                .andExpect(jsonPath("$.[*].resultado").value(hasItem(DEFAULT_RESULTADO)));
    }

    @Test
    @Transactional
    public void getPartido() throws Exception {
        // Initialize the database
        partidoRepository.saveAndFlush(partido);

        // Get the partido
        restPartidoMockMvc.perform(get("/api/partidos/{id}", partido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(partido.getId().intValue()))
            .andExpect(jsonPath("$.hora_inicio").value(DEFAULT_HORA_INICIO_STR))
            .andExpect(jsonPath("$.hora_final").value(DEFAULT_HORA_FINAL_STR))
            .andExpect(jsonPath("$.equipo_local").value(DEFAULT_EQUIPO_LOCAL.toString()))
            .andExpect(jsonPath("$.equipo_visitante").value(DEFAULT_EQUIPO_VISITANTE.toString()))
            .andExpect(jsonPath("$.arbitro").value(DEFAULT_ARBITRO.toString()))
            .andExpect(jsonPath("$.resultado").value(DEFAULT_RESULTADO));
    }

    @Test
    @Transactional
    public void getNonExistingPartido() throws Exception {
        // Get the partido
        restPartidoMockMvc.perform(get("/api/partidos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePartido() throws Exception {
        // Initialize the database
        partidoRepository.saveAndFlush(partido);

		int databaseSizeBeforeUpdate = partidoRepository.findAll().size();

        // Update the partido
        partido.setHora_inicio(UPDATED_HORA_INICIO);
        partido.setHora_final(UPDATED_HORA_FINAL);
        partido.setEquipo_local(UPDATED_EQUIPO_LOCAL);
        partido.setEquipo_visitante(UPDATED_EQUIPO_VISITANTE);
        partido.setArbitro(UPDATED_ARBITRO);
        partido.setResultado(UPDATED_RESULTADO);
        

        restPartidoMockMvc.perform(put("/api/partidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(partido)))
                .andExpect(status().isOk());

        // Validate the Partido in the database
        List<Partido> partidos = partidoRepository.findAll();
        assertThat(partidos).hasSize(databaseSizeBeforeUpdate);
        Partido testPartido = partidos.get(partidos.size() - 1);
        assertThat(testPartido.getHora_inicio().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testPartido.getHora_final().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_HORA_FINAL);
        assertThat(testPartido.getEquipo_local()).isEqualTo(UPDATED_EQUIPO_LOCAL);
        assertThat(testPartido.getEquipo_visitante()).isEqualTo(UPDATED_EQUIPO_VISITANTE);
        assertThat(testPartido.getArbitro()).isEqualTo(UPDATED_ARBITRO);
        assertThat(testPartido.getResultado()).isEqualTo(UPDATED_RESULTADO);
    }

    @Test
    @Transactional
    public void deletePartido() throws Exception {
        // Initialize the database
        partidoRepository.saveAndFlush(partido);

		int databaseSizeBeforeDelete = partidoRepository.findAll().size();

        // Get the partido
        restPartidoMockMvc.perform(delete("/api/partidos/{id}", partido.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Partido> partidos = partidoRepository.findAll();
        assertThat(partidos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
