package fr.codeworks.bbl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.FileReader
import java.io.PrintStream

internal class TechnicalWorkshopTest{
    lateinit var out: PrintStream
    val filePath = "src/test/resources/init"

    @Nested
    inner class GoldenMaster {
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
                assertThat(line).isEqualTo(lead.readLine())
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
                assertThat(line).isEqualTo(lead.readLine())
            }
        }

        inner class TestableTechnicalWorkshop: TechnicalWorkshop(){

            override fun readTheUserResponseForPlaying() = "y"
            override fun readCodeworkerEvaluation() = "t"
            override fun readCandidateAnswersToQuestions() = "random"
        }
    }

    @Nested
    inner class CharacterizationTesting{

        @Test
        fun `Should return a score of 0 when the candidate refuses to play`(){

          val result = TechnicalWorkshop().runCodeTest("Java")

            assertThat(result).isEqualTo(0.0)
        }

        @Test
        fun `Should return a score of 2,5 when the candidates is half correct`(){
            val workshop = TestableTechnicalWorkshop()

            val fakeQ1 = Question("Q1", "R1", 2)
            val fakeQ2 = Question("Q2", "R2", 3)
            val fakeQ3 = Question("Q3", "R3", 3)

            val fakeR1 = CandidateResponse("R1", fakeQ1)
            val fakeR2 = CandidateResponse("R1", fakeQ2)
            val fakeR3 = CandidateResponse("R1", fakeQ3)

            val allResponses = mutableListOf(fakeR1, fakeR2, fakeR3)
            workshop.collectCandidatesAnswersToQuestions(listOf(fakeQ1,fakeQ2, fakeQ3), allResponses)

            val finalScore = workshop.runCodeTest("Java")
            assertThat(finalScore).isEqualTo(2.5)
        }

        inner class TestableTechnicalWorkshop: TechnicalWorkshop(){

            override fun readTheUserResponseForPlaying() = "y"
            override fun readCandidateAnswersToQuestions() = "random"
            override fun computeScore(responses: MutableList<CandidateResponse>): Double = 2.5
        }
    }



}
