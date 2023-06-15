package com.cleverlance.test.project;

import com.cleverlance.test.project.repository.model.Employee;
import java.sql.Date;
import java.time.ZoneOffset;
import javax.annotation.Generated;
import org.openapitools.model.EmployeeDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-14T14:48:13+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.33.0.v20230218-1114, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeDTO employeeToDTO(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setName( employee.getName() );
        employeeDTO.setId( employee.getId() );
        employeeDTO.setSurname( employee.getSurname() );
        employeeDTO.setEmail( employee.getEmail() );
        if ( employee.getDateBirth() != null ) {
            employeeDTO.setDateBirth( employee.getDateBirth().toLocalDate() );
        }

        return employeeDTO;
    }

    @Override
    public Employee dtoToEmployee(EmployeeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setName( dto.getName() );
        employee.setId( dto.getId() );
        employee.setSurname( dto.getSurname() );
        if ( dto.getDateBirth() != null ) {
            employee.setDateBirth( new Date( dto.getDateBirth().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        employee.setEmail( dto.getEmail() );

        return employee;
    }
}
