package be.ehb.gdt.jrivia.room

import androidx.test.platform.app.InstrumentationRegistry
import be.ehb.gdt.jrivia.models.DailyQuest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.junit.Assert.*
import org.junit.Test

class JriviaDatabaseCallbackTest {
    @Test
    fun isDailyQuestTableSeeded() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val coroutineScope = CoroutineScope(SupervisorJob())
        val database = JriviaRoomDatabase.getDatabase(appContext, coroutineScope)
        val repository = DailyQuestRepository(database.dailyQuestDao())

        val quest = DailyQuest(
            1,
            "It recently moved ahead of Chicago to become our 2nd largest",
            "Los Angeles",
            100
        )

        coroutineScope.launch {
            repository.getLastQuest().collect {
                assertEquals(quest, it)
            }
        }
    }
}