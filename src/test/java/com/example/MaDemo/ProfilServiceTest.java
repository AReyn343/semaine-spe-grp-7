package com.example.MaDemo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.service.ProfilService;

@SpringBootTest
class ProfilServiceTest {

    @Autowired
    private ProfilService profilService;

    @Test
    void testSaveProfil() {
        
    	// ce que la DB renverrait après save()
//        Profil savedProfil = new Profil();
//        savedProfil.setId(1L);
//        savedProfil.setName("Test Profil");
//
//        when(profilRepository.save(any(Profil.class)))
//            .thenReturn(savedProfil);
//
//        ProfilDto dto = new ProfilDto();
//        dto.setName("Test Profil");
//
//        ProfilDto result = profilService.saveProfil(dto);
//
//        assertThat(result.getId()).isEqualTo(1L);
        
    }
}
