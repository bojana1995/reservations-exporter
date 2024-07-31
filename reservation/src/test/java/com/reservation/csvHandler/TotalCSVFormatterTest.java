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
 * Unit tests for {@link TotalCSVFormatter}.
 * Tests various scenarios for formatting reservation data into summarized CSV.
 *
 * @author Bojana Samardzic
 */
public class TotalCSVFormatterTest {

    private TotalCSVFormatter totalCSVFormatter;
    private CSVWriter csvWriter;
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        totalCSVFormatter = new TotalCSVFormatter();
        csvWriter = mock(CSVWriter.class);

        reservation = new Reservation();
        reservation.setTimestamp(ZonedDateTime.now().toLocalDateTime());
        reservation.setAssetId(UUID.randomUUID());
        reservation.setMarketId(UUID.randomUUID());
        reservation.setPositiveValue(5000);
        reservation.setNegativeValue(3000);
        reservation.setUpdatedAt(ZonedDateTime.now().toLocalDateTime());
    }

    /**
     * Tests the header row writing for summarized CSV data.
     */
    @Test
    public void testWriteHeader_Success() {
        totalCSVFormatter.writeHeader(csvWriter);

        ArgumentCaptor<String[]> headerCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(headerCaptor.capture());

        String[] expectedHeader = {
                "timestamp", "assetId", "marketId", "positiveValue", "negativeValue"
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
            totalCSVFormatter.writeHeader(csvWriter);
        });

        assertEquals("CSVWriter exception", exception.getMessage());
    }

    /**
     * Tests writing a reservation data row to the CSV writer in summarized format.
     */
    @Test
    public void testWriteRow_Success() {
        totalCSVFormatter.writeRow(csvWriter, reservation);

        ArgumentCaptor<String[]> rowCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(rowCaptor.capture());

        String[] expectedRow = {
                reservation.getTimestamp().atZone(ZoneOffset.UTC).toString(),
                reservation.getAssetId().toString(),
                reservation.getMarketId().toString(),
                String.valueOf(reservation.getPositiveValue() / 1000.0),
                String.valueOf(reservation.getNegativeValue() / 1000.0)
        };
        assertArrayEquals(expectedRow, rowCaptor.getValue());
    }

    /**
     * Tests writing a reservation data row with zero values.
     */
    @Test
    public void testWriteRow_ZeroValues_Success() {
        reservation.setPositiveValue(0);
        reservation.setNegativeValue(0);

        totalCSVFormatter.writeRow(csvWriter, reservation);

        ArgumentCaptor<String[]> rowCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(rowCaptor.capture());

        String[] expectedRow = {
                reservation.getTimestamp().atZone(ZoneOffset.UTC).toString(),
                reservation.getAssetId().toString(),
                reservation.getMarketId().toString(),
                "0.0",
                "0.0"
        };
        assertArrayEquals(expectedRow, rowCaptor.getValue());
    }

    /**
     * Tests writing a reservation data row with future timestamps.
     */
    @Test
    public void testWriteRow_FutureTimestamps_Success() {
        LocalDateTime futureTimestamp = ZonedDateTime.now().plusYears(1).toLocalDateTime();
        reservation.setTimestamp(futureTimestamp);
        reservation.setUpdatedAt(futureTimestamp);

        totalCSVFormatter.writeRow(csvWriter, reservation);

        ArgumentCaptor<String[]> rowCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(rowCaptor.capture());

        String[] expectedRow = {
                futureTimestamp.atZone(ZoneOffset.UTC).toString(),
                reservation.getAssetId().toString(),
                reservation.getMarketId().toString(),
                String.valueOf(reservation.getPositiveValue() / 1000.0),
                String.valueOf(reservation.getNegativeValue() / 1000.0)
        };
        assertArrayEquals(expectedRow, rowCaptor.getValue());
    }

    /**
     * Tests writing a reservation data row with past timestamps.
     */
    @Test
    public void testWriteRow_PastTimestamps_Success() {
        LocalDateTime pastTimestamp = ZonedDateTime.now().minusYears(1).toLocalDateTime();
        reservation.setTimestamp(pastTimestamp);
        reservation.setUpdatedAt(pastTimestamp);

        totalCSVFormatter.writeRow(csvWriter, reservation);

        ArgumentCaptor<String[]> rowCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(rowCaptor.capture());

        String[] expectedRow = {
                pastTimestamp.atZone(ZoneOffset.UTC).toString(),
                reservation.getAssetId().toString(),
                reservation.getMarketId().toString(),
                String.valueOf(reservation.getPositiveValue() / 1000.0),
                String.valueOf(reservation.getNegativeValue() / 1000.0)
        };
        assertArrayEquals(expectedRow, rowCaptor.getValue());
    }

    /**
     * Tests the handling of reservations with high positive and negative values.
     */
    @Test
    public void testWriteRow_HighValues_Success() {
        reservation.setPositiveValue(10000000);
        reservation.setNegativeValue(5000000);

        totalCSVFormatter.writeRow(csvWriter, reservation);

        ArgumentCaptor<String[]> rowCaptor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(1)).writeNext(rowCaptor.capture());

        String[] expectedRow = {
                reservation.getTimestamp().atZone(ZoneOffset.UTC).toString(),
                reservation.getAssetId().toString(),
                reservation.getMarketId().toString(),
                String.valueOf(reservation.getPositiveValue() / 1000.0),
                String.valueOf(reservation.getNegativeValue() / 1000.0)
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
            totalCSVFormatter.writeRow(csvWriter, reservation);
        });

        assertEquals("CSVWriter exception", exception.getMessage());
    }
}
