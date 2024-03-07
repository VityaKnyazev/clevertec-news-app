package ru.clevertec.ecl.knyazev.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.ecl.knyazev.controller.exception.DefaultResponseEntityExceptionHandler;

@ExtendWith(SpringExtension.class)
public class ExceptionControllerAutoConfigurationTest {
    private static final String CONFIGURATION_ON_CLASS_AND_BEAN_NAME = "defaultResponseEntityExceptionHandler";
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    public void checkDefaultResponseEntityExceptionHandlerClassShouldBePresent() {
        contextRunner.withUserConfiguration(ExceptionControllerAutoConfiguration.class)
                .run(context -> {
                    assertThat(context.containsBean(CONFIGURATION_ON_CLASS_AND_BEAN_NAME));
                    assertThat(context.getBean(CONFIGURATION_ON_CLASS_AND_BEAN_NAME))
                            .isExactlyInstanceOf(DefaultResponseEntityExceptionHandler.class);
                });
    }

    @Test
    public void checkDefaultResponseEntityExceptionHandlerClassNotPresent() {
        contextRunner.withUserConfiguration(ExceptionControllerAutoConfiguration.class)
                .withClassLoader(new FilteredClassLoader(DefaultResponseEntityExceptionHandler.class))
                .run(context -> {
                    assertThat(context).doesNotHaveBean(CONFIGURATION_ON_CLASS_AND_BEAN_NAME);
                    assertThat(context).doesNotHaveBean(DefaultResponseEntityExceptionHandler.class);
                });
    }
}
