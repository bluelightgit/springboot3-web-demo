package com.mySpring.demo.recommendation.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "main-server", url = "http://localhost:8085")
public interface VisitorServiceClient {

    @GetMapping("/visitors/UUID/{UUID}")
    boolean isBlankVisitor(@PathVariable("UUID") String UUID);

    @GetMapping("/visitor/{UUID}/distinctHistory")
    List<Long> getVisitorHistory(@PathVariable("UUID") String UUID);

}

