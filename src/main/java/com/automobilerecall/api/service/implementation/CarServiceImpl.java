package com.automobilerecall.api.service.implementation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import com.automobilerecall.api.dao.CarDao;
import com.automobilerecall.api.dao.implementation.CarDaoImpl;
import com.automobilerecall.api.dto.EmailDto;
import com.automobilerecall.api.mongo.entity.Car;
import com.automobilerecall.api.mongo.entity.Recall;
import com.automobilerecall.api.service.CarService;
import com.automobilerecall.api.validation.ValidateCar;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Service
public class CarServiceImpl implements CarService {

	@Autowired
	CarDaoImpl carDao;
	
	@Autowired
	ValidateCar validator;

	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public Optional<Car> findByVinnumber(String vinnumber) {
		Optional<Car> mongos = carDao.findByVinnumberDetailed(vinnumber);
		if (mongos.isPresent()) {
			mongos.get().getRecalls().clear();
		}
		return mongos;
	}

	@Override
	@Transactional
	public <S extends Car> S insert(S entity) {
		return carDao.save(entity);
	}

	@Override
	@Transactional
	public <S extends Car> List<S> insert(Iterable<S> entities) {
		return carDao.saveAll(entities);
	}

	@Override
	public List<Car> findAll() {
		return carDao.findAll();
	}

	@Override
	public Optional<Car> findByVinnumberDetailed(String vinnumber) {
		return carDao.findByVinnumberDetailed(vinnumber);
	}

	@Override
	public boolean sendEmail(EmailDto emailDto) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

			helper.setTo(emailDto.getEmail());
			// helper.setTo("tituspkbawa@gmail.com");
			helper.setFrom("comcom.com");
			helper.setSubject("New Vin Inquiry");

			StringBuilder text = new StringBuilder();
			text.append("Hello, new inquiry from user ").append(emailDto.getFirstName()).append(", \n Phone:")
					.append(emailDto.getPhone()).append("\n Email ").append(emailDto.getEmail());

			Optional<Car> mongos = findByVinnumberDetailed(emailDto.getVin());
			String html = getEmailContent(emailDto.getVin(), emailDto.getFirstName(), emailDto.getPhone(),
					emailDto.getEmail(), mongos);
			helper.setText(html, true);

			mailSender.send(mimeMessage);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private String getEmailContent(String vin, String fname, String phone, String email, Optional<Car> mongos) {
		Car car;
		String html;
		/*
		 * this condition checks if information is Present for the given VIN Number if
		 * present then we will send them car information a table format else will send
		 * email with user's personal info and saying we will catch you if any
		 * information is found
		 *
		 */
		if (mongos.isPresent()) {
			car = mongos.get();

			html = "<html><head>"

					+ "</head>" + "<LINK REL='stylesheet' HREF='stylesheet/fac_css.css' TYPE='text/css'>" + "<body>"
					+ "<table border='1' width='800' cellpadding='2' cellspacing='1' bgColor='#FFFFFF' style='border-collapse: collapse' bordercolor='#000000' align='left'>"
					+ "<tr  bgColor=#FFFFFF class='centerheading' align='left'><td height='5'>Email</td>" + "<td>"
					+ email + "</td></tr>"

					+ "<tr  bgColor=#FFFFFF class='centerheading' align='left'><td height='5'>Name</td>" + "<td>"
					+ fname + "</td></tr>"

					+ "<tr  bgColor=#FFFFFF class='centerheading' align='left'><td height='5'>Phone number</td>"
					+ "<td >" + phone + "</td></tr>"

					+ "<tr  bgColor=#FFFFFF class='centerheading' align='left'><td height='5'>car</td>" + "<td >"
					+ car.getCar() + "</td></tr>"

					+ "<tr  bgColor=#FFFFFF class='centerheading' align='left'><td height='5'>Car name</td>" + "<td >"
					+ car.getCarname() + "</td></tr>"

					+ "<tr  bgColor=#FFFFFF class='centerheading' align='left'><td height='5'>Vin number</td>" + "<td>"
					+ car.getVinnumber() + "</td></tr>"

					+ "<tr  bgColor=#FFFFFF class='centerheading' align='left'><td height='5'>Incomplete recall</td>"
					+ "<td >" + car.getIncompleteRecall() + "</td></tr>"

					+ "<tr  bgColor=#FFFFFF class='centerheading' align='left'><td height='5'>Recalls</td>" + "<td >"
					+ car.getRecalls() + "</td></tr>"

					+ "</table></body></html>";
		} else {

			html = "<html><head>"

					+ "</head>" + "<LINK REL='stylesheet' HREF='stylesheet/fac_css.css' TYPE='text/css'>" + "<body>"
					+ "<b>No recalls information available for this Vin Number. We will update you once we find any information.</b>"
					+ "<table border='1' width='800' cellpadding='2' cellspacing='1' bgColor='#FFFFFF' style='border-collapse: collapse' bordercolor='#000000' align='left'>"
					+ "<tr  bgColor=#FFFFFF class='centerheading' align='left'><td height='5'>Email</td>" + "<td>"
					+ email + "</td></tr>"

					+ "<tr  bgColor=#FFFFFF class='centerheading' align='left'><td height='5'>Name</td>" + "<td>"
					+ fname + "</td></tr>"

					+ "<tr  bgColor=#FFFFFF class='centerheading' align='left'><td height='5'>Phone number</td>"
					+ "<td >" + phone + "</td></tr>"

					+ "<tr  bgColor=#FFFFFF class='centerheading' align='left'><td height='5'>Vin number</td>" + "<td>"
					+ vin + "</td></tr>"

					+ "</table></body></html>";

		}
		return html;
	}

