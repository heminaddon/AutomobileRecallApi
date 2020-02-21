package com.automobilerecall.api.validation;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.automobilerecall.api.dto.EmailDto;
@Component
public class ValidateEmailDto implements Validator {

	boolean invalid = false;

	public boolean supports(Class<?> clazz) {
		return EmailDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		EmailDto emailDto = (EmailDto) target;

		// starting from line 23 to line 40 checks for valid email address
		if (emailDto.getEmail() != null && emailDto.getEmail().length() > 0
				&& !Character.isWhitespace(emailDto.getEmail().charAt(0))) {
			String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
					+ "A-Z]{2,7}$";

			Pattern pat = Pattern.compile(emailRegex);
			if (!pat.matcher(emailDto.getEmail()).matches()) {
				errors.rejectValue("email", "email address is invalid");
			}
		} else {
			errors.rejectValue("email", "field required");
		}

		if (emailDto.getFirstName() != null && emailDto.getFirstName().length() > 0
				&& !Character.isWhitespace(emailDto.getFirstName().charAt(0))) {

			boolean invalid = false;

			for (int i = 0; i < emailDto.getFirstName().length() && !invalid; i++) {
				if (!Character.isAlphabetic(emailDto.getFirstName().charAt(i))
						&& emailDto.getFirstName().charAt(i) != 32) {
					invalid = true;
					errors.rejectValue("firstName", "field contains invalid characters");
				}
			}
			if (!invalid) {
				if (emailDto.getFirstName().length() < 2 || emailDto.getFirstName().length() > 30) {
					errors.rejectValue("firstName", "field length must be between 10 and 30");
				}
			}
		} else {
			errors.rejectValue("firstName", "field required");
		}

		if (emailDto.getVin() != null && emailDto.getVin().length() > 0
				&& !Character.isWhitespace(emailDto.getVin().charAt(0))) {

			boolean invalid = false;

			
			if (!invalid) {
				if (emailDto.getVin().length() != 17) {
					errors.rejectValue("vin", "field length must be 17");
				}
			}
		} else {
			errors.rejectValue("vin", "field required");
		}

		if (emailDto.getPhone() != null && emailDto.getPhone().length() > 0
				&& !Character.isWhitespace(emailDto.getPhone().charAt(0))) {
			boolean invalid = false;
			if (!invalid) {
				if (emailDto.getVin().length() < 10) {
					errors.rejectValue("phone", "field length must be greater than 10");
				}
			}
		} else {
			errors.rejectValue("phone", "field required");
		}
	}

}
