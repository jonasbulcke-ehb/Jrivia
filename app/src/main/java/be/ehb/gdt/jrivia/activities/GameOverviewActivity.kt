package be.ehb.gdt.jrivia.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.models.viewmodels.GameViewModel
import be.ehb.gdt.jrivia.util.IntentExtraNames

class GameOverviewActivity : AppCompatActivity() {
    private val gameViewModel: GameViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_overview)

        try {
            gameViewModel.game = intent.getParcelableExtra(IntentExtraNames.GAME)
                ?: throw IllegalStateException("Something went wrong while trying to finish the game")
        } catch (e: IllegalStateException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
}