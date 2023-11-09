package com.example.uploadexcelfiles.service;

import com.example.uploadexcelfiles.DTO.AddressResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class AddressService {
    private final RestTemplate restTemplate;

    public AddressService() {
        this.restTemplate = new RestTemplate();
    }


    public AddressResponseDTO fetchAddresses() {
        String url = "https://fias-public-service.nalog.ru/api/spas/v2.0/SearchAddressItems";
        return restTemplate.getForObject(url, AddressResponseDTO.class);
    }
}