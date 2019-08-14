package com.example;

import com.netflix.loadbalancer.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class RibbonCLR {

    private final DiscoveryClient discoveryClient;
    private final Log log = LogFactory.getLog(getClass());

    public RibbonCLR(final DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/test2")
    public void run(final String... args) {
        String serviceId = "greetings-service";

        List<Server> servers = this.discoveryClient
                .getInstances(serviceId).stream()
                .map(si -> new Server(si.getHost(), si.getPort()))
                .collect(Collectors.toList());

        IRule roundRobinRule = new RoundRobinRule();

        BaseLoadBalancer loadBalancer = LoadBalancerBuilder.newBuilder()
                .withRule(roundRobinRule)
                .buildFixedServerListLoadBalancer(servers);

        IntStream.range(0, 10).forEach(i -> {
            Server server = loadBalancer.chooseServer();
            URI uri = URI.create("http://" + server.getHost() + ":" + server.getPort() + "/");

            log.info("resolved service " + uri.toString());
        });
    }
}