	/**
	 * reads the csv file records.
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws Exception
	 */

	@Override
	@Transactional
	public Map<String, Object> readOneByOne(MultipartFile file) {
		InputStreamReader reader = null;
		CSVReader csvReader = null;
		Map<String, Object> errorMap = new HashMap<String, Object>();
		List<String> duplicateVins = new ArrayList<String>();
		Map<String, Object> validationErrors = new HashMap<String, Object>();
		
		//check for correct file extension
		if (file.isEmpty() || !file.getOriginalFilename().toUpperCase().contains(".CSV")) {
			errorMap.put("Incorrect file type.", "Please enter a CSV file.");
			return errorMap;
		}
		try {
			//read csv file
			reader = new InputStreamReader(file.getInputStream());
			csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
			String[] line;

			//read each record
			while ((line = csvReader.readNext()) != null) {
				List<Recall> recalls = new LinkedList<Recall>();
				// list.add(line);
				int incompleteRecallsNo =0;
				if (line[3] != null && !line[3].isEmpty())
					incompleteRecallsNo = Integer.valueOf(line[3]);
				if (incompleteRecallsNo > 0) {
					// get 1st recall
					while (incompleteRecallsNo > 0) {
						Recall r = new Recall(line[(incompleteRecallsNo * 4)], line[(incompleteRecallsNo * 4) + 1],
								line[(incompleteRecallsNo * 4) + 2], line[(incompleteRecallsNo * 4) + 3]);
						recalls.add(r);
						incompleteRecallsNo--;
					}
				}
				Car car = new Car(line[0], line[1], line[2], Integer.valueOf(line[3]), recalls);
				
				//validate the car object
				Errors validationError = new BeanPropertyBindingResult(new Car(), "car");
				validator.validate(car, validationError);
				if (!validationError.hasErrors()) {
					
					//check if record is already exists in collection
					Optional<Car> alreadyExistingCar = findByVinnumber(car.getVinnumber());
					if (alreadyExistingCar.isPresent()) {
						// this will update
						car.setId(alreadyExistingCar.get().getId());
						if(!duplicateVins.contains(car.getVinnumber())) {
							insert(car);
							duplicateVins.add(car.getVinnumber());
							
						}else {
							errorMap.put(car.getVinnumber(), "Same Vin number found more than once in csv file, not Updated.");
						}

					} else {
						// this will insert
						insert(car);
						errorMap.put(car.getVinnumber(), "Record Inserted.");
					}
				} else {
					validationErrors.put(car.getVinnumber(), validationError.getFieldErrors().get(0).getCode());
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			errorMap.put("File Error", "Incorrect file content found");
			return errorMap;
		} finally {
			try {
				reader.close();
				csvReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!duplicateVins.isEmpty()) {
			// errorMap.put("Duplicate Enitiy Updated.", duplicateVins);
			for (String duplicateVin : duplicateVins) {
				if(!errorMap.containsKey(duplicateVin))
				errorMap.put(duplicateVin, "Record Updated.");
			}
		}
		errorMap.putAll(validationErrors);

		return errorMap;

	}

}