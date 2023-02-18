import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.bigsy_previsodotempo.R
import com.google.gson.annotations.SerializedName
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {

    private var cityesco: String = ""
    private var progressbarcirc = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        supportActionBar?.setCustomView(R.layout.titlebar);


        val button = findViewById<Button>(R.id.button)
        val input = findViewById<EditText>(R.id.editTextText)

        val progressValue = findViewById<TextView>(R.id.progress_value)
        val progressBar = findViewById<CircularProgressBar>(R.id.progressBar2)
        progressBar.startAngle = 180f

        progressValue.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                val value = progressValue.text.toString().toIntOrNull()
                if (value != null){
                    progressBar.progress = value.toFloat()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        button.setOnClickListener{
            val value = input.text.toString().toIntOrNull()
            if (value != null){
                progressValue.text = value.toString()
                progressBar.setProgressWithAnimation(value.toFloat(), 1000)
            }
        }

        val searchView = findViewById<SearchView>(R.id.searchviewcidade)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotBlank()) {
                    cityesco = query
                    starttemp()
                        }
                return true
            }

            private fun starttemp() {
                if (cityesco != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val weatherResponse = weatherService.getCurrentWeather(cityesco!!, API_KEY)
                            runOnUiThread {
                                val temperatura = weatherResponse.main.temp.toInt()
                                progressValue.text = temperatura.toString()
                                progressBar.setProgressWithAnimation(temperatura.toFloat(), 1000)
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                progressValue.text = "X"
                                progressBar.progress = 0f
                            }
                        }
                    }
                }
            }


            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }
}

data class WeatherResponse(
    @SerializedName("main")
    val main: Main,
){
    data class Main(
        @SerializedName("temp")
        val temp: Double,
    )
}

interface WeatherService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): WeatherResponse
}

private const val API_KEY = "6XXXXXXXXXXX"
private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openweathermap.org/data/2.5/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private val weatherService = retrofit.create(WeatherService::class.java)
