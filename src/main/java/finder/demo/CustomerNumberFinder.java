package finder.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class CustomerNumberFinder implements NumberFinder {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(CustomerNumberFinder.class);


    @Override
    public boolean contains(int valueToFind, List<CustomNumberEntity> numberEntityList) {

        if(numberEntityList.isEmpty())  return false;

        boolean result = false;

        ExecutorService executor = Executors.newFixedThreadPool(numberEntityList.size());
        List<FutureTask<Integer>>  tasks = new ArrayList<>();

        long start = new Date().getTime();

        for(CustomNumberEntity  entity: numberEntityList)
        {
            if(entity.getNumber() == null) continue;
            FutureTask<Integer> task = new FutureTask<>(new CompareTask(valueToFind, entity));
            tasks.add(task);
            executor.execute(task);
        }

        for(FutureTask<Integer>  task: tasks)
        {
            try {
                if(task.get()==0)
                {
                    result = true;
                    break;
                }
            } catch (InterruptedException e) {
                logger.debug("Comparison task was interrupted by other thread");
            } catch (ExecutionException e) {
                logger.debug("Internal error from comparison task");
            }
        }

        logger.info("Total time: "  + ( new Date().getTime() - start)* Math.pow(10, -3) + " sec.");

        executor.shutdown();

        return result;
    }

    @Override
    public List<CustomNumberEntity> readFromFile(String filePath) {
        String jsonArray = null;
        try {
            jsonArray = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            logger.error("Failed to read json from file");
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(jsonArray, new TypeReference<List<CustomNumberEntity>>(){});
        } catch ( JsonProcessingException e) {
            logger.debug("Failed to convert CustomerNumberEntity from json string");
        } catch (IOException e) {
            logger.debug("Failed to convert CustomerNumberEntity from json string");
        }
        return Collections.emptyList();
    }

    private class CompareTask implements Callable<Integer> {
        private Integer targetValue = 0;
        private CustomNumberEntity entity;

        public CompareTask(Integer target, CustomNumberEntity entity)
        {
            this.targetValue = target;
            this.entity = entity;
        }
        @Override
        public Integer call() throws Exception {
            FastestComparator comparator = new FastestComparator();
            return comparator.compare(targetValue, entity);
        }
    }
}
