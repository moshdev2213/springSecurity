package com.example.springjwt.authontroller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequset {
	private String fname;
	private String lname;
	private String email;
	private String password;
}
