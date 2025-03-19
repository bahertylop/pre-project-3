package org.bot.util;

import org.dto.CarPositionDto;
import org.dto.response.ProfileResponse;

import java.util.List;

public class MessagesConstants {

    public static final String HELLO_MESSAGE = "Привет, здесь ты можешь отслеживать медианную цену на свой автомобиль на авито, если ты уже зарегистрирован, нажми \"вход\", если нет, то сначала нажми \"регистрация\"";
    public static final String ADD_EMAIL_MESSAGE = "Введите почту";
    public static final String USER_ALREADY_SIGNED_IN = "Вы уже вошли";
    public static final String NOT_VALID_EMAIL_MESSAGE = "Введена некорректная почта, попробуйте еще раз";
    public static final String ADD_PASSWORD_MESSAGE = "Введите пароль";
    public static final String NOT_VALID_PASSWORD_MESSAGE = "Введен некорректный пароль, длина от 8 до 50 символов, только латинский алфавит, хотя-бы одна маленькая буква, хотя-бы одна заглавная буква, хотя-бы одна цифра. Попробуйте еще раз";
    public static final String SUCCESS_SIGN_IN = "успешный вход, теперь можете пользоваться функциями бота";
    public static final String FAILED_SIGN_IN = "ошибка при входе, попробуйте еще раз";
    public static final String EMPTY_CAR_POSITION_LIST = "Вы пока не добавили ни одной машины";

    public static final String CAR_POSITION_LIST_MESSAGE = "Список ваших машин:";

    public static String carPositionListMessage(List<CarPositionDto> cars) {
        StringBuilder sb = new StringBuilder();
        sb.append("Список ваших машин:\n");
        for (int i = 0; i < cars.size(); i++) {
            CarPositionDto car = cars.get(i);
            sb.append(i + 1).append(". ").append(car.getBrand()).append(" ").append(car.getModel()).append("\n");
            sb.append("Год: ")
                    .append(car.getYearFrom() == null ? "_" : car.getYearFrom())
                    .append(" - ")
                    .append(car.getYearBefore() == null ? "_" : car.getYearBefore())
                    .append("\n");
            sb.append("Пробег: ")
                    .append(car.getMileageFrom() == null ? "_" : car.getMileageFrom())
                    .append(" - ")
                    .append(car.getMileageBefore() == null ? "_" : car.getMileageBefore())
                    .append("\n");
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String profileResponseMessage(ProfileResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(response.getId()).append("\n");
        sb.append("name: ").append(response.getName()).append("\n");
        sb.append("email: ").append(response.getEmail()).append("\n");
        sb.append("age: ").append(response.getAge()).append("\n");
        sb.append("roles: ").append(response.getRoles());

        return sb.toString();
    }

    public static final String INPUT_CAR_BRAND_MESSAGE = "Введите марку автомобиля";
    public static final String INPUT_CAR_MODEL_MESSAGE = "Выберите модель автомобиля";
    public static final String INPUT_YEAR_FROM_MESSAGE = "Введите параметр год от, если не хотите указывать отправьте -";
    public static final String INPUT_YEAR_BEFORE_MESSAGE = "Введите параметр год до, если не хотите указывать отправьте -";
    public static final String INPUT_MILEAGE_FROM_MESSAGE = "Введите параметр пробег от, если не хотите указывать отправьте -";
    public static final String INPUT_MILEAGE_BEFORE_MESSAGE = "Введите параметр пробег до, если не хотите указывать отправьте -";
    public static final String NOT_FOUND_CAR_BRAND = "Не найдена введенная марка, возможно написано с ошибкой";
    public static final String SUCCESS_ADD_CAR_POSITION = "Успешно добавлена новая машина, начат парсинг цен";
    public static final String FAILED_TO_ADD_CAR_POSITION = "Не удалось добавить новую машину, попробуйте снова";

}
