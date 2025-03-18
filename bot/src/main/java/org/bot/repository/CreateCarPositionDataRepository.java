package org.bot.repository;

import org.bot.model.CreateCarPositionData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateCarPositionDataRepository extends JpaRepository<CreateCarPositionData, Long> {

    void deleteAllByChatId(Long chatId);

    CreateCarPositionData findByChatId(Long chatId);


}
