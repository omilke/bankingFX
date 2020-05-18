package de.omilke.banking.interop.exporting

import org.apache.commons.io.IOUtils
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object LinesWriter {

    private val LOGGER: Logger = LogManager.getLogger(LinesWriter.javaClass)

    fun saveLinesToFile(file: File, lines: List<String>) {

        val path = file.toPath()
        try {
            val fos = FileOutputStream(file)
            IOUtils.writeLines(lines, IOUtils.LINE_SEPARATOR_WINDOWS, fos, Charsets.ISO_8859_1)
            fos.close()

            LOGGER.log(Level.INFO, "Completed writing {} lines to file {}", lines.size, path)
        } catch (e: IOException) {
            LOGGER.log(Level.WARN, "File could not be written to: {}", path, e)
        }
    }

}