package com.automobilerecall.api.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.automobilerecall.api.dto.ApiResponse;
import com.automobilerecall.api.dto.EmailDto;
import com.automobilerecall.api.mongo.entity.Car;
import com.automobilerecall.api.service.CarService;
import com.automobilerecall.api.validation.ValidateEmailDto;

@RestController
@CrossOrigin(origins = "*")
public class CarController {
	
	@Autowired
	CarService carService;
	
	@Autowired
	ValidateEmailDto validator;

	/**
	 * find car vin record by vin no.
	 * @param vin
	 * @return
	 */
	@GetMapping("/findByVin")
	@ResponseBody
	public Optional<Car> findByVin(@RequestParam("vin") String vin) {
		System.out.println("FINDBYVIN CALLED");
		return carService.findByVinnumber(vin);
	}

	/**
	 * find detailed car info by vin no.
	 * @param vin
	 * @return
	 */
	@GetMapping("/detailed/findByVin")
	public Optional<Car> detailedFindByVin(@RequestParam("vin") String vin) {
		return carService.findByVinnumber(vin);
	}
	
	/**
	 * sends the inquiry email on form submit
	 * @param emailDto
	 * @param result
	 * @return
	 *
	 */
	@PostMapping("/sendEmail")
	public ApiResponse sendEmail2(@Validated @RequestBody EmailDto emailDto, BindingResult result) {
			System.out.println("SendEmail calls");		
		validator.validate(emailDto, result);
		if (!result.hasErrors()) {
			return new ApiResponse(carService.sendEmail(emailDto));
		} else {
			return new ApiResponse(false);
		}
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping("/import-csv")
	public ApiResponse importCsvSubmit(@RequestParam("file") MultipartFile file) throws IOException, Exception {
		System.out.println("import called");
		return new ApiResponse(carService.readOneByOne(file));
	}

	

	

}
