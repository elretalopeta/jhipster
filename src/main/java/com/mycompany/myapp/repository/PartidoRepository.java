package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Partido;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Partido entity.
 */
public interface PartidoRepository extends JpaRepository<Partido,Long> {

}
