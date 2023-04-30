package hu.milliolepesstat.entity;

import lombok.Data;

@Data
public class School {
	private String id;
	private Integer position;
	private String name;
	private Double kkNumber;
	private Integer participants;
	private String city;
	private String county;
}

