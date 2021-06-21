package finder.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class OtherNumberFinder implements NumberFinder{

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(OtherNumberFinder.class);

    @Override
    public boolean contains(int valueToFind, List<CustomNumberEntity> numberEntityList) {
        boolean response = false;

        if(numberEntityList.isEmpty()) {
            return false;
        }

        long start = new Date().getTime();
        ExecutorService executor = Executors.newFixedThreadPool(numberEntityList.size());
        List<CompletableFuture<Integer>> completableFutures =
                numberEntityList.stream().filter(entity -> entity.getNumber() != null)
                .map(entity -> CompletableFuture.supplyAsync(() -> {
                        FastestComparator comparator = new FastestComparator();
                        return comparator.compare(valueToFind, entity);
                    }, executor))
                        .collect(toList());

        try {
            anyMatch(completableFutures, validationResponse -> validationResponse == 0).get();
            response = true;
        } catch (ExecutionException | InterruptedException | CompletionException e) {
            logger.debug("Number not found");
        }finally {
            executor.shutdown();
        }
        logger.info("Total time: "  + ( new Date().getTime() - start)* Math.pow(10, -3) + " sec.");
        return response;
    }

    @Override
    public List<CustomNumberEntity> readFromFile(String filePath) {
        try {
            String jsonArray = new String(Files.readAllBytes(Paths.get(filePath)));
            return objectMapper.readValue(jsonArray, new TypeReference<List<CustomNumberEntity>>(){});
        } catch ( JsonProcessingException e) {
            logger.debug("Failed to convert CustomerNumberEntity from json string");
        } catch (IOException e) {
            logger.debug("Failed to read json from file");
        }
        return Collections.emptyList();
    }

    public static CompletableFuture<Integer> anyMatch(
            List<? extends CompletionStage<Integer>> completionStages, Predicate<Integer> criteria) {

        CompletableFuture<Integer> result = new CompletableFuture<>();
        Consumer<Integer> whenMatching= value -> { if(criteria.test(value)) result.complete(value); };
        CompletableFuture.allOf(completionStages.stream()
                .map(feature -> feature.thenAccept(whenMatching))
                .toArray(CompletableFuture<?>[]::new))
                .whenComplete((ignored, throwable) ->
                        result.completeExceptionally(throwable!=null? throwable: new NoSuchElementException()));
        return result;
    }
}
