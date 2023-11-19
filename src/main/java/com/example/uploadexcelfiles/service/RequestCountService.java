package com.example.uploadexcelfiles.service;

import com.example.uploadexcelfiles.DTO.AddressDTO;
import com.example.uploadexcelfiles.DTO.AddressDetailsDTO;
import com.example.uploadexcelfiles.DTO.AddressResponseDTO;
import com.example.uploadexcelfiles.model.AddressDetail;
import com.example.uploadexcelfiles.model.AddressValue;
import com.example.uploadexcelfiles.model.ExcelAddress;
import com.example.uploadexcelfiles.model.RequestCount;
import com.example.uploadexcelfiles.repository.AddressDetailRepository;
import com.example.uploadexcelfiles.repository.AddressValueRepository;
import com.example.uploadexcelfiles.repository.RequestCountRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestCountService {
    private final AddressDetailRepository addressDetailRepository;
    private final AddressValueRepository addressValueRepository;
    private final RequestCountRepository requestCountRepository;


    public RequestCountService(
            AddressDetailRepository addressDetailRepository,
            AddressValueRepository addressValueRepository,
            RequestCountRepository requestCountRepository
    ) {
        this.addressDetailRepository = addressDetailRepository;
        this.addressValueRepository = addressValueRepository;
        this.requestCountRepository = requestCountRepository;
        }

    public void processAddressesAndSaveCount(List<ExcelAddress> dataFromExcel) throws InterruptedException {
        String url = "https://fias-public-service.nalog.ru/api/spas/v2.0/SearchAddressItems?search_string={name}&address_type=1";
        String token = "c0e8e0dd-3330-4772-b69d-e454da4fc865";
        HttpHeaders headers = new HttpHeaders();
        headers.add("master-token", token);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        int requestCount = 0;

        for (ExcelAddress data : dataFromExcel) {
            if (requestCount >= 100) {
                Thread.sleep(60000); // Пауза на минуту, если достигнут лимит запросов
                requestCount = 0; // Обнуляем счётчик после паузы
            }


            ResponseEntity<AddressResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AddressResponseDTO.class, data.getAddress());
            AddressResponseDTO addressResponse = response.getBody();

            if (addressResponse != null && addressResponse.getAddresses() != null) {
                for (AddressDTO addressDTO : addressResponse.getAddresses()) {
                    int objectId = addressDTO.getObject_id();

                    // Проверяем, есть ли уже такой адрес в базе данных
                    if (addressValueRepository.findByObjectId(objectId) == null) {
                        // Сохраняем адресные данные в базе данных
                        saveAddressDetails(data.getAddress(), addressDTO);
                    }
                }
            }
            requestCount++;
        }
        saveRequestCount(requestCount);
    }

    private void saveRequestCount(int count) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        RequestCount requestCount = requestCountRepository.findByTimestamp(currentDateTime);

        if(requestCount == null || requestCount.getTotalCount() == 0) {
            // Создание новой записи счётчика запросов
            requestCount = new RequestCount();
            requestCount.setTimestamp(currentDateTime);
            requestCount.setCount(count);
            requestCount.setTotalCount(count);
        } else {
            // Обновление значения счётчика
            requestCount.setCount(count);
            requestCount.setTotalCount(requestCount.getTotalCount() + count);
        }

        requestCountRepository.save(requestCount);
    }

    private void saveAddressDetails(String excelAddress, AddressDTO addressDTO) {
        AddressDetail addressDetail = new AddressDetail();
        AddressDetailsDTO addressDetailsDTO = addressDTO.getAddress_details();

        if (addressDetailsDTO != null) {
            // Дополнительные проверки на null для других полей
            if (addressDetailsDTO.getIfns_ul() != null) {
                addressDetail.setIfns_ul(addressDetailsDTO.getIfns_ul());
            }

            if (addressDetailsDTO.getIfns_fl() != null) {
                addressDetail.setIfns_fl(addressDetailsDTO.getIfns_fl());
            }

            if (addressDetailsDTO.getKladr_code() != null) {
                addressDetail.setKladr_code(addressDetailsDTO.getKladr_code());
            }


            // Сохранение AddressDetail только если хотя бы одно из полей не null
            if (addressDetail.getIfns_ul() != null || addressDetail.getIfns_fl() != null || addressDetail.getKladr_code() != null) {
                addressDetailRepository.save(addressDetail);
            }
        }

        AddressValue addressValue = new AddressValue();
        addressValue.setObjectId(addressDTO.getObject_id());
        addressValue.setObjectLevelId(addressDTO.getObject_level_id());
        addressValue.setOperationTypeId(addressDTO.getOperation_type_id());
        addressValue.setObjectGuid(addressDTO.getObject_guid());
        addressValue.setFullName(addressDTO.getFull_name());
        addressValue.setRegionCode(addressDTO.getRegion_code());


        // Привязка AddressValue к ExcelAddress
        addressValue.setExcelAddressValue(excelAddress);
        addressValue.setKladrCode(addressDetail.getKladr_code());
        addressValue.setAddressDetail(addressDetail);

            addressValueRepository.save(addressValue);

    }
}
