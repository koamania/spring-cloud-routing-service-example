package com.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@EnableDiscoveryClient
@SpringBootApplication
public class GreetingsServiceApplication {

		private final Log log = LogFactory.getLog(getClass());

//		@Bean
//		RouterFunction<ServerResponse> routes() {
//				return route(GET("/hi/{name}"), req ->
//					ok().body(fromObject(Collections
//						.singletonMap("greeting", String.format("Hello, %s!", req.pathVariable("name")))))
//				);
//		}
//
//		private void debug(ServerRequest request) {
//				String str = ToStringBuilder.reflectionToString(request);
//				log.info("request: " + str);
//				request.headers().asHttpHeaders().forEach((k, v) -> log.info(k + '=' + v));
//		}

		public static void main(String[] args) {
				SpringApplication.run(GreetingsServiceApplication.class, args);
		}
}

@RestController
class GreetingsRestController {

	@GetMapping("/hi/{name}")
	Map<String, String> hi(@PathVariable String name, @RequestHeader(value = "X-CNJ-Name", required = false) Optional<String> cn) {
		LogFactory.getLog(GreetingsRestController.class).info("wow: greetings service");
		String resolvedName = cn.orElse(name);
		return Collections.singletonMap("greeting", "Hello, " + resolvedName + "!");
	}
}