package com.reservation.csvHandler;

import com.opencsv.CSVWriter;
import com.reservation.model.Reservation;

/**
 * Interface for formatting reservation data into CSV format.
 * Provides methods to write headers and rows for CSV files.
 * Implementations of this interface can define specific formatting strategies.
 *
 * @author Bojana Samardzic
 */
public interface CSVFormatter {

    /**
     * Writes the header row to the provided CSV writer.
     *
     * @param csvWriter the CSV writer to which the header will be written
     */
    void writeHeader(CSVWriter csvWriter);

    /**
     * Writes a reservation data row to the provided CSV writer.
     *
     * @param csvWriter   the CSV writer to which the reservation data will be written
     * @param reservation the reservation object containing the data to be written
     */
    void writeRow(CSVWriter csvWriter, Reservation reservation);
}
