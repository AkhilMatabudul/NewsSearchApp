package com.akhil.newssearchapp.activities

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.akhil.newssearchapp.R
import com.akhil.newssearchapp.adapters.AllNewsAdapter
import com.akhil.newssearchapp.databinding.ActivityMainBinding
import com.akhil.newssearchapp.modals.Article
import com.akhil.newssearchapp.utils.AppConstants
import com.akhil.newssearchapp.utils.RetrofitInstance
import com.akhil.newssearchapp.viewmodels.AllNewViewModel
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(),OnInitListener {

    // Variable declarations and initialisations
    lateinit var binding: ActivityMainBinding
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var viewModel: AllNewViewModel
    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0
    private var finalSearch=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Change statusBar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.light_blue_600)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel initialisation
        viewModel = ViewModelProvider(this).get(AllNewViewModel::class.java)

        // News data observer from viewModel
        viewModel.allNewsList.observe(this, { posts ->
            binding.progressHorizontal.visibility = View.GONE
            binding.notFound.visibility = View.GONE
            binding.recyclerview.visibility = View.VISIBLE
            var articleList:MutableList<Article> = mutableListOf()
            if (posts.articles.size<1){
                binding.notFound.visibility = View.VISIBLE
            }else{
                for (article in posts.articles){
                    Log.d("zzzzzz","${finalSearch} ${article.title.toLowerCase()}")

                    if (article.title.toLowerCase().contains(finalSearch.toLowerCase())){
                        articleList.add(article)
                    }
                }
            }
            if (articleList.size<1){
                binding.notFound.visibility = View.VISIBLE
            }
            // Create a DividerItemDecoration and set it to the recyclerview
            val dividerItemDecoration = DividerItemDecoration(
                binding.recyclerview.getContext(),
                LinearLayoutManager.VERTICAL
            )
            binding.recyclerview.addItemDecoration(dividerItemDecoration)
            // Loading data in recyclerview
            binding.recyclerview.setAdapter(AllNewsAdapter(articleList,this,this))
        })
        textToSpeech = TextToSpeech(this, this)
        viewModel.fetchData("a", RetrofitInstance.apiKey)

        // News search
        binding.searchBtn.setOnClickListener {
            AppConstants.hideKeyboard(it)
            if (TextUtils.isEmpty(binding.searchView.text.toString())){
                Toast.makeText(applicationContext,"Please enter title",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding.progressHorizontal.visibility = View.VISIBLE
            binding.recyclerview.visibility = View.GONE
            finalSearch = binding.searchView.text.toString()
            Log.d("zzzzzz","$finalSearch")
            viewModel.fetchData(binding.searchView.text.toString(), RetrofitInstance.apiKey)
        }

        // Date picker
        binding.datePickerActions.setOnClickListener {
            showDatePickerDialog()
        }
    }

    // Date picker function
    private fun showDatePickerDialog() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a datePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // Update the selected date
                selectedYear = year
                selectedMonth = month+1
                selectedDay = day
                val dateString = "${selectedYear}-${selectedMonth}-${selectedDay}"
                var searchQ="a"
                binding.progressHorizontal.visibility = View.VISIBLE
                binding.recyclerview.visibility = View.GONE
                if (!TextUtils.isEmpty(binding.searchView.text.toString())) searchQ = binding.searchView.text.toString()
                viewModel.getNewsDate(searchQ,dateString, RetrofitInstance.apiKey)

                finalSearch = binding.searchView.text.toString()
            },
            currentYear,
            currentMonth,
            currentDay
        )
        // Disallow to select future date
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        // Show the datePickerDialog
        datePickerDialog.show()
    }

    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }

    // Text-to-speech initialisation
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set the language for text-to-speech
            val result = textToSpeech.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                println("Language not supported.")
            } else {

            }
        } else {
            println("TextToSpeech initialization failed.")
        }
    }

    // Text-to-speech function
    fun speakText(text: String) {
        showCustomDialog(text)
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    // Text-to-speech popup window
    private fun showCustomDialog(content: String) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_dialog_layout, null)

        // Customisation dialog view
        val titleTextView: TextView = view.findViewById(R.id.dialogTitle)
        titleTextView.text = content

        val animationView: LottieAnimationView = view.findViewById(R.id.lottieAnimationView)
        animationView.setAnimation(R.raw.speaking)
        animationView.playAnimation()

        val closeButton: Button = view.findViewById(R.id.dialogButton)
        closeButton.setOnClickListener {
            if (textToSpeech.isSpeaking) {
                textToSpeech.stop()
            }
            alertDialog?.dismiss()
        }

        builder.setView(view)
        alertDialog = builder.create()

        // Show the dialog
        alertDialog.show()
    }
}