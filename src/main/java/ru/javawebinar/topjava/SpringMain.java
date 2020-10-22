package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));

            System.out.println("AdminRestController test");
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName3", "email1@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName5", "email2@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName2", "email3@mail.ru", "password", Role.ADMIN));
            System.out.println(adminUserController.getAll());
            System.out.println(adminUserController.get(1));
            System.out.println(adminUserController.getByMail("email3@mail.ru"));

            System.out.println("MealRestController test");
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            System.out.println(mealRestController.getAll());
            System.out.println(mealRestController.get(1));

        }
    }
}
