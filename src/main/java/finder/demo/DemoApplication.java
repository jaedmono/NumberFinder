package finder.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String... args){
        NumberFinder numberFinder = new OtherNumberFinder();
        List<CustomNumberEntity> customNumberEntities = numberFinder.readFromFile(args[1]);
        boolean response = numberFinder.contains(Integer.valueOf(args[0]), customNumberEntities);
        if(response) {
            logger.info("Number found ");
        }else{
            logger.info("Number NOT found ");
        }
    }
}
