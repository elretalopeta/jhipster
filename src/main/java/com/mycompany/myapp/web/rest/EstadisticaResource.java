package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Estadistica;
import com.mycompany.myapp.repository.EstadisticaRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Estadistica.
 */
@RestController
@RequestMapping("/api")
public class EstadisticaResource {

    private final Logger log = LoggerFactory.getLogger(EstadisticaResource.class);

    @Inject
    private EstadisticaRepository estadisticaRepository;

    /**
     * POST  /estadisticas -> Create a new estadistica.
     */
    @RequestMapping(value = "/estadisticas",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estadistica> create(@Valid @RequestBody Estadistica estadistica) throws URISyntaxException {
        log.debug("REST request to save Estadistica : {}", estadistica);
        if (estadistica.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new estadistica cannot already have an ID").body(null);
        }
        Estadistica result = estadisticaRepository.save(estadistica);
        return ResponseEntity.created(new URI("/api/estadisticas/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("estadistica", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /estadisticas -> Updates an existing estadistica.
     */
    @RequestMapping(value = "/estadisticas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estadistica> update(@Valid @RequestBody Estadistica estadistica) throws URISyntaxException {
        log.debug("REST request to update Estadistica : {}", estadistica);
        if (estadistica.getId() == null) {
            return create(estadistica);
        }
        Estadistica result = estadisticaRepository.save(estadistica);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("estadistica", estadistica.getId().toString()))
                .body(result);
    }

    /**
     * GET  /estadisticas -> get all the estadisticas.
     */
    @RequestMapping(value = "/estadisticas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Estadistica>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Estadistica> page = estadisticaRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/estadisticas", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /estadisticas/:id -> get the "id" estadistica.
     */
    @RequestMapping(value = "/estadisticas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estadistica> get(@PathVariable Long id) {
        log.debug("REST request to get Estadistica : {}", id);
        return Optional.ofNullable(estadisticaRepository.findOne(id))
            .map(estadistica -> new ResponseEntity<>(
                estadistica,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /estadisticas/:id -> delete the "id" estadistica.
     */
    @RequestMapping(value = "/estadisticas/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Estadistica : {}", id);
        estadisticaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("estadistica", id.toString())).build();
    }
}
