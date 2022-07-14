package Pack.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {
    private final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${firebase-sdk-path}") // your firebase sdk path
    private String firebaseSdkPath;

    @PostConstruct
    public void initialize() {
    	System.out.println(firebaseSdkPath);
        try {

            ClassPathResource resource = new ClassPathResource(firebaseSdkPath);
            InputStream serviceAccount = resource.getInputStream();
            // 2021.06.23 FirebaseOptions 생성자가 Deprecated 되었기 때문에 builder 수정.
            // 2022.01.04 공식홈페이지에서 제대로 수정됨
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);

        } catch (FileNotFoundException e) {
            logger.error("Firebase ServiceAccountKey FileNotFoundException" + e.getMessage());
        } catch (IOException e) {
            logger.error("FirebaseOptions IOException" + e.getMessage());
        }

    }
}