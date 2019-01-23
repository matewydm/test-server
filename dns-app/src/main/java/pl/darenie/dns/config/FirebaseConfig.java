package pl.darenie.dns.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@EnableAutoConfiguration
public class FirebaseConfig {

    @Bean
    public DatabaseReference firebaseDatabase() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Value("${firebase.databaseUrl}")
    public String DATABASE_URL;
    @Value("${firebase.serviceKeyFilepath}")
    public String SERVICE_KEY_FILEPATH;


    @PostConstruct
    public void init() throws IOException {

        InputStream serviceAccount = FirebaseConfig.class.getClassLoader().getResourceAsStream(SERVICE_KEY_FILEPATH);

        FirebaseOptions options =  new FirebaseOptions.Builder()
                    .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                    .setDatabaseUrl(DATABASE_URL).build();
        FirebaseApp.initializeApp(options);
    }

}
