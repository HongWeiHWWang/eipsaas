package com.hotent.base.conf;

import javax.annotation.Resource;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * 
 * @author jason
 *
 */
@Configuration
public class ValidatorConfig {

	/**
     * MessageSource
     */
    @Resource
    private MessageSource messageSource;

    /**
     * Validation message i18n
     * @return Validator
     */
    @NotNull
    @Bean
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(this.messageSource);
        return validator;
    }
	
}
