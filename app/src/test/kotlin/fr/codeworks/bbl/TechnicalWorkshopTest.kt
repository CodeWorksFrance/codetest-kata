package fr.codeworks.bbl

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.*

internal class TechnicalWorkshopTest{
    lateinit var out: PrintStream
    val filePath = "src/test/resources/init"

    @BeforeEach
    fun setup(){
        out = System.out
        System.setOut(PrintStream(FileOutputStream("$filePath/lead.txt")))

    }
    @AfterEach
    fun destroy(){
        System.setOut(out)
    }

    @Test
    @DisplayName("Should make sure the last outputs matches gold")
    fun shouldRunMain() {
        val gold = BufferedReader(FileReader("$filePath/gold.txt"))
        val lead = BufferedReader(FileReader("$filePath/lead.txt"))

        main()

        var line: String?
        while (gold.readLine().also { line = it } != null) {
            Assertions.assertThat(line).isEqualTo(lead.readLine())
        }

    }
    @Test
    @DisplayName("Should extract input and expected output data from a test data file")
    fun shouldExtractTestData() {
        val testDataFileName = "src/test/resources/MyTestData.txt"
        val testDataInputFileName = "src/test/resources/MyTestData_input.txt"
        val testDataExpectedOutputFileName = "src/test/resources/MyTestData_expected_output.txt"
        val testDataFile = File(testDataFileName)
        testDataFile.bufferedWriter().use { out ->
            out.write("# a sample test data for testing purposes\n")
            out.write("< some input (first line)\n")
            out.write("> some matching output (first line)\n")
            out.write("# some more comment\n")
            out.write("< some more input (second line)\n")
            out.write("> some more matching output (second line)\n")
        }
        val extractor =  TestExtractorHelper()
        extractor.extractTestFiles(testDataFileName)
        Assertions.assertThat(File(testDataInputFileName).exists())
        val inputData = File(testDataInputFileName).readText(Charsets.UTF_8)
        Assertions.assertThat(inputData).isEqualTo("some input (first line)\nsome more input (second line)")
        Assertions.assertThat(File("test/resources/MyTestData_expected_output.txt").exists())
        val expectedOutputData = File(testDataExpectedOutputFileName).readText(Charsets.UTF_8)
        Assertions.assertThat(expectedOutputData).isEqualTo("some matching output (first line)\nsome more matching output (second line)")
    }
}
