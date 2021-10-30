package com.grijesh.cricket.dto;

/**
 * DTO which store error details.
 *
 * @author Grijesh Saini
 */
public record ApiError(int status, String reason) {
}
