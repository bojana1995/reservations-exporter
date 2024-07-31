package com.reservation.service;

import com.reservation.model.Reservation;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing reservations.
 * Provides methods to retrieve and export reservation data.
 *
 * @author Bojana Samardzic
 */
public interface ReservationService {

    /**
     * Retrieves a list of reservations based on the provided asset and market IDs.
     *
     * @param assetId  the unique identifier of the asset
     * @param marketId the unique identifier of the market
     * @return a list of reservations matching the given asset ID and market ID
     */
    List<Reservation> getReservations(UUID assetId, UUID marketId);

    /**
     * Retrieves a list of reservations based on asset ID, market ID and a time range.
     * Optionally calculates the total of positive and negative values.
     *
     * @param assetId  the unique identifier of the asset
     * @param marketId the unique identifier of the market
     * @param from     the start of the time range
     * @param to       the end of the time range
     * @param total    if true, calculates the total of positive and negative values
     * @return a list of reservations matching the criteria
     */
    List<Reservation> getReservations(UUID assetId, UUID marketId, ZonedDateTime from, ZonedDateTime to, boolean total);

    /**
     * Exports reservations to a CSV file based on asset ID, market ID and a time range.
     * Optionally calculates the total of positive and negative values.
     *
     * @param assetId  the unique identifier of the asset
     * @param marketId the unique identifier of the market
     * @param from     the start of the time range
     * @param to       the end of the time range
     * @param total    if true, calculates the total of positive and negative values
     * @return a CSV formatted string of the reservations
     * @throws IOException if an I/O error occurs during export
     */
    String exportReservationsToCSV(UUID assetId, UUID marketId, ZonedDateTime from, ZonedDateTime to, boolean total) throws IOException;
}
