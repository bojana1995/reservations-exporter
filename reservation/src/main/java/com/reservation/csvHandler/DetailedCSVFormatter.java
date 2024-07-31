package com.reservation.csvHandler;

import com.opencsv.CSVWriter;
import com.reservation.model.Reservation;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

/**
 * Service for formatting reservation data into detailed CSV format.
 * Implements the {@link CSVFormatter} interface to provide detailed formatting of reservation data.
 *
 * @author Bojana Samardzic
 */
@Service
public class DetailedCSVFormatter implements CSVFormatter {

    /**
     * Writes the header row for detailed CSV data.
     *
     * @param csvWriter the CSV writer to which the header will be written
     */
    @Override
    public void writeHeader(CSVWriter csvWriter) {
        csvWriter.writeNext(new String[]{"timestamp", "assetId", "marketId", "positiveBidId", "negativeBidId", "positiveValue", "positiveCapacityPrice", "positiveEnergyPrice", "negativeValue", "negativeCapacityPrice", "negativeEnergyPrice", "updatedAt"});
    }

    /**
     * Writes a reservation data row to the CSV writer in detailed format.
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
                reservation.getPositiveBidId().toString(),
                reservation.getNegativeBidId().toString(),
                String.valueOf(reservation.getPositiveValue() / 1000),
                String.valueOf(reservation.getPositiveCapacityPrice()),
                String.valueOf(reservation.getPositiveEnergyPrice()),
                String.valueOf(reservation.getNegativeValue() / 1000),
                String.valueOf(reservation.getNegativeCapacityPrice()),
                String.valueOf(reservation.getNegativeEnergyPrice()),
                reservation.getUpdatedAt().atZone(ZoneOffset.UTC).toString()
        });
    }
}
