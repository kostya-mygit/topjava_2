package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.util.MealsUtil.meals;

public class MealStorageInMemory implements MealStorage {
    @Override
    public Meal get(int id) {
        return meals.get(id);
    }

    @Override
    public void save(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }
}
