package org.bot.service;

import lombok.RequiredArgsConstructor;
import org.bot.model.CreateCarPositionData;
import org.bot.repository.CreateCarPositionDataRepository;
import org.hibernate.annotations.AttributeAccessor;
import org.hibernate.annotations.Target;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCarPositionDataService {

    private final CreateCarPositionDataRepository carPositionDataRepository;

    @Transactional
    public void deleteAllCarPositionDataByChatId(Long chatId) {
        carPositionDataRepository.deleteAllByChatId(chatId);
    }

    public CreateCarPositionData getCarPosition(Long chatId) {
        return carPositionDataRepository.findByChatId(chatId);
    }

    public void createCarPositionData(Long chatId, String carBrand) {
        CreateCarPositionData data = CreateCarPositionData.builder()
                .chatId(chatId)
                .brandName(carBrand)
                .build();
        carPositionDataRepository.save(data);
    }

    @Transactional
    public void setModelToCarCreatePositionData(Long chatId, String carModel) {
        CreateCarPositionData data = getCarPosition(chatId);
        data.setModelName(carModel);
        carPositionDataRepository.save(data);
    }

    @Transactional
    public void setYearFromToCarCreatePositionData(Long chatId, Integer yearFrom) {
        CreateCarPositionData data = getCarPosition(chatId);
        data.setYearFrom(yearFrom);
        carPositionDataRepository.save(data);
    }

    @Transactional
    public void setYearBeforeToCarCreatePositionData(Long chatId, Integer yearBefore) {
        CreateCarPositionData data = getCarPosition(chatId);
        data.setYearTo(yearBefore);
        carPositionDataRepository.save(data);
    }

    @Transactional
    public void setMileageFromToCarCreatePositionData(Long chatId, Integer mileageFrom) {
        CreateCarPositionData data = getCarPosition(chatId);
        data.setMileageFrom(mileageFrom);
        carPositionDataRepository.save(data);
    }

    @Transactional
    public void setMileageBeforeToCarCreatePositionData(Long chatId, Integer mileageBefore) {
        CreateCarPositionData data = getCarPosition(chatId);
        data.setMileageBefore(mileageBefore);
        carPositionDataRepository.save(data);
    }
}
