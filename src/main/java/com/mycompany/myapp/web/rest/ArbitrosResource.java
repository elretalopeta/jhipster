package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Arbitros;
import com.mycompany.myapp.repository.ArbitrosRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Arbitros.
 */
@RestController
@RequestMapping("/api")
public class ArbitrosResource {

    private final Logger log = LoggerFactory.getLogger(ArbitrosResource.class);

    @Inject
    private ArbitrosRepository arbitrosRepository;

    /**
     * POST  /arbitross -> Create a new arbitros.
     */
    @RequestMapping(value = "/arbitross",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Arbitros> create(@RequestBody Arbitros arbitros) throws URISyntaxException {
        log.debug("REST request to save Arbitros : {}", arbitros);
        if (arbitros.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new arbitros cannot already have an ID").body(null);
        }
        Arbitros result = arbitrosRepository.save(arbitros);
        return ResponseEntity.created(new URI("/api/arbitross/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("arbitros", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /arbitross -> Updates an existing arbitros.
     */
    @RequestMapping(value = "/arbitross",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Arbitros> update(@RequestBody Arbitros arbitros) throws URISyntaxException {
        log.debug("REST request to update Arbitros : {}", arbitros);
        if (arbitros.getId() == null) {
            return create(arbitros);
        }
        Arbitros result = arbitrosRepository.save(arbitros);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("arbitros", arbitros.getId().toString()))
                .body(result);
    }

    /**
     * GET  /arbitross -> get all the arbitross.
     */
    @RequestMapping(value = "/arbitross",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Arbitros>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("equipo-is-null".equals(filter)) {
            log.debug("REST request to get all Arbitross where equipo is null");
            return new ResponseEntity<>(StreamSupport
                .stream(arbitrosRepository.findAll().spliterator(), false)
                .filter(arbitros -> arbitros.getEquipo() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        
        Page<Arbitros> page = arbitrosRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/arbitross", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /arbitross/:id -> get the "id" arbitros.
     */
    @RequestMapping(value = "/arbitross/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Arbitros> get(@PathVariable Long id) {
        log.debug("REST request to get Arbitros : {}", id);
        return Optional.ofNullable(arbitrosRepository.findOne(id))
            .map(arbitros -> new ResponseEntity<>(
                arbitros,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /arbitross/:id -> delete the "id" arbitros.
     */
    @RequestMapping(value = "/arbitross/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Arbitros : {}", id);
        arbitrosRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("arbitros", id.toString())).build();
    }
}
