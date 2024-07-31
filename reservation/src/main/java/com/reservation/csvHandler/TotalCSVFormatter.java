package com.reservation.csvHandler;

import com.opencsv.CSVWriter;
import com.reservation.model.Reservation;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

/**
 * Service for formatting reservation data into a summarized CSV format.
 * Implements the {@link CSVFormatter} interface to provide aggregated formatting of reservation data.
 *
 * @author Bojana Samardzic
 */
@Service
public class TotalCSVFormatter implements CSVFormatter {

    /**
     * Writes the header row for summarized CSV data.
     *
     * @param csvWriter the CSV writer to which the header will be written
     */
    @Override
    public void writeHeader(CSVWriter csvWriter) {
        csvWriter.writeNext(new String[]{"timestamp", "assetId", "marketId", "positiveValue", "negativeValue"});
    }

    /**
     * Writes a reservation data row to the CSV writer in summarized format.
     * Converts positive and negative values from kW to MW.
     *
     * @param csvWriter   the CSV writer to which the reservation data will be written
     * @param reservation the reservation object containing the data to be written
     */
    @Override
    public void writeRow(CSVWriter csvWriter, Reservation reservation) {
        csvWriter.writeNext(new String[]{
                reservation.getTimestamp().atZone(ZoneOffset.UTC).toString(),
                reservation.getAssetId().toString(),
                reservation.getMarketId().toString(),
                String.valueOf(reservation.getPositiveValue() / 1000),
                String.valueOf(reservation.getNegativeValue() / 1000)
        });
    }
}
