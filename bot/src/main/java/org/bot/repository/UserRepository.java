package org.bot.repository;

import org.bot.model.TgUser;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<TgUser, Long> {

    Optional<TgUser> getUserByChatId(Long chatId);
}
