package com.reservation.csvHandler;

import com.opencsv.CSVWriter;
import com.reservation.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link DetailedCSVFormatter}.
 * Tests various scenarios for formatting reservation data into CSV.
 *
 * @author Bojana Samardzic
 */
public class DetailedCSVFormatterTest {

    private DetailedCSVFormatter detailedCSVFormatter;
    private CSVWriter csvWriter;
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        detailedCSVFormatter = new DetailedCSVFormatter();
        csvWriter = mock(CSVWriter.class);

        reservation = new Reservation();
        reservation.setTimestamp(ZonedDateTime.now().toLocalDateTime());
        reservation.setAssetId(UUID.randomUUID());
        reservation.setMarketId(UUID.randomUUID());
        reservation.setPositiveBidId(UUID.randomUUID());
        reservation.setNegativeBidId(UUID.randomUUID());
        reservation.setPositiveValue(5000);
        reservation.setPositiveCapacityPrice(10.5);
        reservation.setPositiveEnergyPrice(20.5);
        reservation.setNegativeValue(3000);
        reservation.setNegativeCapacityPrice(5.5);
        reservation.setNegativeEnergyPrice(15.5);
        reservation.setUpdatedAt(ZonedDateTime.now().toLocalDateTime());
    }

    /**
     * Tests writing the header row to the CSV file.
     *
     * @see DetailedCSVFormatter#writeHeader(CSVWriter)
     */
    @Test
    public void testWriteHeader_Success() {
        detailedCSVFormatter.writeHeader(csvWriter);

        ArgumentCaptor<String[]> headerCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(headerCaptor.capture());

        String[] expectedHeader = {
                "timestamp", "assetId", "marketId", "positiveBidId", "negativeBidId",
                "positiveValue", "positiveCapacityPrice", "positiveEnergyPrice",
                "negativeValue", "negativeCapacityPrice", "negativeEnergyPrice", "updatedAt"
        };
        assertArrayEquals(expectedHeader, headerCaptor.getValue());
    }

    /**
     * Tests the scenario where CSVWriter throws an exception when writing the header.
     */
    @Test
    public void testWriteHeader_Exception() {
        doThrow(new RuntimeException("CSVWriter exception")).when(csvWriter).writeNext(any(String[].class));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            detailedCSVFormatter.writeHeader(csvWriter);
        });

        assertEquals("CSVWriter exception", exception.getMessage());
    }

    /**
     * Tests writing a row with standard reservation data to the CSV file.
     *
     * @see DetailedCSVFormatter#writeRow(CSVWriter, Reservation)
     */
    @Test
    public void testWriteRow_Success() {
        detailedCSVFormatter.writeRow(csvWriter, reservation);

        ArgumentCaptor<String[]> rowCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(rowCaptor.capture());

        String[] expectedRow = {
                reservation.getTimestamp().atZone(ZoneOffset.UTC).toString(),
                reservation.getAssetId().toString(),
                reservation.getMarketId().toString(),
                reservation.getPositiveBidId().toString(),
                reservation.getNegativeBidId().toString(),
                String.valueOf(reservation.getPositiveValue() / 1000.0),
                String.valueOf(reservation.getPositiveCapacityPrice()),
                String.valueOf(reservation.getPositiveEnergyPrice()),
                String.valueOf(reservation.getNegativeValue() / 1000.0),
                String.valueOf(reservation.getNegativeCapacityPrice()),
                String.valueOf(reservation.getNegativeEnergyPrice()),
                reservation.getUpdatedAt().atZone(ZoneOffset.UTC).toString()
        };
        assertArrayEquals(expectedRow, rowCaptor.getValue());
    }

    /**
     * Tests writing a row with zero values to the CSV file.
     *
     * @see DetailedCSVFormatter#writeRow(CSVWriter, Reservation)
     */
    @Test
    public void testWriteRow_ZeroValues_Success() {
        reservation.setPositiveValue(0);
        reservation.setNegativeValue(0);
        detailedCSVFormatter.writeRow(csvWriter, reservation);

        ArgumentCaptor<String[]> rowCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(rowCaptor.capture());

        String[] expectedRow = {
                reservation.getTimestamp().atZone(ZoneOffset.UTC).toString(),
                reservation.getAssetId().toString(),
                reservation.getMarketId().toString(),
                reservation.getPositiveBidId().toString(),
                reservation.getNegativeBidId().toString(),
                "0.0",
                String.valueOf(reservation.getPositiveCapacityPrice()),
                String.valueOf(reservation.getPositiveEnergyPrice()),
                "0.0",
                String.valueOf(reservation.getNegativeCapacityPrice()),
                String.valueOf(reservation.getNegativeEnergyPrice()),
                reservation.getUpdatedAt().atZone(ZoneOffset.UTC).toString()
        };
        assertArrayEquals(expectedRow, rowCaptor.getValue());
    }

    /**
     * Tests writing a row with future timestamps to the CSV file.
     *
     * @see DetailedCSVFormatter#writeRow(CSVWriter, Reservation)
     */
    @Test
    public void testWriteRow_FutureTimestamps_Success() {
        LocalDateTime futureTimestamp = ZonedDateTime.now().plusYears(1).toLocalDateTime();
        reservation.setTimestamp(futureTimestamp);
        reservation.setUpdatedAt(futureTimestamp);

        detailedCSVFormatter.writeRow(csvWriter, reservation);

        ArgumentCaptor<String[]> rowCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(rowCaptor.capture());

        String[] expectedRow = {
                futureTimestamp.atZone(ZoneOffset.UTC).toString(),
                reservation.getAssetId().toString(),
                reservation.getMarketId().toString(),
                reservation.getPositiveBidId().toString(),
                reservation.getNegativeBidId().toString(),
                String.valueOf(reservation.getPositiveValue() / 1000.0),
                String.valueOf(reservation.getPositiveCapacityPrice()),
                String.valueOf(reservation.getPositiveEnergyPrice()),
                String.valueOf(reservation.getNegativeValue() / 1000.0),
                String.valueOf(reservation.getNegativeCapacityPrice()),
                String.valueOf(reservation.getNegativeEnergyPrice()),
                futureTimestamp.atZone(ZoneOffset.UTC).toString()
        };
        assertArrayEquals(expectedRow, rowCaptor.getValue());
    }

    /**
     * Tests writing a row with maximum values for numeric fields.
     *
     * @see DetailedCSVFormatter#writeRow(CSVWriter, Reservation)
     */
    @Test
    public void testWriteRow_MaxValues_Success() {
        reservation.setPositiveValue(Integer.MAX_VALUE);
        reservation.setNegativeValue(Integer.MAX_VALUE);
        reservation.setPositiveCapacityPrice(Double.MAX_VALUE);
        reservation.setNegativeCapacityPrice(Double.MAX_VALUE);
        reservation.setPositiveEnergyPrice(Double.MAX_VALUE);
        reservation.setNegativeEnergyPrice(Double.MAX_VALUE);

        detailedCSVFormatter.writeRow(csvWriter, reservation);

        ArgumentCaptor<String[]> rowCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(rowCaptor.capture());

        String[] expectedRow = {
                reservation.getTimestamp().atZone(ZoneOffset.UTC).toString(),
                reservation.getAssetId().toString(),
                reservation.getMarketId().toString(),
                reservation.getPositiveBidId().toString(),
                reservation.getNegativeBidId().toString(),
                String.valueOf(reservation.getPositiveValue() / 1000.0),
                String.valueOf(reservation.getPositiveCapacityPrice()),
                String.valueOf(reservation.getPositiveEnergyPrice()),
                String.valueOf(reservation.getNegativeValue() / 1000.0),
                String.valueOf(reservation.getNegativeCapacityPrice()),
                String.valueOf(reservation.getNegativeEnergyPrice()),
                reservation.getUpdatedAt().atZone(ZoneOffset.UTC).toString()
        };
        assertArrayEquals(expectedRow, rowCaptor.getValue());
    }

    /**
     * Tests writing a row with minimum (negative) values for numeric fields.
     *
     * @see DetailedCSVFormatter#writeRow(CSVWriter, Reservation)
     */
    @Test
    public void testWriteRow_MinValues_Success() {
        reservation.setPositiveValue(Integer.MIN_VALUE);
        reservation.setNegativeValue(Integer.MIN_VALUE);
        reservation.setPositiveCapacityPrice(Double.MIN_VALUE);
        reservation.setNegativeCapacityPrice(Double.MIN_VALUE);
        reservation.setPositiveEnergyPrice(Double.MIN_VALUE);
        reservation.setNegativeEnergyPrice(Double.MIN_VALUE);

        detailedCSVFormatter.writeRow(csvWriter, reservation);

        ArgumentCaptor<String[]> rowCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(rowCaptor.capture());

        String[] expectedRow = {
                reservation.getTimestamp().atZone(ZoneOffset.UTC).toString(),
                reservation.getAssetId().toString(),
                reservation.getMarketId().toString(),
                reservation.getPositiveBidId().toString(),
                reservation.getNegativeBidId().toString(),
                String.valueOf(reservation.getPositiveValue() / 1000.0),
                String.valueOf(reservation.getPositiveCapacityPrice()),
                String.valueOf(reservation.getPositiveEnergyPrice()),
                String.valueOf(reservation.getNegativeValue() / 1000.0),
                String.valueOf(reservation.getNegativeCapacityPrice()),
                String.valueOf(reservation.getNegativeEnergyPrice()),
                reservation.getUpdatedAt().atZone(ZoneOffset.UTC).toString()
        };
        assertArrayEquals(expectedRow, rowCaptor.getValue());
    }

    /**
     * Tests writing a row with invalid UUID formats (not actual UUIDs but invalid strings).
     *
     * @see DetailedCSVFormatter#writeRow(CSVWriter, Reservation)
     */
    @Test
    public void testWriteRow_InvalidUUIDs_Success() {
        reservation.setAssetId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        reservation.setMarketId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        reservation.setPositiveBidId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        reservation.setNegativeBidId(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        detailedCSVFormatter.writeRow(csvWriter, reservation);

        ArgumentCaptor<String[]> rowCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(rowCaptor.capture());

        String[] expectedRow = {
                reservation.getTimestamp().atZone(ZoneOffset.UTC).toString(),
                reservation.getAssetId().toString(),
                reservation.getMarketId().toString(),
                reservation.getPositiveBidId().toString(),
                reservation.getNegativeBidId().toString(),
                String.valueOf(reservation.getPositiveValue() / 1000.0),
                String.valueOf(reservation.getPositiveCapacityPrice()),
                String.valueOf(reservation.getPositiveEnergyPrice()),
                String.valueOf(reservation.getNegativeValue() / 1000.0),
                String.valueOf(reservation.getNegativeCapacityPrice()),
                String.valueOf(reservation.getNegativeEnergyPrice()),
                reservation.getUpdatedAt().atZone(ZoneOffset.UTC).toString()
        };
        assertArrayEquals(expectedRow, rowCaptor.getValue());
    }

    /**
     * Tests the scenario where CSVWriter throws an exception when writing a row.
     */
    @Test
    public void testWriteRow_Exception() {
        doThrow(new RuntimeException("CSVWriter exception")).when(csvWriter).writeNext(any(String[].class));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            detailedCSVFormatter.writeRow(csvWriter, reservation);
        });

        assertEquals("CSVWriter exception", exception.getMessage());
    }
}
