package com.example.Test;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface EmployeesRepository extends JpaRepository<Employee, Long> {
	

}
