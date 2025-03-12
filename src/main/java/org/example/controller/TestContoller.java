package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.parsing.BrandModelParsing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestContoller {

    private final BrandModelParsing brandModelParsing;

    @GetMapping("/qqq")
    public void parse() {
        brandModelParsing.parseBrandsModels();
    }
}
