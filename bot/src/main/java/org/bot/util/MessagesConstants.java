package org.bot.util;

import org.dto.CarPositionDto;
import org.dto.response.CarPositionResponse;
import org.dto.response.ProfileResponse;

import java.util.List;

public class MessagesConstants {

    public static final String HELLO_MESSAGE = "Привет, здесь ты можешь отслеживать медианную цену на свой автомобиль на авито, если ты уже зарегистрирован, нажми \"вход\", если нет, то сначала нажми \"регистрация\"";
    public static final String ADD_EMAIL_MESSAGE = "Введите почту";
    public static final String USER_ALREADY_SIGNED_IN = "Вы уже вошли";
    public static final String NOT_VALID_EMAIL_MESSAGE = "Введена некорректная почта, попробуйте еще раз";
    public static final String ADD_PASSWORD_MESSAGE = "Введите пароль";
    public static final String NOT_VALID_PASSWORD_MESSAGE = "Введен некорректный пароль, длина от 8 до 50 символов, только латинский алфавит, хотя-бы одна маленькая буква, хотя-бы одна заглавная буква, хотя-бы одна цифра. Попробуйте еще раз";
    public static final String SUCCESS_SIGN_IN = "Успешный вход, теперь можете пользоваться функциями бота";
    public static final String FAILED_SIGN_IN = "Ошибка при входе, попробуйте еще раз";
    public static final String EMPTY_CAR_POSITION_LIST = "Вы пока не добавили ни одной машины";

    public static final String CAR_POSITION_LIST_MESSAGE = "Список ваших машин:";

    public static String carPositionListMessage(List<CarPositionDto> cars) {
        StringBuilder sb = new StringBuilder();
        sb.append("Список ваших машин:\n");
        for (int i = 0; i < cars.size(); i++) {
            CarPositionDto car = cars.get(i);
            sb.append(i + 1).append(". ").append(car.getBrand()).append(" ").append(car.getModel()).append("\n");
            carPositionParamsToString(sb, car.getYearFrom(), car.getYearBefore(), car.getMileageFrom(), car.getMileageBefore());
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

    public static final String FAILED_TO_GET_CAR_POSITION = "Не удалось получить информацию о машине";
    public static final String EMPTY_CAR_POSITION_PRICES_LIST = "Цены еще не загружены, попробуйте немного позже";

    public static String getCarPositionMessage(CarPositionResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append(response.getBrand()).append(" ").append(response.getModel()).append("\n");
        carPositionParamsToString(sb, response.getYearFrom(), response.getYearBefore(), response.getMileageFrom(), response.getMileageBefore());
        return sb.toString();
    }

    private static void carPositionParamsToString(StringBuilder sb, Integer yearFrom, Integer yearBefore, Integer mileageFrom, Integer mileageBefore) {
        sb.append("Год: ")
                .append(yearFrom == null ? "_" : yearFrom)
                .append(" - ")
                .append(yearBefore == null ? "_" : yearBefore)
                .append("\n");
        sb.append("Пробег: ")
                .append(mileageFrom == null ? "_" : mileageFrom)
                .append(" - ")
                .append(mileageBefore == null ? "_" : mileageBefore)
                .append("\n");
    }

    public static final String UNDEFINED_COMMAND_MESSAGE = "Команда не распознана";
}
