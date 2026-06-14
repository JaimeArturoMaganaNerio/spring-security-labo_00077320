package com.server.app.repositories;

import com.server.app.entities.Inversion;
import com.server.app.entities.Portafolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InversionRepository extends JpaRepository<Inversion, Long> {
    List<Inversion> findByPortafolioAndEstado(Portafolio portafolio, String estado);
}