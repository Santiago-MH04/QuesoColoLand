package com.quesocololand.msvcattractions.controllers;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.quesocololand.msvcattractions.models.dto.GroupedVisitorCountDTO;
import com.quesocololand.msvcattractions.services.abstractions.VisitorCountBatchImportService;
import com.quesocololand.msvcattractions.services.abstractions.VisitorCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/visitor-counts")
@RequiredArgsConstructor
@Slf4j
@Tag(
    name = "visitor counts",
    description = "visitor counts controller"
)
public class VisitorCountController {
    //Fields of VisitorCountController
    private final VisitorCountBatchImportService visitorCountBatchImportService;
    private final VisitorCountService visitorCountService;

    //Constructors of VisitorCountController
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

    @GetMapping("/aggregated")
    @Operation(
        summary = "grouped by interval visitor counts endpoint",
        description = "lists the attendance to an attraction on a given time-interval in QuesoColoLand",
        tags = {"visitor attendance", "grouped", "time-interval", "get"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "successful grouped visitor count read",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = List.class)
                )
            )
        }
    )
    public ResponseEntity<?> getGroupedVisitorCounts(
            @RequestParam String attractionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, // Format "YYYY-MM-DD"
            @RequestParam(defaultValue = "10") int intervalMinutes) {
        if (!List.of(5, 10, 15, 30, 60).contains(intervalMinutes)) {
            log.error("Interval must be 5, 15, 30, or 60 minutes");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The defined interval must be either 5, 15, 30, or 60 minutes");
        }
        List<GroupedVisitorCountDTO> groupedVisitorCounts = this.visitorCountService.getGroupedVisitorCounts(attractionId, date, intervalMinutes);

        return ResponseEntity.ok(groupedVisitorCounts);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportGroupedVisitorCountsToCsv(
        @RequestParam String attractionId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam int intervalMinutes) {
        log.info("Request to export grouped visitor counts to CSV for attractionId: {}, date: {}, interval: {} minutes", attractionId, date, intervalMinutes);

        try {
            // 1. Generar el contenido CSV usando el método del servicio (OpenCSV uses this)
            String csvFile = this.visitorCountService.getCsvFile(attractionId, date, intervalMinutes);

            // 2. Preparar los encabezados de la respuesta HTTP para la descarga del CSV
            HttpHeaders headers = new HttpHeaders();
            String filename = String.format("visitor_counts_%s_%s_%dmin.csv",
                attractionId,
                date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                intervalMinutes
            );
                headers.setContentType(MediaType.parseMediaType("text/csv"));
                headers.setContentDispositionFormData("attachment", filename);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            // 3. Devolver la respuesta con el contenido CSV en bytes
            return new ResponseEntity<>(csvFile.getBytes(), headers, HttpStatus.OK);

        } catch (CsvRequiredFieldEmptyException |
                 CsvDataTypeMismatchException |
                 IOException |
                 ExecutionException |
                 InterruptedException e
        ) {
            log.error("Error during CSV generation/export for attractionId: {}", attractionId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
