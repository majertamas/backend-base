package hu.mikrum.backendbase.teszt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CastService {

    private final ObjectMapper objectMapper;

    public <T> T castObject(Object source, TypeReference<T> targetType) {
        return objectMapper.convertValue(source, targetType);
    }

}
