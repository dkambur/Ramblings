package ie.kambur.file;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AtomicRenamer implements AutoCloseable {
    protected static final Logger logger = LogManager.getLogger(AtomicRenamer.class);

    private File theFinalFile;
    private File temporaryFile;

    private boolean successful = false;

    /**
     * @param targetFile
     * @param prefix
     * @param postfix
     * @throws IOException
     */
    public AtomicRenamer (File targetFile, String prefix, String postfix) throws IOException {
        // not needed but makes paths shorter
        theFinalFile = targetFile.getCanonicalFile();

        temporaryFile = new File ( theFinalFile.getParentFile(), prefix + theFinalFile.getName() + postfix);
        logger.debug ("Creating '" + temporaryFile + "' for " + theFinalFile + "'");
    }

    /**
     * Create AtomicRenamer with default pre and postfix ($)
     * @param targetFile
     * @throws IOException
     */
    public AtomicRenamer (File targetFile) throws IOException {
        this (targetFile, "$", "$");
    }

    /**
     * @return the file
     */
    public File getFile() {
        return temporaryFile;
    }

    public void successful() {
        successful = true;
    }

    @Override
    public void close() throws Exception {
        if (successful) {
            final String status = "'" + temporaryFile + "' to '" + theFinalFile + "'";

            boolean success = temporaryFile.renameTo (theFinalFile);

            if (success)
                logger.debug ("Renamed " + status);
            else
                throw new Exception ("Couldn't rename " + status);
        }
        else {
            boolean success = temporaryFile.delete();

            if (!success)
                logger.warn ("Deletion failed of temporary file: '" + temporaryFile + "'" );
        }
    }
}
