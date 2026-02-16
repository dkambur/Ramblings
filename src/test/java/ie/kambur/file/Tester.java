package ie.kambur.file;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tester {
    protected static final Logger logger = LogManager.getLogger(Tester.class);

    public static void main(String[] args) throws Exception  {
        File f = new File("/home/dalen/Downloads/../Downloads/muppet.zip");


        try (AtomicRenamer abc = new AtomicRenamer(f);
             FileOutputStream fos = new FileOutputStream(abc.getFile()) ) {
            Random rnd = new Random();

            for (int i=0; i<50*102400; i++) {
                fos.write('a' + rnd.nextInt(10));
            }

            abc.successful();
            logger.info ("Completed");
        }
    }
}

