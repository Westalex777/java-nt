package org.example.appntloader.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.appntloader.service.NTLoaderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class NTLoaderController {

    private final NTLoaderService ntLoaderService;

    @GetMapping("/load")
    public void load(@RequestParam(name = "k", required = false, defaultValue = "0") Integer k) throws ExecutionException, InterruptedException {
        log.info("Request /load");
        ntLoaderService.load(k);
    }

}
