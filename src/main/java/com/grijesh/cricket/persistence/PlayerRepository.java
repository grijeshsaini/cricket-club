package com.grijesh.cricket.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Data access for the {@link PlayerEntity}, using {@link CrudRepository}.
 *
 * @author Grijesh Saini
 */
@Repository
public interface PlayerRepository extends CrudRepository<PlayerEntity, Long> {

}
