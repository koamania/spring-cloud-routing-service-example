package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@EnableDiscoveryClient
@SpringBootApplication
public class CustomService {

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
				SpringApplication.run(CustomService.class, args);
		}
}

@RestController
class CustomRestController {

	private final RestTemplate restTemplate;

	CustomRestController(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("/hi/{name}")
	Map<String, String> hi(@PathVariable String name, @RequestHeader(value = "X-CNJ-Name", required = false) Optional<String> cn) {
		LogFactory.getLog(CustomRestController.class).info("wow: custom service");
		String resolvedName = cn.orElse(name);
		return Collections.singletonMap("greeting", "Hello, " + resolvedName + "!");
	}

	@GetMapping("/test3/{name}")
	public void test3(@PathVariable String name) {
		Map<String, String> variables = Collections.singletonMap("name", name);

		ResponseEntity<JsonNode> response = this.restTemplate
				.getForEntity("//greetings-service/hi/{name}", JsonNode.class, variables);

		JsonNode body = response.getBody();
		String greeting = body.get("greeting").asText();
		LogFactory.getLog(CustomRestController.class).info("greeting: " + greeting);
	}

}