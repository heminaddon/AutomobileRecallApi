package com.automobilerecall.api.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.automobilerecall.api.dto.EmailDto;
import com.automobilerecall.api.mongo.entity.Car;

public interface CarService {
	
	public List<Car> findAll();

	public Optional<Car> findByVinnumber(String vinnumber);
	
	public Optional<Car> findByVinnumberDetailed(String vinnumber);

	public <S extends Car> S insert(S entity);

	public <S extends Car> List<S> insert(Iterable<S> entities);
	
	public boolean sendEmail(EmailDto emailDto);

	public Map<String, Object> readOneByOne(MultipartFile file);
}