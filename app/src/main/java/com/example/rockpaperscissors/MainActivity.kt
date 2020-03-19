package com.example.rockpaperscissors

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.rockpaperscissors.ResultActivitiy.Result
import com.example.rockpaperscissors.Database.ResultRepository
import com.example.rockpaperscissors.ResultActivitiy.ResultActivity
import com.example.rockpaperscissors.UI.Companion.getDrawableFromChoice
import kotlinx.coroutines.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

/**
 * @author Omar Mulla Ibrahim
 * Student Nr 500766035
 */
class MainActivity : AppCompatActivity() {
    lateinit var resultRepository: ResultRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        resultRepository = ResultRepository(this)
        initViews()
    }

    private fun initViews() {
        tvStatstics.text = getResults()
        tvWinLoseMessage.text = ""
        imagePaper.setOnClickListener { gameProces(Options.PAPER) }
        imageRock.setOnClickListener { gameProces(Options.Rock) }
        imageScissors.setOnClickListener { gameProces(Options.SCISSOR) }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_history -> {

                startResultActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // here we save the results the database starting a Coroutine
    private fun saveResultInDatabase(result: Result) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                //here we insert the result using the repository
                resultRepository.insertResult(result)
            }
        }
    }

    //the image will be updated when the imagebtn clicked
    private fun updateUI(result: Result) {
        ivPlayerChoice.setImageResource(getDrawableFromChoice(result.playerChoice))
        ivComputerChoice.setImageResource(getDrawableFromChoice(result.computerChoice))
        tvWinLoseMessage.text = result.winner
        tvStatstics.text = getResults()
    }

    // execute Game after the player chose
    private fun gameProces(playerChoice: Options) {
        val choiceComputer = RandomEnum.randomEnum()
        val result = match(playerChoice, choiceComputer)
        // update images after the player choose
        updateUI(result)
        saveResultInDatabase(result)

    }

    // Check who is the winner
    private fun getWinner(playerChoice: Options, computerChoice: Options): String {
        if (playerChoice.toString() == computerChoice.toString()) {
            return "DRAW"
        }
        if (playerChoice == Options.PAPER && computerChoice == Options.Rock ||
            playerChoice == Options.Rock && computerChoice == Options.SCISSOR ||
            playerChoice == Options.SCISSOR && computerChoice == Options.PAPER
        ) {
            return "You win!!"
        }
        return "Computer wins!"
    }

    private fun getResults(): String {
        var resultString = ""
        runBlocking(Dispatchers.IO) {
            val wins = async {
                resultRepository.getNumberOfWins("You win!!").toString()
            }
            val loses = async {
                resultRepository.getNumberOfWins("Computer wins!").toString()
            }
            val draws = async {
                resultRepository.getNumberOfWins("DRAW").toString()
            }
            wins.await() + loses.await() + draws.await()
            resultString =
                "Wins : " + wins.getCompleted() + " Loses : " + loses.getCompleted() + " Draws : " + draws.getCompleted()

        }


        return resultString
    }

    // here we go to Result page
    private fun startResultActivity() {
        val intent = Intent(this@MainActivity, ResultActivity::class.java)
        startActivity(intent)

    }

    //  here is the process of the match
    private fun match(playerChoiche: Options, computerChoiche: Options): Result {
        val winner = getWinner(playerChoiche, computerChoiche)
        return Result(
            playerChoiche.toString(),
            computerChoiche.toString(),
            winner,
            Calendar.getInstance().time.toString()
        )
    }
}
