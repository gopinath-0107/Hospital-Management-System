package com.hospital.service;

import org.springframework.web.multipart.MultipartFile;

public interface SpeechToTextService {

    String convertAudioToText(MultipartFile audio);

}