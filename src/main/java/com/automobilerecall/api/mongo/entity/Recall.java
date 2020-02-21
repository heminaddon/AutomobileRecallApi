package com.automobilerecall.api.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recall {

	String recallType;
	String recallStatus;
	String recallTitle;
	String recallDescription;

	@Override
	public String toString() {
		return "Recall [recallType=" + recallType + ", recallStatus=" + recallStatus + ", recallTitle=" + recallTitle
				+ ", recallDescription=" + recallDescription + "]";
	}

}
