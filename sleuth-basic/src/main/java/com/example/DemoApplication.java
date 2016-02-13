package com.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@RestController
class SleuthMessageRestController {

	private final RestTemplate restTemplate;
	private final SpanAccessor spanAccessor;

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	SleuthMessageRestController(RestTemplate restTemplate,
								SpanAccessor spanAccessor) {
		this.restTemplate = restTemplate;
		this.spanAccessor = spanAccessor;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{domainDotCom}")
	ResponseEntity<?> call(@PathVariable String domainDotCom) {
		try {
			URI uri = URI.create("http://" + domainDotCom + ".com");
			ResponseEntity<String> response = this.restTemplate.getForEntity(uri, String.class);
			String body = response.getBody();
			MediaType mediaType = response.getHeaders().getContentType();
			return ResponseEntity.ok()
					.contentType(mediaType)
					.body(body);
		} finally {
			debug();
		}
	}

	private void debug() {
		Span span = this.spanAccessor.getCurrentSpan();
		this.log.info(String.format("traceId: %s, spanId: %s",
				span.getTraceId(), span.getSpanId()));
	}
}
