package pl.termosteam.kinex.service.security;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActivationService {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
