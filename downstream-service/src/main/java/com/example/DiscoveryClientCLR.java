package com.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DiscoveryClientCLR {

    private final DiscoveryClient discoveryClient;
    private Log log = LogFactory.getLog(getClass());

    @Autowired
    public DiscoveryClientCLR(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/test1")
    public void run() throws Exception {
        this.log.info("localServiceInstance");
        this.discoveryClient.getServices()
                .forEach(this::logServiceInstance);

        String serviceId = "greetings-service";
        this.log.info(String.format("registered instance of '%s'", serviceId));
        this.discoveryClient.getInstances(serviceId)
                .forEach(this::logServiceInstance);

        String test = ("asd");
        this.log.info(test);
    }

    private void logServiceInstance(String serviceId) {
        discoveryClient.getInstances(serviceId)
                .forEach(this::logServiceInstance);

    }

    private void logServiceInstance(ServiceInstance si) {
        String msg = String.format("host = %s, port = %s, service ID = %s", si.getHost(), si.getPort(), si.getServiceId());
        this.log.info(msg);
    }

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryClientCLR.class, args);
    }
}
