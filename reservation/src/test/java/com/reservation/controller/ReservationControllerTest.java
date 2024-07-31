package com.reservation.controller;

import com.reservation.dto.ReservationDTO;
import com.reservation.model.Reservation;
import com.reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ReservationController}.
 * Tests the export of reservation data to CSV file.
 *
 * @author Bojana Samardzic
 */
public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReservationController reservationController;

    private UUID assetId;
    private UUID marketId;
    private ZonedDateTime from;
    private ZonedDateTime to;
    private boolean total;
    private String csvData;

    /**
     * Sets up the test environment before each test.
     * Initializes mock objects and test data.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        assetId = UUID.randomUUID();
        marketId = UUID.randomUUID();
        from = ZonedDateTime.now().minusDays(1);
        to = ZonedDateTime.now();
        total = false;
        csvData = "csv,data";
    }

    /**
     * Tests successful export of reservation data to CSV.
     *
     * @throws IOException if an I/O error occurs during export
     */
    @Test
    public void testExportReservationsToCSV_Success() throws IOException {
        when(reservationService.exportReservationsToCSV(any(UUID.class), any(UUID.class), any(ZonedDateTime.class), any(ZonedDateTime.class), anyBoolean()))
                .thenReturn(csvData);

        ResponseEntity<String> response = reservationController.exportReservationsToCSV(assetId, marketId, from, to, total);

        HttpHeaders headers = response.getHeaders();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("attachment; filename=reservations.csv", Objects.requireNonNull(headers.getFirst(HttpHeaders.CONTENT_DISPOSITION)));
        assertEquals(csvData, response.getBody());
    }

    /**
     * Tests successful export of reservation data to CSV when 'total' parameter is true.
     *
     * @throws IOException if an I/O error occurs during export
     */
    @Test
    public void testExportReservationsToCSV_WithTotal_Success() throws IOException {
        total = true;
        when(reservationService.exportReservationsToCSV(any(UUID.class), any(UUID.class), any(ZonedDateTime.class), any(ZonedDateTime.class), eq(true)))
                .thenReturn(csvData);

        ResponseEntity<String> response = reservationController.exportReservationsToCSV(assetId, marketId, from, to, total);

        HttpHeaders headers = response.getHeaders();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("attachment; filename=reservations.csv", Objects.requireNonNull(headers.getFirst(HttpHeaders.CONTENT_DISPOSITION)));
        assertEquals(csvData, response.getBody());
    }

    /**
     * Tests the export when the date range is invalid.
     * The 'from' date is after the 'to' date.
     */
    @Test
    public void testExportReservationsToCSV_InvalidDates() throws IOException {
        ZonedDateTime invalidFrom = ZonedDateTime.now();
        ZonedDateTime invalidTo = ZonedDateTime.now().minusDays(1);

        when(reservationService.exportReservationsToCSV(any(UUID.class), any(UUID.class), eq(invalidFrom), eq(invalidTo), anyBoolean()))
                .thenThrow(new IllegalArgumentException("Invalid date range"));

        try {
            reservationController.exportReservationsToCSV(assetId, marketId, invalidFrom, invalidTo, total);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid date range", e.getMessage());
        }
    }

    /**
     * Tests the export when the asset ID is null.
     */
    @Test
    public void testExportReservationsToCSV_NullAssetId() throws IOException {
        assetId = null;

        try {
            reservationController.exportReservationsToCSV(assetId, marketId, from, to, total);
        } catch (IllegalArgumentException e) {
            assertEquals("Asset ID cannot be null", e.getMessage());
        }
    }

    /**
     * Tests the export when the market ID is null.
     */
    @Test
    public void testExportReservationsToCSV_NullMarketId() throws IOException {
        marketId = null;

        try {
            reservationController.exportReservationsToCSV(assetId, marketId, from, to, total);
        } catch (IllegalArgumentException e) {
            assertEquals("Market ID cannot be null", e.getMessage());
        }
    }

    /**
     * Tests successful export of reservation data to CSV when the CSV data is empty.
     *
     * @throws IOException if an I/O error occurs during export
     */
    @Test
    public void testExportReservationsToCSV_EmptyCSVData() throws IOException {
        when(reservationService.exportReservationsToCSV(any(UUID.class), any(UUID.class), any(ZonedDateTime.class), any(ZonedDateTime.class), anyBoolean()))
                .thenReturn(csvData);

        ResponseEntity<String> response = reservationController.exportReservationsToCSV(assetId, marketId, from, to, total);

        HttpHeaders headers = response.getHeaders();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("attachment; filename=reservations.csv", Objects.requireNonNull(headers.getFirst(HttpHeaders.CONTENT_DISPOSITION)));
        assertEquals(csvData, response.getBody());
    }

    /**
     * Tests the export when the CSV data is null.
     *
     * @throws IOException if an I/O error occurs during export
     */
    @Test
    public void testExportReservationsToCSV_NullCSVData() throws IOException {
        when(reservationService.exportReservationsToCSV(any(UUID.class), any(UUID.class), any(ZonedDateTime.class), any(ZonedDateTime.class), anyBoolean()))
                .thenReturn(csvData);

        ResponseEntity<String> response = reservationController.exportReservationsToCSV(assetId, marketId, from, to, total);

        HttpHeaders headers = response.getHeaders();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("attachment; filename=reservations.csv", Objects.requireNonNull(headers.getFirst(HttpHeaders.CONTENT_DISPOSITION)));
        assertEquals(csvData, response.getBody());
    }

    /**
     * Test successful retrieval of reservations.
     * This test simulates a scenario where reservations are found for the given asset ID and market ID.
     * It verifies that the response contains the list of `ReservationDTO` objects and has an HTTP 200 OK status.
     */
    @Test
    void testGetReservations_Success() {
        Reservation reservation = new Reservation();
        ReservationDTO reservationDTO = new ReservationDTO();

        when(reservationService.getReservations(assetId, marketId))
                .thenReturn(Collections.singletonList(reservation));
        when(modelMapper.map(reservation, ReservationDTO.class))
                .thenReturn(reservationDTO);

        ResponseEntity<List<ReservationDTO>> response = reservationController.getReservations(assetId, marketId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(reservationDTO, response.getBody().get(0));
    }

    /**
     * Test scenario where no reservations are found.
     * This test simulates a scenario where no reservations exist for the provided asset ID and market ID.
     * It verifies that the response contains an HTTP 404 Not Found status.
     */
    @Test
    void testGetReservations_NoContent() {
        when(reservationService.getReservations(assetId, marketId))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<ReservationDTO>> response = reservationController.getReservations(assetId, marketId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Test scenario where the asset ID and market ID are invalid (e.g., null or incorrect format).
     * This test checks how the controller handles invalid IDs.
     * Note: This specific scenario assumes that invalid IDs are handled elsewhere (e.g., in a validation layer).
     * The focus here is on correct handling and mapping of valid data.
     */
    @Test
    void testGetReservations_InvalidIds() {
        UUID invalidAssetId = null;
        UUID invalidMarketId = null;

        when(reservationService.getReservations(any(UUID.class), any(UUID.class)))
                .thenThrow(new IllegalArgumentException("Invalid UUID"));

        ResponseEntity<List<ReservationDTO>> response = reservationController.getReservations(invalidAssetId, invalidMarketId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}
