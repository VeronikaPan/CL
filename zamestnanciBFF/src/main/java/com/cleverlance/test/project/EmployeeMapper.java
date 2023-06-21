package com.cleverlance.test.project;

import com.cleverlance.test.project.repository.model.Employee;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
//import org.openapitools.client.model.EmployeeDTO;
import org.openapitools.model.EmployeeDTO;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);
    
    @Mapping(source = "name", target = "name")
    EmployeeDTO employeeToDTO(Employee employee);

    @Mapping(source = "name", target = "name")
    Employee dtoToEmployee(EmployeeDTO dto);

    @Mapping(source = "name", target = "name")
    default EmployeeDTO employeeToDTO(Optional<Employee> employeeData) {
        return employeeData.map(this::employeeToDTO).orElse(null);
    }
}


