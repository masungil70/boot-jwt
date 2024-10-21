package org.kosa.jwt.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "sample", description = "SampleContrroller 관련 API 입니다.")
@RestController
@RequestMapping("/api/sample")
public class SampleController {

	@Operation(
			summary = "sample get doA",
		    description = "sample doA api를 호출합니다"
	)
	@ApiResponse(
	        responseCode = "200",
	        description = "sample doA 호출을 성공하였습니다."
	    )

	@GetMapping("/doA")
    public List<String> doA() {
        return Arrays.asList("AAA","BBB","CCC");
    }

}
