package com.hospital.dto;


import lombok.Data;

import org.springframework.web.multipart.MultipartFile;


@Data
public class UploadLabReportRequest {


    private Long labOrderId;


    private MultipartFile file;


    private String report;

}