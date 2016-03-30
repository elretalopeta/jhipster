package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Liga;
import com.mycompany.myapp.repository.LigaRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Liga.
 */
@RestController
@RequestMapping("/api")
public class LigaResource {

    private final Logger log = LoggerFactory.getLogger(LigaResource.class);

    @Inject
    private LigaRepository ligaRepository;

    /**
     * POST  /ligas -> Create a new liga.
     */
    @RequestMapping(value = "/ligas",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Liga> create(@RequestBody Liga liga) throws URISyntaxException {
        log.debug("REST request to save Liga : {}", liga);
        if (liga.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new liga cannot already have an ID").body(null);
        }
        Liga result = ligaRepository.save(liga);
        return ResponseEntity.created(new URI("/api/ligas/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("liga", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /ligas -> Updates an existing liga.
     */
    @RequestMapping(value = "/ligas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Liga> update(@RequestBody Liga liga) throws URISyntaxException {
        log.debug("REST request to update Liga : {}", liga);
        if (liga.getId() == null) {
            return create(liga);
        }
        Liga result = ligaRepository.save(liga);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("liga", liga.getId().toString()))
                .body(result);
    }

    /**
     * GET  /ligas -> get all the ligas.
     */
    @RequestMapping(value = "/ligas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Liga>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Liga> page = ligaRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ligas", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ligas/:id -> get the "id" liga.
     */
    @RequestMapping(value = "/ligas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Liga> get(@PathVariable Long id) {
        log.debug("REST request to get Liga : {}", id);
        return Optional.ofNullable(ligaRepository.findOne(id))
            .map(liga -> new ResponseEntity<>(
                liga,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ligas/:id -> delete the "id" liga.
     */
    @RequestMapping(value = "/ligas/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Liga : {}", id);
        ligaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("liga", id.toString())).build();
    }
}
