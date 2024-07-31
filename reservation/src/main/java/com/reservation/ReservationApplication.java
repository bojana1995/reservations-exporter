package com.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Reservation application.
 * This class contains the main method which is used to start the Spring Boot application.
 *
 * @author Bojana Samardzic
 */
@SpringBootApplication
public class ReservationApplication {

    /**
     * The main method that launches the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ReservationApplication.class, args);
    }

}
