package fr.codeworks.bbl

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.FileReader
import java.io.PrintStream

internal class TechnicalWorkshopTest{
    lateinit var out: PrintStream
    val filePath = "src/test/resources/init"

    @BeforeEach
    fun setup(){
        out = System.out

    }
    @AfterEach
    fun destroy(){
        System.setOut(out)
    }

    @Test
    fun `Should make sure the last outputs matches gold when the candidate refuses to play`() {
        System.setOut(PrintStream(FileOutputStream("$filePath/lead.txt")))

        val gold = BufferedReader(FileReader("$filePath/gold.txt"))
        val lead = BufferedReader(FileReader("$filePath/lead.txt"))

        main()

        var line: String?
        while (gold.readLine().also { line = it } != null) {
            Assertions.assertThat(line).isEqualTo(lead.readLine())
        }
    }

    @Test
    fun `Should make sure the last output matches the perfect score`(){
        System.setOut(PrintStream(FileOutputStream("$filePath/lead_perfect_score.txt")))
        val gold = BufferedReader(FileReader("$filePath/gold_perfect_score.txt"))
        val lead = BufferedReader(FileReader("$filePath/lead_perfect_score.txt"))

        runTheWholeGame(TestableTechnicalWorkshop())

        var line: String?
        while (gold.readLine().also { line = it } != null) {
            Assertions.assertThat(line).isEqualTo(lead.readLine())
        }
    }

    inner class TestableTechnicalWorkshop: TechnicalWorkshop(){

        override fun readTheUserResponseForPlaying() = "y"
        override fun readCodeworkerEvaluation() = "t"
        override fun readCandidateAnswersToQuestions() = "random"
    }
}
