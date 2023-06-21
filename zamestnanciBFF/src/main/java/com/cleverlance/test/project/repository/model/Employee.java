package com.cleverlance.test.project.repository.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {

	private Long id;
	private String name;
	private String surname;
	private LocalDate dateBirth;
	private String email;

}
