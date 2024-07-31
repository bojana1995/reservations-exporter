package com.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for transferring reservation data.
 *
 * @author Bojana Samardzic
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    /**
     * Unique identifier for the reservation.
     */
    private Long id;

    /**
     * The date and time when the reservation was made.
     */
    private LocalDateTime timestamp;

    /**
     * Unique identifier for the asset associated with the reservation.
     */
    private UUID assetId;

    /**
     * Unique identifier for the market associated with the reservation.
     */
    private UUID marketId;

    /**
     * Unique identifier for the positive bid, if applicable.
     */
    private UUID positiveBidId;

    /**
     * Unique identifier for the negative bid, if applicable.
     */
    private UUID negativeBidId;

    /**
     * Value of the positive bid in kW.
     */
    private double positiveValue;

    /**
     * Positive capacity price in EUR/MW/h.
     */
    private double positiveCapacityPrice;

    /**
     * Positive energy price in EUR/MW/h.
     */
    private double positiveEnergyPrice;

    /**
     * Value of the negative bid in kW.
     */
    private double negativeValue;

    /**
     * Negative capacity price in EUR/MW/h.
     */
    private double negativeCapacityPrice;

    /**
     * Negative energy price in EUR/MW/h.
     */
    private double negativeEnergyPrice;

    /**
     * Date time of updated interval.
     */
    private LocalDateTime updatedAt;
}

