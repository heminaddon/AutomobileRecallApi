package com.automobilerecall.api.mongo.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "car")
public class Car {

	@Id
	private String id;

	private String car;

	private String carname;

	@Indexed(unique = true)
	private String vinnumber;

	Integer incompleteRecall;

	List<Recall> recalls;

	public Car(String carname, String vinnumber, String car, int incompleteRecalls, List<Recall> recallArray) {
		super();
		this.car = car;
		this.carname = carname;
		this.vinnumber = vinnumber;
		this.incompleteRecall = incompleteRecalls;
		recalls = recallArray;
	}

	@Override
	public String toString() {
		return " [ car=" + car + ", carname=" + carname + ", vinnumber=" + vinnumber + ", incompleteRecall="
				+ incompleteRecall + ", recalls=" + recalls + "]";
	}

}