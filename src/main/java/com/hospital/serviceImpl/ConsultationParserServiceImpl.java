package com.hospital.serviceImpl;

import com.hospital.dto.VoiceConsultationResponse;
import com.hospital.service.ConsultationParserService;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ConsultationParserServiceImpl implements ConsultationParserService {

    @Override
    public VoiceConsultationResponse parse(Long appointmentId,
                                           String transcription) {

        VoiceConsultationResponse response = new VoiceConsultationResponse();

        response.setAppointmentId(appointmentId);
        response.setTranscription(transcription);

        response.setBloodPressure(extractBloodPressure(transcription));
        response.setTemperature(extractTemperature(transcription));
        response.setPulse(extractPulse(transcription));
        response.setDiagnosis(extractDiagnosis(transcription));

        return response;
    }

    /**
     * Example:
     * Blood pressure is 120 over 80
     * Blood pressure 120/80
     */
    private String extractBloodPressure(String text) {

        Pattern pattern = Pattern.compile(
                "(?i)blood pressure\\s*(?:is)?\\s*(\\d{2,3})\\s*(?:over|/)\\s*(\\d{2,3})"
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1) + "/" + matcher.group(2);
        }

        return "";
    }

    /**
     * Example:
     * Temperature is 98.6
     * Temperature 100.4
     */
    private Double extractTemperature(String text) {

        Pattern pattern = Pattern.compile(
                "(?i)temperature\\s*(?:is)?\\s*(\\d+(?:\\.\\d+)?)"
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }

        return null;
    }

    /**
     * Example:
     * Pulse is 72
     * Pulse 80
     */
    private Integer extractPulse(String text) {

        Pattern pattern = Pattern.compile(
                "(?i)pulse\\s*(?:is)?\\s*(\\d{2,3})"
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return null;
    }

    /**
     * Example:
     * Diagnosis Viral Fever
     * Diagnosis is Viral Fever
     */
    private String extractDiagnosis(String text) {

        Pattern pattern = Pattern.compile(
                "(?i)diagnosis\\s*(?:is)?\\s*([A-Za-z ]+)"
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "";
    }
}