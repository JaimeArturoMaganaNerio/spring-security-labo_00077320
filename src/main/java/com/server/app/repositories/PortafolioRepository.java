package com.server.app.repositories;

import com.server.app.entities.Portafolio;
import com.server.app.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortafolioRepository extends JpaRepository<Portafolio, Long> {
    Page<Portafolio> findByUsuario(User usuario, Pageable pageable);
}