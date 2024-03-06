package ru.clevertec.ecl.knyazev.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.ecl.knyazev.aop.ControllerLoggerAspect;

@ExtendWith(SpringExtension.class)
public class ControllerLoggingAutoConfigurationTest {
    private static final String CONFIGURATION_ON_CLASS_AND_BEAN_NAME = "controllerLoggerAspect";
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    public void checkControllerLoggerAspectClassShouldBePresent() {
        System.out.println(CONFIGURATION_ON_CLASS_AND_BEAN_NAME);
        contextRunner.withUserConfiguration(ControllerLoggingAutoConfiguration.class)
                .run(context -> {
                    assertThat(context.containsBean(CONFIGURATION_ON_CLASS_AND_BEAN_NAME));
                    assertThat(context.getBean(CONFIGURATION_ON_CLASS_AND_BEAN_NAME))
                            .isExactlyInstanceOf(ControllerLoggerAspect.class);
                });
    }

    @Test
    public void checkControllerLoggerAspectClassNotPresent() {
        contextRunner.withUserConfiguration(ControllerLoggingAutoConfiguration.class)
                .withClassLoader(new FilteredClassLoader(ControllerLoggerAspect.class))
                .run(context -> {
                    assertThat(context).doesNotHaveBean(CONFIGURATION_ON_CLASS_AND_BEAN_NAME);
                    assertThat(context).doesNotHaveBean(ControllerLoggerAspect.class);
                });
    }
}
