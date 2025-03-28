package org.bot.handlers.command;

import lombok.RequiredArgsConstructor;
import org.bot.bot.AvitoBot;
import org.bot.dto.SenderDto;
import org.bot.model.TgUser;
import org.bot.service.api.ProfileService;
import org.bot.service.UserService;
import org.bot.util.MessagesConstants;
import org.dto.response.ProfileResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfileCommandHandler implements CommandHandler {

    private final ProfileService profileService;

    private final UserService userService;

    @Override
    public void handle(AvitoBot bot, SenderDto senderDto, String command) {
        userService.changeUserBotStatus(senderDto, TgUser.BotState.WORKING);

        Optional<ProfileResponse> profileResponseOp = profileService.getProfileInfo(senderDto);
        profileResponseOp.ifPresentOrElse((profileResponse) -> {
                    bot.sendMessage(senderDto.getChatId(), MessagesConstants.profileResponseMessage(profileResponse));
                },
                () -> bot.sendMessage(senderDto.getChatId(), "Нет информации о профиле")
        );
    }
}
