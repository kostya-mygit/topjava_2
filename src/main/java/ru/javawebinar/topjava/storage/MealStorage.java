package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

public interface MealStorage {
    Meal get(int id);

    void save(Meal meal);

    void delete(int id);
}
