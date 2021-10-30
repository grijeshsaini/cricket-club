package com.grijesh.cricket.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Player DTO.
 *
 * @author Grijesh Saini
 */
public record Player(Long id, @NotEmpty String name, String shortName, @NotNull Roles role) {
}
