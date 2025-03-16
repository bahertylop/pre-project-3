package org.backend.controller;

import lombok.RequiredArgsConstructor;
import org.backend.parsing.BrandModelParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestContoller {

    private final BrandModelParser brandModelParsing;

    @GetMapping("/qqq")
    public void parse() {
        brandModelParsing.parseBrandsModels();
    }
}
