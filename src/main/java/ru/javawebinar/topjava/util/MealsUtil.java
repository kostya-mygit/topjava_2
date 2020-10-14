package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MealsUtil {
    private static AtomicInteger counter = new AtomicInteger(0);

    private static final Integer COUNTER_1 = incrementAndGetCounter();
    private static final Integer COUNTER_2 = incrementAndGetCounter();
    private static final Integer COUNTER_3 = incrementAndGetCounter();
    private static final Integer COUNTER_4 = incrementAndGetCounter();
    private static final Integer COUNTER_5 = incrementAndGetCounter();
    private static final Integer COUNTER_6 = incrementAndGetCounter();
    private static final Integer COUNTER_7 = incrementAndGetCounter();

    public static ConcurrentMap<Integer, Meal> meals;

    static {
        meals = new ConcurrentHashMap<>();
        meals.put(COUNTER_1, new Meal(COUNTER_1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.put(COUNTER_2, new Meal(COUNTER_2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.put(COUNTER_3, new Meal(COUNTER_3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.put(COUNTER_4, new Meal(COUNTER_4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.put(COUNTER_5, new Meal(COUNTER_5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.put(COUNTER_6, new Meal(COUNTER_6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.put(COUNTER_7, new Meal(COUNTER_7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    public static void main(String[] args) {

        List<MealTo> mealsTo = filteredByStreams(new ArrayList<>(meals.values()), LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static List<Meal> getMeals() {
        return new ArrayList<>(meals.values());
    }

    public static int incrementAndGetCounter() {
        return counter.incrementAndGet();
    }
}
