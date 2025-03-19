package org.bot.util;

import liquibase.pro.packaged.I;
import org.dto.CarModelDto;
import org.dto.CarPositionDto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardConstants {

    public static InlineKeyboardMarkup authCommands() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        InlineKeyboardButton signInButton = new InlineKeyboardButton();
        signInButton.setText("Вход");
        signInButton.setCallbackData("/signIn");
        InlineKeyboardButton signUpButton = new InlineKeyboardButton();
        signUpButton.setText("Регистрация");
        signUpButton.setUrl("http://194.87.94.5:3000/home");
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(signInButton);
        row.add(signUpButton);
        rows.add(row);
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup botButtons() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(new KeyboardButton("профиль"));
        keyboardRow.add(new KeyboardButton("машины"));

        keyboard.add(keyboardRow);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public static InlineKeyboardMarkup carModelsButtons(List<CarModelDto> carModels) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (CarModelDto carModel : carModels) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(carModel.getName());
            button.setCallbackData("/model_" + carModel.getName());
            buttons.add(button);
            if (buttons.size() == 3) {
                rows.add(buttons);
                buttons = new ArrayList<>();
            }
        }
        rows.add(buttons);
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    public static InlineKeyboardMarkup carPositionListButtons(List<CarPositionDto> carPositions) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (int i = 0; i < carPositions.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setCallbackData("/car_" + carPositions.get(i).getId());
            button.setText(String.valueOf(i + 1));
            buttons.add(button);
            if (buttons.size() == 4) {
                rows.add(buttons);
                buttons = new ArrayList<>();
            }
        }
        rows.add(buttons);

        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }
}
