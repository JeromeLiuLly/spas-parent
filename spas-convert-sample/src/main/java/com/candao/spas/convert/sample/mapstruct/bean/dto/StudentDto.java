package com.candao.spas.convert.sample.mapstruct.bean.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDto {

	private String userName;
	private Integer age;
	private String phone;
	
	private String birthday;

	private Double height;
	
	private List<ProjectDto> pro;

	private List<BagDto> back;
	
}
