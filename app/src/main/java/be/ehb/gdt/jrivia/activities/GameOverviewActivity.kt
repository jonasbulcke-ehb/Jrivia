package be.ehb.gdt.jrivia.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import be.ehb.gdt.jrivia.R
import be.ehb.gdt.jrivia.viewmodels.GameViewModel
import be.ehb.gdt.jrivia.util.IntentExtraNames
import com.google.android.material.snackbar.Snackbar

class GameOverviewActivity : AppCompatActivity() {
    private val gameViewModel: GameViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_overview)

        supportActionBar?.title = getString(R.string.single_player)

        try {
            gameViewModel.game = intent.getParcelableExtra(IntentExtraNames.GAME)
                ?: throw IllegalStateException(getString(R.string.game_overview_error))
        } catch (e: IllegalStateException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            finish()
        }
    }
}