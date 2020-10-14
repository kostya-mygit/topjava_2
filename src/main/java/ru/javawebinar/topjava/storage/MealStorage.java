package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealStorage {
    Meal get(int id);

    Meal save(Meal meal);

    boolean delete(int id);

    Collection<Meal> getAll();
}
