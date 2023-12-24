package ba.unsa.etf.ts.backend.utils.validation.annotation;

import ba.unsa.etf.ts.backend.utils.validation.validator.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmailConstraint {
    String message() default "Email address already in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
