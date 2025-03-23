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
public class TgUserDto {

    private Long id;

    private String email;

    private Long chatId;

    private String tgUserName;

    private String jwtToken;

    private String refreshToken;

    private TgUser.BotState botState;

    public static TgUserDto from(TgUser tgUser) {
        return TgUserDto.builder()
                .id(tgUser.getId())
                .chatId(tgUser.getChatId())
                .tgUserName(tgUser.getTgUserName())
                .jwtToken(tgUser.getJwtToken())
                .refreshToken(tgUser.getRefreshToken())
                .botState(tgUser.getBotState())
                .build();
    }
}
