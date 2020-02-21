package com.automobilerecall.api.dao.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.automobilerecall.api.dao.CarDao;
import com.automobilerecall.api.dto.EmailDto;
import com.automobilerecall.api.mongo.entity.Car;

@Component
public  class CarDaoImpl {

	@Autowired
	CarDao carDao;

	public List<Car> findAll() {
		return carDao.findAll();
	}

	public Optional<Car> findByVinnumberDetailed(String vinnumber) {
		return carDao.findByVinnumber(vinnumber);
	}

	public <S extends Car> S save(S entity) {
		return carDao.save(entity);
	}

	public <S extends Car> List<S> saveAll(Iterable<S> entities) {
		return carDao.saveAll(entities);
	}

	//public abstract boolean sendEmail(EmailDto emailDto);

	//public abstract boolean readOneByOne(MultipartFile file);

}
