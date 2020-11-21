package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal newMeal = MealTestData.getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        Meal mealWithDuplicatedDateTime = getWithDuplicatedDateTime();
        assertThrows(DuplicateKeyException.class, () -> service.create(mealWithDuplicatedDateTime, USER_ID));
    }

    @Test
    public void get() {
        Meal actualMeal = service.get(USER_MEAL_ID_1, USER_ID);
        assertMatch(actualMeal, user_meal_1);
    }

    @Test
    public void getForAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID_1, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_ID_1, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID_1, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_NOT_FOUND, USER_ID));
    }

    @Test
    public void deletedForAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL_ID_1, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(service.getBetweenInclusive(null, null, USER_ID), service.getAll(USER_ID));
        assertMatch(service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                user_meal_3, user_meal_2, user_meal_1);
        assertMatch(service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 31),
                LocalDate.of(2020, Month.JANUARY, 31), USER_ID),
                user_meal_7, user_meal_6, user_meal_5, user_meal_4);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, admin_meal_2, admin_meal_1);
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(USER_MEAL_ID_1, USER_ID), updated);
    }

    @Test
    public void updateForAnotherUser() {
        Meal updated = MealTestData.getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }
}
