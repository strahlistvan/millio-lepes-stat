package hu.milliolepesstat.entity;

import lombok.Data;

@Data
public class School {
	private String id;
	private Integer position;
	private String name;
	private Double okkNumber;
	private Integer participants;
	private String city;
	private String county;
}

