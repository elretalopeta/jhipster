package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Liga;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Liga entity.
 */
public interface LigaRepository extends JpaRepository<Liga,Long> {

}
