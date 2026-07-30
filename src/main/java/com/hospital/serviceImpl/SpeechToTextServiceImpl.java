package com.hospital.serviceImpl;

import com.hospital.exception.SpeechToTextException;
import com.hospital.service.SpeechToTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@Slf4j
public class SpeechToTextServiceImpl implements SpeechToTextService {

    @Value("${python.executable}")
    private String python;

    @Value("${whisper.script}")
    private String script;

    @Value("${temp.audio.folder}")
    private String tempFolder;

    @Override
    public String convertAudioToText(MultipartFile audio) {

        try {

            File folder = new File(tempFolder);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String filename = UUID.randomUUID() + ".webm";

            Path path = Path.of(tempFolder, filename);

            Files.copy(audio.getInputStream(), path);

            ProcessBuilder builder = new ProcessBuilder(

                    python,

                    script,

                    path.toAbsolutePath().toString()

            );

            builder.redirectErrorStream(false);

            Process process = builder.start();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

            StringBuilder transcription = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {

                transcription.append(line).append("\n");

            }

            int exit = process.waitFor();

          //  Files.deleteIfExists(path);

            if (exit != 0) {
                throw new SpeechToTextException(
                        "Whisper execution failed.\nOutput:\n" + transcription.toString()
                );
            }

            System.out.println("Exit Code = " + exit);
            System.out.println("Python Output = ");
            System.out.println(transcription);

            return transcription.toString().trim();

        }

        catch (Exception e) {

            throw new SpeechToTextException(e.getMessage());

        }

    }

}