package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Partido;
import com.mycompany.myapp.repository.PartidoRepository;
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
 * REST controller for managing Partido.
 */
@RestController
@RequestMapping("/api")
public class PartidoResource {

    private final Logger log = LoggerFactory.getLogger(PartidoResource.class);

    @Inject
    private PartidoRepository partidoRepository;

    /**
     * POST  /partidos -> Create a new partido.
     */
    @RequestMapping(value = "/partidos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Partido> create(@Valid @RequestBody Partido partido) throws URISyntaxException {
        log.debug("REST request to save Partido : {}", partido);
        if (partido.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new partido cannot already have an ID").body(null);
        }
        Partido result = partidoRepository.save(partido);
        return ResponseEntity.created(new URI("/api/partidos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("partido", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /partidos -> Updates an existing partido.
     */
    @RequestMapping(value = "/partidos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Partido> update(@Valid @RequestBody Partido partido) throws URISyntaxException {
        log.debug("REST request to update Partido : {}", partido);
        if (partido.getId() == null) {
            return create(partido);
        }
        Partido result = partidoRepository.save(partido);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("partido", partido.getId().toString()))
                .body(result);
    }

    /**
     * GET  /partidos -> get all the partidos.
     */
    @RequestMapping(value = "/partidos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Partido>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Partido> page = partidoRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/partidos", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /partidos/:id -> get the "id" partido.
     */
    @RequestMapping(value = "/partidos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Partido> get(@PathVariable Long id) {
        log.debug("REST request to get Partido : {}", id);
        return Optional.ofNullable(partidoRepository.findOne(id))
            .map(partido -> new ResponseEntity<>(
                partido,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /partidos/:id -> delete the "id" partido.
     */
    @RequestMapping(value = "/partidos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Partido : {}", id);
        partidoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("partido", id.toString())).build();
    }
}
