package pl.termosteam.kinex.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActivateService {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
