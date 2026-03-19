package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.metier.entity.PlayerStats;
import com.example.service.ProfilService;

@Configuration
public class DataTestRunner {

    @Bean
    CommandLineRunner testProfil(ProfilService profilService) {
        return args -> {
            PlayerStats p = new PlayerStats();
            p.setName("Test CommandLine");

            System.out.println("✅ Profil créé avec ID : " + p.getId());
        };
    }
}
