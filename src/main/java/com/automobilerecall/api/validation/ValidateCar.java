package com.automobilerecall.api.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.automobilerecall.api.mongo.entity.Car;

@Component
public class ValidateCar implements Validator{

	boolean invalid = false;

	public boolean supports(Class<?> clazz) {
		return Car.class.equals(clazz);
	}
	
	
	@Override
	public void validate(Object target, Errors errors) {
		Car car = (Car) target;

		if (car.getVinnumber() != null ) {

			boolean invalid = false;

			if (!invalid) {
				if (car.getVinnumber().length() != 17) {
					errors.rejectValue("vinnumber", "Validation Error : Vin Number length must be 17");
				}
			}
		} else {
			errors.rejectValue("vinnumber", "Validation Error : Vin Number required");
		}

	}

}
