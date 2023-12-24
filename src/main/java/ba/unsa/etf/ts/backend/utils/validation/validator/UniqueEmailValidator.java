package ba.unsa.etf.ts.backend.utils.validation.validator;

import ba.unsa.etf.ts.backend.repository.UserRepository;
import ba.unsa.etf.ts.backend.utils.validation.annotation.UniqueEmailConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmailConstraint, String> {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext cxt) {
        return true;
    }
}
