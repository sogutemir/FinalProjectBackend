package org.work.personnelinfo.personel.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
@Documented
@Constraint(validatedBy = TCIDNoValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTCIDNo {


    String message() default "Invalid T.C. Identity Number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
