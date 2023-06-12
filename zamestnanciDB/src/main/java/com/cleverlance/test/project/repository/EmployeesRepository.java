package com.cleverlance.test.project.repository;

import com.cleverlance.test.project.repository.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeesRepository extends JpaRepository<Employee, Long> {
	

}
