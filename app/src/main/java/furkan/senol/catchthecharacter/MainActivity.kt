package furkan.senol.catchthecharacter

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore.Audio.Media
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

private lateinit var alert: AlertDialog.Builder
private lateinit var alert2: AlertDialog.Builder

private var highScoreFromPreferences: Int = 0
private var scoreTotal: Int = 0

private lateinit var imageViewArray: ArrayList<ImageView>

private var handler = Handler(Looper.getMainLooper())
private var runnable = Runnable { }

private lateinit var sharedPreferences: SharedPreferences

private lateinit var music:MediaPlayer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //music=MediaPlayer.create(this@MainActivity,R.raw.sound)

        scoreTotal = 0

        sharedPreferences =
            this.getSharedPreferences("furkan.senol.catchthecharacter", MODE_PRIVATE)
        highScoreFromPreferences = sharedPreferences.getInt("highScore", -1)

        if (highScoreFromPreferences == -1)
            highScoreText.text = "High Score: 0"
        else
            highScoreText.text = "High Score: $highScoreFromPreferences"

        alert = AlertDialog.Builder(this@MainActivity)
        alert2 = AlertDialog.Builder(this@MainActivity)

        imageViewArray = ArrayList<ImageView>()

        imageViewArray.add(imageView1)
        imageViewArray.add(imageView2)
        imageViewArray.add(imageView3)
        imageViewArray.add(imageView4)
        imageViewArray.add(imageView5)
        imageViewArray.add(imageView6)
        imageViewArray.add(imageView7)
        imageViewArray.add(imageView8)
        imageViewArray.add(imageView9)

        hideImages()

        object : CountDownTimer(14600, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timerText.setText("Time: " + millisUntilFinished / 1000)
                //music.start()

            }

            override fun onFinish() {
                //music.stop()
                scoreText.text = "Score: 0"


                if (scoreTotal > highScoreFromPreferences) {
                    sharedPreferences.edit().putInt("highScore", scoreTotal).apply()

                    alert2.setTitle("Congratulations!")
                        .setMessage("New Record : $scoreTotal")
                        .setPositiveButton("Okey") { dialog, it ->
                            val mainPage = Intent(this@MainActivity, MainActivity::class.java)
                            startActivity(mainPage)
                            finish()
                        }.show()
                } else {

                    alert.setTitle("Time is Over")
                        .setMessage("Do you want to play again?")
                        .setPositiveButton("Yes") { dialog, it ->

                            val mainPage = Intent(this@MainActivity, MainActivity::class.java)
                            startActivity(mainPage)
                            finish()
                        }
                        .setNegativeButton("No") { dialog, it ->
                            Toast.makeText(this@MainActivity, "Game Over!!", Toast.LENGTH_SHORT)
                                .show()
                        }.show()

                }
                handler.removeCallbacks(runnable)

                for (image in imageViewArray) {
                    image.visibility = View.INVISIBLE
                }


            }
        }.start()
    }

    fun hideImages() {
        runnable = object : Runnable {
            override fun run() {
                for (image in imageViewArray) {
                    image.visibility = View.INVISIBLE
                }

                val random = Random()
                val randomIndex = random.nextInt(9)
                imageViewArray[randomIndex].visibility = View.VISIBLE

                handler.postDelayed(runnable, 500)
            }
        }
        handler.post(runnable)
    }

    fun scoreCount(view: View) {
        scoreTotal += 1
        scoreText.text = "Score: " + scoreTotal
    }


}