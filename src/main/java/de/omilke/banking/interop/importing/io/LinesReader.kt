package de.omilke.banking.interop.importing.io

import org.apache.commons.io.IOUtils
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

object LinesReader {

    private val LOGGER = LogManager.getLogger(LinesReader::class.java)

    /**
     * Reads the content of a file as given by the nullable path.
     *
     * Returns an empty list in case the path is null
     * @see readLinesFromFile
     */
    fun readLinesFromPath(filePath: String?, charset: Charset = StandardCharsets.UTF_8): List<String> {

        return if (filePath != null) {
            return readLinesFromFile(File(filePath), charset)
        } else {
            ArrayList()
        }
    }

    /**
     * Reads the content of a file.
     *
     * Returns an empty list in case
     * - the path is invalid
     * - there has been a error reading the file
     */
    fun readLinesFromFile(file: File, charset: Charset = StandardCharsets.UTF_8): List<String> {

        LOGGER.log(Level.TRACE, "Reading content from {} using {}", file.path, charset)

        return try {
            IOUtils.readLines(FileInputStream(file), charset)
        } catch (ex: IOException) {

            LOGGER.log(Level.ERROR, "Error reading file : {} [using {}]", file.path, charset, ex)
            ArrayList()
        }
    }
}