package tw.com.ruten.springbootstarterwebvalidation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.com.ruten.springbootstarterwebvalidation.aop.aspect.ValidateRequestAspect;
import tw.com.ruten.springbootstarterwebvalidation.validator.NonNullableValidationProvider;
import tw.com.ruten.springbootstarterwebvalidation.validator.StringPatternValidationProvider;
import tw.com.ruten.springbootstarterwebvalidation.validator.ValidationProvider;

import static org.assertj.core.api.Assertions.assertThat;

class SpringBootStarterWebValidationApplicationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(WebValidationAutoConfiguration.class));

	@Test
	void contextLoads() {
		this.contextRunner.withUserConfiguration(ValidateConfiguration.class).run(context -> {
			assertThat(context).hasSingleBean(ValidateRequestAspect.class);
			assertThat(context).getBean("myValidateRequestAspect").isSameAs(context.getBean(ValidateRequestAspect.class));
		});
	}

	@Configuration
	static  class ValidateConfiguration {

		@Bean
		ValidateRequestAspect myValidateRequestAspect() {
			return new ValidateRequestAspect();
		}

		@Bean
		ValidationProvider nonNullableValidationProvider() {
			return new NonNullableValidationProvider();
		}

		@Bean
		ValidationProvider stringPatternValidationProvider() {
			return new StringPatternValidationProvider();
		}
	}

}
