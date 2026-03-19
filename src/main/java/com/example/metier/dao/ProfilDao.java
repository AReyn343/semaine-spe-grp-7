package com.example.metier.dao;

import org.springframework.stereotype.Repository;

import com.example.metier.dto.ProfilDto;
import com.example.metier.entity.PlayerStats;
import com.example.util.DtoEntityUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProfilDao {

    @PersistenceContext
    private EntityManager entityManager;

    public ProfilDto save(ProfilDto profilDto) {
    	
    	PlayerStats profil = DtoEntityUtil.profilDToToProfil(profilDto);
    	
    	entityManager.persist(profil);
    	
    	profilDto.setId(profil.getId());
    	
    	return profilDto;
        
    }
    
    public PlayerStats findById(Long id) {
        return entityManager.find(PlayerStats.class, id);
    }
    
}
