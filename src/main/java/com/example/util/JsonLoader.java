package com.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class JsonLoader {

    private final ObjectMapper mapper;

    public JsonLoader() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Charge un fichier JSON depuis src/main/resources/data/
     * et le désérialise en liste d'objets du type donné.
     *
     * Exemple : jsonLoader.loadList("matches.json", Match.class)
     */
    public <T> List<T> loadList(String fileName, Class<T> clazz) {
        try {
            InputStream is = new ClassPathResource("data/" + fileName).getInputStream();
            return mapper.readValue(is, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger le fichier JSON : data/" + fileName, e);
        }
    }

    /**
     * Charge un fichier JSON avec un TypeReference (pour les génériques complexes).
     */
    public <T> T load(String fileName, TypeReference<T> typeRef) {
        try {
            InputStream is = new ClassPathResource("data/" + fileName).getInputStream();
            return mapper.readValue(is, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger le fichier JSON : data/" + fileName, e);
        }
    }
}
