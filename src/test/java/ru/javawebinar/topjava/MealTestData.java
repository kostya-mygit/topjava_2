package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID_1 = START_SEQ + 2;
    public static final int USER_MEAL_ID_2 = START_SEQ + 3;
    public static final int USER_MEAL_ID_3 = START_SEQ + 4;
    public static final int USER_MEAL_ID_4 = START_SEQ + 5;
    public static final int USER_MEAL_ID_5 = START_SEQ + 6;
    public static final int USER_MEAL_ID_6 = START_SEQ + 7;
    public static final int USER_MEAL_ID_7 = START_SEQ + 8;
    public static final int ADMIN_MEAL_ID_1 = START_SEQ + 9;
    public static final int ADMIN_MEAL_ID_2 = START_SEQ + 10;
    public static final int MEAL_NOT_FOUND = 10;

    public static final Meal user_meal_1 = new Meal(USER_MEAL_ID_1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal user_meal_2 = new Meal(USER_MEAL_ID_2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal user_meal_3 = new Meal(USER_MEAL_ID_3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal user_meal_4 = new Meal(USER_MEAL_ID_4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal user_meal_5 = new Meal(USER_MEAL_ID_5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal user_meal_6 = new Meal(USER_MEAL_ID_6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal user_meal_7 = new Meal(USER_MEAL_ID_7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    public static final Meal admin_meal_1 = new Meal(ADMIN_MEAL_ID_1, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    public static final Meal admin_meal_2 = new Meal(ADMIN_MEAL_ID_2, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 21, 0), "Еда", 100);
    }

    public static Meal getWithDuplicatedDateTime() {
        return new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Еда", 100);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(user_meal_1);
        updated.setDescription("Второй завтрак");
        updated.setCalories(550);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
