package jb.CouponsBack;

import jb.CouponsBack.beans.OurSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;

@SpringBootApplication
public class CouponsBackApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =SpringApplication.run(CouponsBackApplication.class, args);
	}

	@Bean
	public HashMap<String, OurSession> sessions(){
		return new  HashMap<String , OurSession>();
	}

}
