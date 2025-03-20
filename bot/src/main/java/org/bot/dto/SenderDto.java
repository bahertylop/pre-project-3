package org.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bot.model.TgUser;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SenderDto {

    private Long chatId;

    private TgUserDto user;

    private Integer messageId;
}
