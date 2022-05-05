package com.alekennedy.usersmanagement;

import com.alekennedy.usersmanagement.etc.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableDiscoveryClient
@SpringBootApplication
public class UsersManagementApplication {

	public static void main(String[] args) {
            Configuration.initialize();
            SpringApplication.run(UsersManagementApplication.class, args);
	}
}
