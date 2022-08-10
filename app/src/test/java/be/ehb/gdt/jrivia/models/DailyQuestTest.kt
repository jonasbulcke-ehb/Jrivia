package be.ehb.gdt.jrivia.models

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class DailyQuestTest {
    private val question = "What is the name of the first president of the U.S.A."
    private val answer = "George Washington"

    @Test
    fun isFromToday_returnsTrue() {
        val dailyQuest =
            DailyQuest(1, question, answer, 100, System.currentTimeMillis() - 12 * 60 * 60 * 1000)

        assertTrue(dailyQuest.isFromToday())
    }

    @Test
    fun isFromToday_returnsFalse() {
        val dailyQuest =
            DailyQuest(1, question, answer, 100, System.currentTimeMillis() - 25 * 60 * 60 * 1000)

        assertFalse(dailyQuest.isFromToday())
    }


}