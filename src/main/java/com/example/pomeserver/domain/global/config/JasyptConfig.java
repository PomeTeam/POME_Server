package com.example.pomeserver.domain.global.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

    @Value("${jasypt.encryptor.algorithm}")
    private String algorithm;
    @Value("${jasypt.encryptor.pool-size}")
    private int poolSize;
    @Value("${jasypt.encryptor.string-output-type}")
    private String stringOutputType;
    @Value("${jasypt.encryptor.key-obtention-iterations}")
    private int keyObtentionIterations;
    @Value("${jasypt.encryptor.password}")
    private String password;

    @Bean
    public StringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPoolSize(poolSize);
        encryptor.setAlgorithm(algorithm);
        encryptor.setPassword(password);
        encryptor.setStringOutputType(stringOutputType);
        encryptor.setKeyObtentionIterations(keyObtentionIterations);
        String source1 = "rt-post-image";
        log.info("plane :: {}, encrypt :: {}", source1, encryptor.encrypt(source1));
        String source2 = "AKIAQDEN6QAG3O5OYZNP";
        log.info("plane :: {}, encrypt :: {}", source2, encryptor.encrypt(source2));
        String source3 = "+Ls1wBrR8ohLhUP6FYIP/QbErDxjLYsIHBN8gSOh";
        log.info("plane :: {}, encrypt :: {}", source3, encryptor.encrypt(source3));
        return encryptor;
    }
}
