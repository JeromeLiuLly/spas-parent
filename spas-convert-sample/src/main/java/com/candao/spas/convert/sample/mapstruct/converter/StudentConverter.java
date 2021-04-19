package com.candao.spas.convert.sample.mapstruct.converter;


import com.candao.spas.convert.sample.mapstruct.bean.dto.BagDto;
import com.candao.spas.convert.sample.mapstruct.bean.dto.ProjectDto;
import com.candao.spas.convert.sample.mapstruct.bean.dto.StudentDto;
import com.candao.spas.convert.sample.mapstruct.bean.vo.Project;
import com.candao.spas.convert.sample.mapstruct.bean.vo.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentConverter {
	
	StudentConverter INSTANCE = Mappers.getMapper(StudentConverter.class);
	
	@Mappings({
		@Mapping(source = "name", target = "userName"),
		@Mapping(source = "consigneePhones", target = "phone"),
		@Mapping(target = "birthday", expression = "java(org.apache.commons.lang3.time.DateFormatUtils.format(Long.valueOf(student.getBirth()),\"yyyy-MM-dd HH:mm:ss\"))"),
		@Mapping(source = "projects", target = "pro"),
		@Mapping(source = "bag",target = "back"),
		@Mapping(source = "height",target = "height", numberFormat = "$#.00")
	})
	StudentDto domain2dto(Student student);

	@Mappings({
			@Mapping(source = "name", target = "project"),
			@Mapping(source = "teacherId", target = "value")
	})
	ProjectDto domain2dto(Project project);

	default BagDto domain2dto(String value){
		BagDto bagDto = new BagDto();
		bagDto.setName(value);
		return bagDto;
	}
}
