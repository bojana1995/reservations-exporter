package com.reservation.service;

import com.opencsv.CSVWriter;
import com.reservation.csvHandler.TotalCSVFormatter;
import com.reservation.model.Reservation;
import com.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ReservationServiceImpl}.
 * Tests various scenarios for retrieving and exporting reservation data.
 *
 * @author Bojana Samardzic
 */
public class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TotalCSVFormatter totalCSVFormatter;

    @InjectMocks
    private ReservationServiceImpl reservationServiceImpl;

    private UUID assetId;
    private UUID marketId;
    private ZonedDateTime from;
    private ZonedDateTime to;

    /**
     * Sets up the test environment by initializing mocks and test data.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        assetId = UUID.randomUUID();
        marketId = UUID.randomUUID();
        from = ZonedDateTime.now().minusDays(1);
        to = ZonedDateTime.now().plusDays(1);
    }

    /**
     * Test successful retrieval and conversion of reservations.
     * This test simulates a scenario where reservations are found for the given asset ID and market ID,
     * and the values are successfully converted from kW to MW.
     * It verifies that the returned list of reservations contains the converted values.
     */
    @Test
    void testGetReservations_AssetIdAndMarketId_Success() {
        Reservation reservation = new Reservation();
        reservation.setPositiveValue(1000);
        reservation.setNegativeValue(2000);

        when(reservationRepository.findByAssetIdAndMarketId(assetId, marketId))
                .thenReturn(Collections.singletonList(reservation));

        double expectedPositiveValue = 1.0;
        double expectedNegativeValue = 2.0;

        List<Reservation> result = reservationServiceImpl.getReservations(assetId, marketId);

        assertNotNull(result);
        assertEquals(1, result.size());
        Reservation resultReservation = result.get(0);
        assertEquals(expectedPositiveValue, resultReservation.getPositiveValue());
        assertEquals(expectedNegativeValue, resultReservation.getNegativeValue());
    }

    /**
     * Test retrieval with no reservations found.
     * This test simulates a scenario where no reservations are found for the given asset ID and market ID.
     * It verifies that an empty list is returned.
     */
    @Test
    void testGetReservations_AssetIdAndMarketId_NoContent() {
        when(reservationRepository.findByAssetIdAndMarketId(assetId, marketId))
                .thenReturn(Collections.emptyList());

        List<Reservation> result = reservationServiceImpl.getReservations(assetId, marketId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test retrieval with null asset ID and market ID.
     * This test simulates a scenario where null values are provided as asset ID and market ID.
     * It verifies that the service handles null inputs correctly.
     */
    @Test
    void testGetReservations_AssetIdAndMarketId_NullParameters() {
        UUID nullAssetId = null;
        UUID nullMarketId = null;

        when(reservationRepository.findByAssetIdAndMarketId(nullAssetId, nullMarketId))
                .thenReturn(Collections.emptyList());

        List<Reservation> result = reservationServiceImpl.getReservations(nullAssetId, nullMarketId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests successful retrieval of reservations without aggregation.
     */
    @Test
    public void testGetReservations_Success() {
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation = new Reservation();
        reservation.setAssetId(assetId);
        reservation.setMarketId(marketId);
        reservation.setTimestamp(ZonedDateTime.now().toLocalDateTime());
        reservations.add(reservation);

        when(reservationRepository.findByAssetIdAndMarketIdAndTimestampBetween(any(UUID.class), any(UUID.class), any(), any()))
                .thenReturn(reservations);

        List<Reservation> result = reservationServiceImpl.getReservations(assetId, marketId, from, to, false);

        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
    }

    /**
     * Tests successful retrieval and aggregation of reservations.
     */
    @Test
    public void testGetReservations_WithTotal_Success() {
        List<Reservation> reservations = new ArrayList<>();
        LocalDateTime fixedTimestamp = LocalDateTime.of(2024, 7, 31, 12, 0);

        Reservation reservation1 = new Reservation();
        reservation1.setAssetId(assetId);
        reservation1.setMarketId(marketId);
        reservation1.setTimestamp(fixedTimestamp);
        reservation1.setPositiveValue(100);
        reservation1.setNegativeValue(50);
        reservations.add(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setAssetId(assetId);
        reservation2.setMarketId(marketId);
        reservation2.setTimestamp(fixedTimestamp);
        reservation2.setPositiveValue(200);
        reservation2.setNegativeValue(100);
        reservations.add(reservation2);

        Reservation aggregatedReservation = new Reservation();
        aggregatedReservation.setAssetId(assetId);
        aggregatedReservation.setMarketId(marketId);
        aggregatedReservation.setTimestamp(fixedTimestamp);
        aggregatedReservation.setPositiveValue(300);
        aggregatedReservation.setNegativeValue(150);

        when(reservationRepository.findByAssetIdAndMarketIdAndTimestampBetween(any(UUID.class), any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(reservations);

        List<Reservation> result = reservationServiceImpl.getReservations(assetId, marketId, ZonedDateTime.of(fixedTimestamp, ZoneOffset.UTC), ZonedDateTime.of(fixedTimestamp.plusHours(1), ZoneOffset.UTC), true);

        assertEquals(1, result.size());
        assertEquals(aggregatedReservation.getPositiveValue(), result.get(0).getPositiveValue());
        assertEquals(aggregatedReservation.getNegativeValue(), result.get(0).getNegativeValue());
    }

    /**
     * Tests the behavior when no reservations are found and aggregation is not required.
     */
    @Test
    public void testGetReservations_EmptyList_NoAggregation_Success() {
        when(reservationRepository.findByAssetIdAndMarketIdAndTimestampBetween(any(UUID.class), any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        List<Reservation> result = reservationServiceImpl.getReservations(assetId, marketId, from, to, false);

        assertEquals(0, result.size());
    }

    /**
     * Tests the behavior when no reservations are found but aggregation is requested.
     */
    @Test
    public void testGetReservations_EmptyList_WithAggregation_Success() {
        when(reservationRepository.findByAssetIdAndMarketIdAndTimestampBetween(any(UUID.class), any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        List<Reservation> result = reservationServiceImpl.getReservations(assetId, marketId, from, to, true);

        assertEquals(0, result.size());
    }

    /**
     * Tests successful retrieval of a single reservation without aggregation.
     */
    @Test
    public void testGetReservations_SingleReservation_NoAggregation_Success() {
        List<Reservation> reservations = new ArrayList<>();
        LocalDateTime fixedTimestamp = LocalDateTime.of(2024, 7, 31, 12, 0);

        Reservation reservation = new Reservation();
        reservation.setAssetId(assetId);
        reservation.setMarketId(marketId);
        reservation.setTimestamp(fixedTimestamp);
        reservation.setPositiveValue(100);
        reservation.setNegativeValue(50);
        reservations.add(reservation);

        when(reservationRepository.findByAssetIdAndMarketIdAndTimestampBetween(any(UUID.class), any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(reservations);

        List<Reservation> result = reservationServiceImpl.getReservations(assetId, marketId, from, to, false);

        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
    }

    /**
     * Tests export of reservations to CSV when there are no reservations and aggregation is enabled.
     */
    @Test
    public void testExportReservationsToCSV_NoReservations_WithTotal_Success() throws IOException {
        when(reservationRepository.findByAssetIdAndMarketIdAndTimestampBetween(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        doNothing().when(totalCSVFormatter).writeHeader(any(CSVWriter.class));
        doNothing().when(totalCSVFormatter).writeRow(any(CSVWriter.class), any());

        String result = reservationServiceImpl.exportReservationsToCSV(assetId, marketId, from, to, true);

        String expectedCsv = "";
        assertEquals(expectedCsv, result);

        verify(totalCSVFormatter).writeHeader(any(CSVWriter.class));
        verify(totalCSVFormatter, never()).writeRow(any(CSVWriter.class), any());
    }

    /**
     * Tests export of reservations to CSV when an IOException occurs during export.
     */
    @Test
    public void testExportReservationsToCSV_IOException() throws IOException {
        when(reservationRepository.findByAssetIdAndMarketIdAndTimestampBetween(any(), any(), any(), any()))
                .thenReturn(List.of());

        doAnswer(invocation -> {
            throw new IOException("Simulated IO Exception");
        }).when(totalCSVFormatter).writeHeader(any(CSVWriter.class));

        try {
            reservationServiceImpl.exportReservationsToCSV(assetId, marketId, from, to, true);
        } catch (IOException e) {
            assertEquals("Simulated IO Exception", e.getMessage());
        }
    }

    /**
     * Tests successful aggregation with multiple reservations having the same timestamp.
     */
    @Test
    public void testAggregateReservations_MultipleReservations_Success() {
        LocalDateTime fixedTimestamp = LocalDateTime.of(2024, 7, 31, 12, 0);

        List<Reservation> reservations = new ArrayList<>();

        Reservation reservation1 = new Reservation();
        reservation1.setAssetId(assetId);
        reservation1.setMarketId(marketId);
        reservation1.setTimestamp(fixedTimestamp);
        reservation1.setPositiveValue(100);
        reservation1.setNegativeValue(50);
        reservations.add(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setAssetId(assetId);
        reservation2.setMarketId(marketId);
        reservation2.setTimestamp(fixedTimestamp);
        reservation2.setPositiveValue(200);
        reservation2.setNegativeValue(100);
        reservations.add(reservation2);

        Reservation aggregatedReservation = new Reservation();
        aggregatedReservation.setAssetId(assetId);
        aggregatedReservation.setMarketId(marketId);
        aggregatedReservation.setTimestamp(fixedTimestamp);
        aggregatedReservation.setPositiveValue(300);
        aggregatedReservation.setNegativeValue(150);

        List<Reservation> result = reservationServiceImpl.aggregateReservations(reservations);

        assertEquals(1, result.size());
        assertEquals(aggregatedReservation.getPositiveValue(), result.get(0).getPositiveValue());
        assertEquals(aggregatedReservation.getNegativeValue(), result.get(0).getNegativeValue());
        assertEquals(aggregatedReservation.getTimestamp(), result.get(0).getTimestamp());
    }

    /**
     * Tests aggregation of reservations with different timestamps but the same asset ID and market ID.
     */
    @Test
    public void testAggregateReservations_DifferentTimestamps_Success() {
        List<Reservation> reservations = new ArrayList<>();
        LocalDateTime timestamp1 = LocalDateTime.of(2024, 7, 31, 12, 0);
        LocalDateTime timestamp2 = LocalDateTime.of(2024, 7, 31, 13, 0);

        Reservation reservation1 = new Reservation();
        reservation1.setAssetId(assetId);
        reservation1.setMarketId(marketId);
        reservation1.setTimestamp(timestamp1);
        reservation1.setPositiveValue(100);
        reservation1.setNegativeValue(50);
        reservations.add(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setAssetId(assetId);
        reservation2.setMarketId(marketId);
        reservation2.setTimestamp(timestamp2);
        reservation2.setPositiveValue(200);
        reservation2.setNegativeValue(100);
        reservations.add(reservation2);

        List<Reservation> result = reservationServiceImpl.aggregateReservations(reservations);

        assertEquals(2, result.size());
    }

    /**
     * Tests aggregation when reservations have the same timestamp, asset ID, and market ID but no reservations are present.
     */
    @Test
    public void testAggregateReservations_EmptyList_Success() {
        List<Reservation> reservations = new ArrayList<>();

        List<Reservation> result = reservationServiceImpl.aggregateReservations(reservations);

        assertEquals(0, result.size());
    }

    /**
     * Tests aggregation of reservations with different asset IDs and market IDs but the same timestamp.
     */
    @Test
    public void testAggregateReservations_DifferentAssetMarketIDs_Success() {
        List<Reservation> reservations = new ArrayList<>();
        LocalDateTime timestamp = LocalDateTime.of(2024, 7, 31, 12, 0);
        UUID assetId1 = UUID.randomUUID();
        UUID assetId2 = UUID.randomUUID();
        UUID marketId1 = UUID.randomUUID();
        UUID marketId2 = UUID.randomUUID();

        Reservation reservation1 = new Reservation();
        reservation1.setAssetId(assetId1);
        reservation1.setMarketId(marketId1);
        reservation1.setTimestamp(timestamp);
        reservation1.setPositiveValue(100);
        reservation1.setNegativeValue(50);
        reservations.add(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setAssetId(assetId2);
        reservation2.setMarketId(marketId2);
        reservation2.setTimestamp(timestamp);
        reservation2.setPositiveValue(200);
        reservation2.setNegativeValue(100);
        reservations.add(reservation2);

        List<Reservation> result = reservationServiceImpl.aggregateReservations(reservations);

        assertEquals(2, result.size());
    }

    /**
     * Test successful conversion of positive and negative values from kW to MW.
     * This test verifies that the method correctly converts positive and negative values in a list of reservations
     * from kilowatts (kW) to megawatts (MW) by dividing the values by 1000.
     */
    @Test
    void testConvertKWToMW_Success() {
        Reservation reservation = new Reservation();
        reservation.setPositiveValue(1000);
        reservation.setNegativeValue(2000);

        List<Reservation> reservations = Collections.singletonList(reservation);

        List<Reservation> convertedReservations = reservationServiceImpl.convertKWToMW(reservations);

        assertNotNull(convertedReservations);
        assertEquals(1, convertedReservations.size());
        Reservation resultReservation = convertedReservations.get(0);
        assertEquals(1.0, resultReservation.getPositiveValue());
        assertEquals(2.0, resultReservation.getNegativeValue());
    }

    /**
     * Test conversion with an empty list.
     * This test verifies that the method handles an empty list correctly and returns an empty list.
     */
    @Test
    void testConvertKWToMW_EmptyList() {
        List<Reservation> reservations = Collections.emptyList();

        List<Reservation> convertedReservations = reservationServiceImpl.convertKWToMW(reservations);

        assertNotNull(convertedReservations);
        assertTrue(convertedReservations.isEmpty());
    }

    /**
     * Test conversion with reservations having zero values.
     * This test verifies that the method correctly handles reservations where the positive and negative values are zero.
     * The values should remain zero after conversion.
     */
    @Test
    void testConvertKWToMW_ZeroValues() {
        Reservation reservation = new Reservation();
        reservation.setPositiveValue(0);
        reservation.setNegativeValue(0);

        List<Reservation> reservations = Collections.singletonList(reservation);

        List<Reservation> convertedReservations = reservationServiceImpl.convertKWToMW(reservations);

        assertNotNull(convertedReservations);
        assertEquals(1, convertedReservations.size());
        Reservation resultReservation = convertedReservations.get(0);
        assertEquals(0.0, resultReservation.getPositiveValue());
        assertEquals(0.0, resultReservation.getNegativeValue());
    }
}
