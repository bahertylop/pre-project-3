package org.bot.util;

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
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add(new KeyboardButton("профиль"));
        keyboardRow.add(new KeyboardButton("машины"));

        keyboard.add(keyboardRow);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
}
