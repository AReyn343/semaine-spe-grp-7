package com.example.util;

import com.example.metier.dto.ProfilDto;
import com.example.metier.entity.PlayerStats;

public class DtoEntityUtil {
	

    public static PlayerStats profilDToToProfil(ProfilDto profilDto) {
    	
    	PlayerStats profil = new PlayerStats();
    	
    	profil.setName(profilDto.getName());
    	
    	return profil;
    	
    }

}
