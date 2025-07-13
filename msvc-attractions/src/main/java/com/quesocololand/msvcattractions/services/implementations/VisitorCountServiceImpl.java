package com.quesocololand.msvcattractions.services.implementations;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.quesocololand.msvcattractions.models.Attraction;
import com.quesocololand.msvcattractions.models.VisitorCount;
import com.quesocololand.msvcattractions.models.dto.GroupedVisitorCountDTO;
import com.quesocololand.msvcattractions.repositories.AttractionRepository;
import com.quesocololand.msvcattractions.repositories.VisitorCountRepository;
import com.quesocololand.msvcattractions.services.abstractions.VisitorCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class VisitorCountServiceImpl implements VisitorCountService {
    //Fields of VisitorCountServiceImpl
    private final VisitorCountRepository repoVisitorCount;
    private final AttractionRepository repoAttraction;
    public static final String CSV_HEADERS = "Attraction ID,Attraction name,Interval start,Attendance";

    //Methods of VisitorCountServiceImpl
    @Override
    public List<VisitorCount> findByAttractionId(String attractionId) {
        return this.repoVisitorCount.findByAttractionId(attractionId);
    }

    @Override
    public List<VisitorCount> findByTimestampOn(LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return this.repoVisitorCount.findByTimestampBetween(startOfDay, endOfDay);
    }

    @Override
    @Transactional
    public VisitorCount save(VisitorCount visitorCount) {
        return this.repoVisitorCount.save(visitorCount);
    }

    @Override
    @Transactional
    public void saveAll(List<VisitorCount> visitorCountList) {
        this.repoVisitorCount.saveAll(visitorCountList);
    }


    @Override
    @Transactional(readOnly = true)
    public List<GroupedVisitorCountDTO> getGroupedVisitorCounts(String attractionId, LocalDate date, int intervalMinutes) {
        // Define the date range for the specific day (from 00:00:00 to 23:59:59)
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // This yields 23:59:59.999999999
        return this.repoVisitorCount.getGroupedVisitorCounts(attractionId, startOfDay, endOfDay, intervalMinutes);
    }

    @Transactional(readOnly = true)
    public Optional<String> getAttractionNameById(String id){
        return this.repoAttraction.findById(id)
            .map(Attraction::getName);
    }

    public String generateCsv(List<GroupedVisitorCountDTO> groupedCounts) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        if (groupedCounts == null /*|| groupedCounts.isEmpty()*/) {
            // Generate the headers in case there’s no data
            StringWriter headerWriter = new StringWriter();
            try (CSVWriter csvWriter = new CSVWriter(headerWriter)) {
                return CSV_HEADERS;
            }
        }

       StringWriter writer = new StringWriter();

       try (CSVWriter csvWriter = new CSVWriter(
                writer,
                ICSVWriter.DEFAULT_SEPARATOR,         // Delimiter (comma by default)
                ICSVWriter.NO_QUOTE_CHARACTER,      // <-- CORRECTED! Quote character
                ICSVWriter.DEFAULT_ESCAPE_CHARACTER,  // Escape character
                ICSVWriter.DEFAULT_LINE_END          // End of a line character
       )) {
            StatefulBeanToCsv<GroupedVisitorCountDTO> beanToCsv = new StatefulBeanToCsvBuilder<GroupedVisitorCountDTO>(csvWriter)
                .withOrderedResults(true) // It ensures the columns will be written in the order of the annotations
                .build();

            beanToCsv.write(groupedCounts); // It writes the whole bean list
       } // The try-with-resources automatically closes the writer and the csvWriter
       return writer.toString();
    }

    @Override
    public String getCsvFile(String attractionId, LocalDate date, int intervalMinutes) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException, ExecutionException, InterruptedException {
        //Make concurrent magic using CompletableFuture for steps 1 and 2
        //1. Get attractionName
        CompletableFuture<String> columnNameCompletable = CompletableFuture.supplyAsync(this.getAttractionNameById(attractionId)::get);

        //2. Get grouped VisitorCounts
        CompletableFuture<List<GroupedVisitorCountDTO>> listCompletable = CompletableFuture.supplyAsync(() -> this.getGroupedVisitorCounts(attractionId, date, intervalMinutes));

        // Wait for both calls to complete
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(columnNameCompletable, listCompletable);
            combinedFuture.join();

        // Get the results from the CompletableFutures
        String attractionName = columnNameCompletable.get();
        List<GroupedVisitorCountDTO> visitorCountDTOList = listCompletable.get();

        //3. Set the attraction name for this
        visitorCountDTOList.forEach(vc -> vc.setAttractionName(attractionName));

        //4. Return the list
        return this.generateCsv(visitorCountDTOList);
    }
}
