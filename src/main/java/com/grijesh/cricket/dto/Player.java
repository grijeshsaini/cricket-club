package com.grijesh.cricket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;


import jakarta.validation.constraints.NotNull;

/**
 * Player DTO.
 *
 * @author Grijesh Saini
 */
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
public record Player(Long id, @NotEmpty String name, String shortName, @NotNull Roles role) {
}
