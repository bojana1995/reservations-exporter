package com.reservation.controller;

import com.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * REST controller for managing reservations.
 * Provides an endpoint to export reservation data to a CSV file.
 *
 * @author Bojana Samardzic
 */
@RestController
@RequestMapping("/api/v1/flexibility/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Constructs a new instance of {@code ReservationController} with the specified {@link ReservationService}.
     *
     * @param reservationService the service for managing reservations
     */
    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Exports reservations to a CSV file based on the provided asset ID, market ID, and time range.
     * Optionally aggregates the total of positive and negative values.
     * Returns a CSV file as an attachment or appropriate HTTP error responses based on the input validation and internal processing.
     *
     * @param assetId  the unique identifier of the asset (must be a valid UUID)
     * @param marketId the unique identifier of the market (must be a valid UUID)
     * @param from     the start of the time range for filtering reservations (must be in ISO date-time format)
     * @param to       the end of the time range for filtering reservations (must be in ISO date-time format)
     * @param total    if true, aggregates the total of positive and negative values
     * @return a ResponseEntity containing the CSV formatted string of reservations or an error message
     * - HTTP 200 OK with CSV data if successful
     * - HTTP 400 Bad Request if the 'from' date is after the 'to' date
     * - HTTP 404 Not Found if no data is found for the given parameters
     * - HTTP 500 Internal Server Error if an IO or unexpected error occurs
     */
    @GetMapping("/{assetId}/market/{marketId}/export")
    public ResponseEntity<String> exportReservationsToCSV(
            @PathVariable UUID assetId,
            @PathVariable UUID marketId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to,
            @RequestParam(required = false, defaultValue = "false") boolean total) {
        try {
            if (from.isAfter(to)) {
                return ResponseEntity.badRequest().body("Invalid date range: 'from' cannot be after 'to'");
            }

            String csvData = reservationService.exportReservationsToCSV(assetId, marketId, from, to, total);

            if (csvData == null || csvData.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=reservations.csv")
                    .body(csvData);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while exporting reservations: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
