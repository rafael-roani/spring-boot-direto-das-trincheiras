package dev.rafa.userservice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mapstruct.Qualifier;

@Qualifier
@Retention(value = RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface EncodedMapping {

}
