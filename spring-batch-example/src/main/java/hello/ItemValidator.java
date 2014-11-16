package hello;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ItemValidator implements Validator, InitializingBean {


    private javax.validation.Validator internalValidator;

    @Override
    public void afterPropertiesSet() throws Exception {
       ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
       internalValidator = validatorFactory.usingContext().getValidator();
    }

    public void validate(Object target) throws ValidationException {

        Set<ConstraintViolation<Object>> constraintViolations = internalValidator.validate(target);

        if(constraintViolations.size() > 0) {
            buildValidationException(constraintViolations);
        }
    }

    private void buildValidationException(
            Set<ConstraintViolation<Object>> constraintViolations) {
        StringBuilder message = new StringBuilder();

        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            message.append(constraintViolation.getMessage() + "\n");
        }

        throw new ValidationException(message.toString());
    }
}