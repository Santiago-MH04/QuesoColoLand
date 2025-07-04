package com.quesocololand.msvcattractions.controllers;

import com.quesocololand.msvcattractions.services.abstractions.VisitorCountBatchImportService;
import com.quesocololand.msvcattractions.services.abstractions.VisitorCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/visitor-counts")
@Tag(
    name = "visitor counts",
    description = "visitor counts controller"
)
public class VisitorCountController {
    //Fields of VisitorCountController
    private final VisitorCountBatchImportService visitorCountBatchImportService;

    //Constructors of VisitorCountController
    public VisitorCountController(VisitorCountBatchImportService visitorCountBatchImportService) {
        this.visitorCountBatchImportService = visitorCountBatchImportService;
    }


    //Field setters of VisitorCountController (setters)
    //Field getters of VisitorCountController (getters)
    //Methods of VisitorCountController
    @PostMapping("/upload")
    @Operation(
        summary = "visitor counts uploader endpoint",
        description = "lists all the attractions in QuesoColoLand",
        tags = {"visitor counts", "upload"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "successful visitor count uploading",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = List.class)
                )
            )
        }
    )
    ResponseEntity<?> upload(@RequestParam(name = "file") MultipartFile multipartFile) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
        String dateFormatted = currentDate.format(formatter);

        Path resourcePath = Files.createTempFile("visitors-" + dateFormatted, ".csv");
        multipartFile.transferTo(resourcePath);
        this.visitorCountBatchImportService.batchImport(resourcePath);
        return ResponseEntity.ok("Visitor counts uploaded to database");
    }
}
