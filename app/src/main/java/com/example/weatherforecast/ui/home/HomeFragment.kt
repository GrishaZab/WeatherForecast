package com.example.weatherforecast.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.weatherforecast.R
import com.example.weatherforecast.retrofit.Common
import com.example.weatherforecast.models.weather.WeatherParameters
import com.example.weatherforecast.retrofit.OpenApi
import com.example.weatherforecast.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Response
import kotlin.math.roundToInt

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherService: OpenApi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val city: TextView = binding.city
        val cloudiness: TextView = binding.cloudiness
        val main: TextView = binding.main
        val humidity: TextView = binding.humidity
        val windSpeed: TextView = binding.windSpeed
        val description: TextView = binding.description
        val pressure: TextView = binding.pressure
        val temperature: TextView = binding.temperature
        val image: ImageView = binding.image
        val change: Button = binding.changeCity
        val search: SearchView = binding.search

        var cityValue = "Moscow"

        change.setOnClickListener{
            cityValue = search.query.trim().toString()
            updateCity(cityValue, image,
                temperature, cloudiness,
                main, description,
                humidity, windSpeed,
                pressure, city)
        }

        updateCity(cityValue, image,
            temperature, cloudiness,
            main, description,
            humidity, windSpeed,
            pressure, city)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun updateCity(cityValue: String,
                   image: ImageView,
                   temperature: TextView,
                   cloudiness: TextView,
                   main: TextView,
                   description: TextView,
                   humidity: TextView,
                   windSpeed: TextView,
                   pressure: TextView,
                   city: TextView){
        weatherService = Common.retrofitServiceWeather
        weatherService.getWeatherForecast(cityValue, getString(R.string.part_of_url))
            .enqueue(object : retrofit2.Callback<WeatherParameters> {
                override fun onResponse(
                    call: Call<WeatherParameters>,
                    response: Response<WeatherParameters>
                ) {
                    if(response.isSuccessful) {
                        val number = (((response.body()?.main?.temp!!.toFloat() - 273.15) * 100.0).roundToInt() / 100.0).toString()
                        val temp = "$number°C"
                        val cloud = "Cloudiness: " + response.body()?.clouds?.all.toString() + "%"
                        val mainDescription = response.body()?.weather?.get(0)?.main.toString()
                        val desc = response.body()?.weather?.get(0)?.description.toString()
                        val hum = "Humidity: " + response.body()?.main?.humidity.toString() + "%"
                        val wind = "Wind speed: " + response.body()?.wind?.speed.toString() + "m/s"
                        val press = "Pressure: " + response.body()?.main?.pressure.toString() + "hPa"

                        Log.d("TEST", "https://openweathermap.org/img/wn/${response.body()?.weather?.get(0)?.icon}@4x.png")
                        image.load("https://openweathermap.org/img/wn/${response.body()?.weather?.get(0)?.icon}@4x.png")

                        temperature.text = temp
                        cloudiness.text = cloud
                        main.text = mainDescription
                        description.text = desc
                        humidity.text = hum
                        windSpeed.text = wind
                        pressure.text = press
                        city.text = cityValue
                    }else{
                        Toast.makeText(context, "Can not find this city", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<WeatherParameters>, t: Throwable) {
                    Log.d("RETROFIT", "Error in weather")
                }
            })
    }
}