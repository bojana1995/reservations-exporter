package com.reservation.service;

import com.opencsv.CSVWriter;
import com.reservation.csvHandler.CSVFormatter;
import com.reservation.csvHandler.DetailedCSVFormatter;
import com.reservation.csvHandler.TotalCSVFormatter;
import com.reservation.model.Reservation;
import com.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of the {@link ReservationService} interface.
 * Provides methods to retrieve and export reservation data.
 * Uses {@link ReservationRepository} for database operations and different CSV formatters for exporting data.
 *
 * @author Bojana Samardzic
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TotalCSVFormatter totalCSVFormatter;
    private final DetailedCSVFormatter detailedCSVFormatter;

    /**
     * Constructs a new instance of {@code ReservationServiceImpl} with the specified dependencies.
     *
     * @param reservationRepository the repository for managing reservations
     * @param totalCSVFormatter     the formatter for exporting aggregated reservation data
     * @param detailedCSVFormatter  the formatter for exporting detailed reservation data
     */
    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, TotalCSVFormatter totalCSVFormatter, DetailedCSVFormatter detailedCSVFormatter) {
        this.reservationRepository = reservationRepository;
        this.totalCSVFormatter = totalCSVFormatter;
        this.detailedCSVFormatter = detailedCSVFormatter;
    }

    /**
     * Retrieves a list of reservations based on the provided asset ID and market ID.
     *
     * @param assetId  the unique identifier of the asset
     * @param marketId the unique identifier of the market
     * @return a list of reservations matching the given asset ID and market ID
     */
    @Override
    public List<Reservation> getReservations(UUID assetId, UUID marketId) {
        List<Reservation> reservations = reservationRepository.findByAssetIdAndMarketId(assetId, marketId);
        reservations = convertKWToMW(reservations);

        return reservations;
    }

    /**
     * Retrieves a list of reservations based on asset ID, market ID and a time range.
     * Optionally aggregates the total of positive and negative values.
     *
     * @param assetId  the unique identifier of the asset
     * @param marketId the unique identifier of the market
     * @param from     the start of the time range
     * @param to       the end of the time range
     * @param total    if true, aggregates the total of positive and negative values
     * @return a list of reservations matching the criteria
     */
    @Override
    public List<Reservation> getReservations(UUID assetId, UUID marketId, ZonedDateTime from, ZonedDateTime to, boolean total) {
        LocalDateTime fromLocal = from.toLocalDateTime();
        LocalDateTime toLocal = to.toLocalDateTime();

        List<Reservation> reservations = reservationRepository.findByAssetIdAndMarketIdAndTimestampBetween(assetId, marketId, fromLocal, toLocal);

        if (total) {
            return aggregateReservations(reservations);
        }

        return reservations;
    }

    /**
     * Exports reservations to a CSV file based on asset ID, market ID and a time range.
     * Optionally aggregates the total of positive and negative values.
     *
     * @param assetId  the unique identifier of the asset
     * @param marketId the unique identifier of the market
     * @param from     the start of the time range
     * @param to       the end of the time range
     * @param total    if true, aggregates the total of positive and negative values
     * @return a CSV formatted string of the reservations
     * @throws IOException if an I/O error occurs during export
     */
    @Override
    public String exportReservationsToCSV(UUID assetId, UUID marketId, ZonedDateTime from, ZonedDateTime to, boolean total) throws IOException {
        List<Reservation> reservations = getReservations(assetId, marketId, from, to, total);

        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        CSVFormatter csvFormatter = total ? totalCSVFormatter : detailedCSVFormatter;
        csvFormatter.writeHeader(csvWriter);

        for (Reservation reservation : reservations) {
            csvFormatter.writeRow(csvWriter, reservation);
        }

        csvWriter.close();
        return stringWriter.toString();
    }

    /**
     * Aggregates reservations by summing up positive and negative values for the same timestamp, asset ID and market ID.
     *
     * @param reservations the list of reservations to be aggregated
     * @return a list of aggregated reservations
     */
    List<Reservation> aggregateReservations(List<Reservation> reservations) {
        Map<String, Reservation> aggregatedMap = new HashMap<>();

        for (Reservation reservation : reservations) {
            String key = reservation.getTimestamp().toString() + reservation.getAssetId().toString() + reservation.getMarketId().toString();

            if (aggregatedMap.containsKey(key)) {
                Reservation existing = aggregatedMap.get(key);
                existing.setPositiveValue(existing.getPositiveValue() + reservation.getPositiveValue());
                existing.setNegativeValue(existing.getNegativeValue() + reservation.getNegativeValue());
            } else {
                aggregatedMap.put(key, reservation);
            }
        }

        return new ArrayList<>(aggregatedMap.values());
    }

    /**
     * Converts the positive and negative values of a list of reservations from kilowatts (kW) to megawatts (MW).
     * This method iterates over each reservation in the provided list and divides the `positiveValue` and
     * `negativeValue` fields by 1000 to convert them from kW to MW. It then returns the updated list of reservations.
     *
     * @param reservations the list of reservations with values in kilowatts (kW)
     * @return the list of reservations with values converted to megawatts (MW)
     */
    List<Reservation> convertKWToMW(List<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            reservation.setPositiveValue(reservation.getPositiveValue() / 1000);
            reservation.setNegativeValue(reservation.getNegativeValue() / 1000);
        }

        return reservations;
    }
}
