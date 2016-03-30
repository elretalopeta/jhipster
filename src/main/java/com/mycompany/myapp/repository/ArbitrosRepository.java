package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Arbitros;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Arbitros entity.
 */
public interface ArbitrosRepository extends JpaRepository<Arbitros,Long> {

}
