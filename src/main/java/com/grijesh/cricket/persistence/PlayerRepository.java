package com.grijesh.cricket.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access for the {@link PlayerEntity}, using {@link CrudRepository}.
 *
 * @author Grijesh Saini
 */
@Repository
public interface PlayerRepository extends CrudRepository<PlayerEntity, Long> {

    Optional<PlayerEntity> findByName(String name);

    List<PlayerEntity> findAll();
}
