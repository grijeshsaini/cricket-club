package com.grijesh.cricket.dto;

import javax.validation.constraints.NotEmpty;

/**
 * Player DTO.
 *
 * @author Grijesh Saini
 */
public record Player(Long id, @NotEmpty String name, String shortName, @NotEmpty String role) {
}
