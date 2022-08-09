package be.ehb.gdt.jrivia

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.ehb.gdt.jrivia.models.DailyQuest
import be.ehb.gdt.jrivia.room.DailyQuestRepository
import be.ehb.gdt.jrivia.room.JriviaRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("be.ehb.gdt.jrivia", appContext.packageName)
    }

    @Test
    fun isDailyQuestTableSeeded() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val coroutineScope = CoroutineScope(SupervisorJob())
        val database = JriviaRoomDatabase.getDatabase(appContext, coroutineScope)
        val repository = DailyQuestRepository(database.dailyQuestDao())

        val quest = DailyQuest(1,"It recently moved ahead of Chicago to become our 2nd largest", "Los Angeles", 100)

        coroutineScope.launch {
            repository.getLastQuest().collect {
                assertEquals(quest, it)
            }
        }
    }
}