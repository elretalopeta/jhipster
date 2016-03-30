package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Estadistica;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Estadistica entity.
 */
public interface EstadisticaRepository extends JpaRepository<Estadistica,Long> {

}
