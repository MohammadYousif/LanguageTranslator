package com.yousif.translators

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.yousif.translators.databinding.FragmentTranslationBinding
import com.yousif.translators.ui.activites.MainActivity
import java.util.Locale


class FragmentTranslation : Fragment() , View.OnClickListener{

    private var _binding: FragmentTranslationBinding? = null

    // with the backing property of the kotlin we extract
    // the non null value of the _binding
    private val binding get() = _binding!!


    var englishHindiTranslator: Translator? = null
    var hindiEnglishTranslator:Translator? = null
    var clipboard: ClipboardManager? = null
    var clip: ClipData? = null
    var mdToast: Toast? = null
    var flag = true
    var dialog: Dialog? = null
    var options_2: TranslatorOptions? = null
    private val fromLang = arrayOf(TranslateLanguage.ENGLISH, TranslateLanguage.URDU)
    private val fromLanguages = arrayOf(
        TranslateLanguage.ENGLISH,
        TranslateLanguage.AFRIKAANS,
        TranslateLanguage.ARABIC,
        TranslateLanguage.BELARUSIAN,
        TranslateLanguage.BULGARIAN,
        TranslateLanguage.BENGALI,
        TranslateLanguage.CATALAN,
        TranslateLanguage.CZECH,
        TranslateLanguage.CHINESE,
        TranslateLanguage.DANISH,
        TranslateLanguage.GERMAN,
        TranslateLanguage.GREEK,
        TranslateLanguage.HINDI,
        TranslateLanguage.ITALIAN,
        TranslateLanguage.JAPANESE,
        TranslateLanguage.KANNADA,
        TranslateLanguage.KOREAN,
        TranslateLanguage.MARATHI,
        TranslateLanguage.PERSIAN,
        TranslateLanguage.PORTUGUESE,
        TranslateLanguage.RUSSIAN,
        TranslateLanguage.ROMANIAN,
        TranslateLanguage.SPANISH,
        TranslateLanguage.TELUGU,
        TranslateLanguage.TAMIL,
        TranslateLanguage.TURKISH,
        TranslateLanguage.THAI,
        TranslateLanguage.URDU,
        TranslateLanguage.UKRAINIAN,
        TranslateLanguage.VIETNAMESE,
        TranslateLanguage.WELSH
    )

    private val toLanguages = arrayOf(
        TranslateLanguage.ENGLISH,
        TranslateLanguage.AFRIKAANS,
        TranslateLanguage.ARABIC,
        TranslateLanguage.BELARUSIAN,
        TranslateLanguage.BULGARIAN,
        TranslateLanguage.BENGALI,
        TranslateLanguage.CATALAN,
        TranslateLanguage.CZECH,
        TranslateLanguage.CHINESE,
        TranslateLanguage.DANISH,
        TranslateLanguage.GERMAN,
        TranslateLanguage.GREEK,
        TranslateLanguage.HINDI,
        TranslateLanguage.ITALIAN,
        TranslateLanguage.JAPANESE,
        TranslateLanguage.KANNADA,
        TranslateLanguage.KOREAN,
        TranslateLanguage.MARATHI,
        TranslateLanguage.PERSIAN,
        TranslateLanguage.PORTUGUESE,
        TranslateLanguage.RUSSIAN,
        TranslateLanguage.ROMANIAN,
        TranslateLanguage.SPANISH,
        TranslateLanguage.TELUGU,
        TranslateLanguage.TAMIL,
        TranslateLanguage.TURKISH,
        TranslateLanguage.THAI,
        TranslateLanguage.URDU,
        TranslateLanguage.UKRAINIAN,
        TranslateLanguage.VIETNAMESE,
        TranslateLanguage.WELSH
    )

    private var translateFROM: String? = null
    private var translateTO: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // inflate the layout and bind to the _binding
        _binding = FragmentTranslationBinding.inflate(inflater, container, false)



        binding.mic.setOnClickListener(this)
        binding.cp1.setOnClickListener(this)
        binding.cp2.setOnClickListener(this)


        binding.fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                translateFROM = fromLanguages[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val fromSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(activity as MainActivity, R.layout.spinner_item, fromLanguages)
        fromSpinnerAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.fromSpinner.adapter = fromSpinnerAdapter

        binding.toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                translateTO = toLanguages[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val toSpinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(activity as MainActivity, R.layout.spinner_item, toLanguages)
        toSpinnerAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.toSpinner.adapter = toSpinnerAdapter
        clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        binding.translateBtn.setOnClickListener(View.OnClickListener {
            open_dialog()
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(translateFROM!!)
                .setTargetLanguage(translateTO!!)
                .build()
            englishHindiTranslator = Translation.getClient(options)
            englishHindiTranslator!!.downloadModelIfNeeded().addOnSuccessListener {
                dialog!!.dismiss()
                translateText(binding.et1.text.toString())
            }
                .addOnFailureListener { e -> binding.txt.text = e.message }
        })



        return binding.root
    }


    override fun onClick(v: View) {
        if (v.id == R.id.mic) {
//            voice();
        } else if (v.id == R.id.cp_1) {
            try {
                copy(binding.et1.text.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (v.id == R.id.cp_2) {
            try {
                copy(binding.txt.text.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    fun voice() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
        try {
            startActivityForResult(intent, 200)
        } catch (a: ActivityNotFoundException) { }
    }

    private fun open_dialog() {
        try {
            dialog = Dialog(requireContext(), android.R.style.Theme_Dialog)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.setContentView(R.layout.dialog_loading)
            dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            dialog!!.setCancelable(false)
            dialog!!.show()
        } catch (e: Exception) {
        }
    }


    private fun copy(text: String) {
        if (text != "") {
            clip = ClipData.newPlainText("text", text)
            clipboard!!.setPrimaryClip(ClipData.newPlainText("text", text))
            Toast.makeText(requireContext(), "Text Copied", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Text Copied", Toast.LENGTH_SHORT).show()
        }
        mdToast!!.show()
    }


    @SuppressLint("StaticFieldLeak")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)!!
                binding.et1.setText(result[0])
                translateText(binding.et1.text.toString().trim())
            }
        }
    }

    private fun translateText(text: String?) {
        englishHindiTranslator!!.translate(text!!)
            .addOnSuccessListener { translatedText -> // Translation successful.
                binding.txt.text = translatedText
            }
            .addOnFailureListener { e -> // Error.
                // ...
                binding.txt.text = e.message
            }
    }


}