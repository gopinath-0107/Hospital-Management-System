package com.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class    HospitlSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitlSystemApplication.class, args);
        System.out.println("Hospital Management Application started");
        System.out.println("\n\n");
        System.err.println("PORT : localhost8080");
        System.err.println("documentation : "+"http://localhost:8080/swagger-ui/index.html#/");
        System.err.println("  *****    *******  *******       *****   *******    *****    ******   *******" );
        System.err.println(" *     *   *      *    *         *           *      *     *   *     *     *   " );
        System.err.println("*       *  *      *    *         *           *     *       *  *     *     *   " );
        System.err.println("*       *  *******     *          *****      *     *       *  ******      *   " );
        System.err.println("*********  *           *               *     *     *********  *   *       *   " );
        System.err.println("*       *  *           *               *     *     *       *  *    *      *   " );
        System.err.println("*       *  *        *******       *****      *     *       *  *     *     *   " );

    }

}
