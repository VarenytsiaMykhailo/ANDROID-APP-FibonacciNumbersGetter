package com.example.kibfirstapplication

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
/*
data (число) - это порядковый номер ряда чисел фибоначчи, а не 12.12.2020
* */
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var isCorrectData = false //флажок
        var data: Byte? = null //здесь хранится введенное корректное число. Если числа нет, хранится null
        /*Валидация editText поля (если введены только числа, то подсвечивает зеленым, другой текст - красным. Минусы, точки, запятые - красным)*/
        editText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                try {
                    if (TextUtils.isDigitsOnly(editText.text) && !editText.text.isEmpty()) {
                        if (editText.text.toString().toInt() <= 92) { //если пользователь ввел число <=92
                            editText.setTextColor(resources.getColor(R.color.colorCorrectDataGreen))
                            editText.background.mutate().setColorFilter( //цвет линии editText
                                resources.getColor(R.color.colorCorrectDataGreen),
                                PorterDuff.Mode.SRC_ATOP
                            )
                            isCorrectData = true
                            data = editText.text.toString().toByte()
                        } else { //если пользователь ввел число > 92
                            editText.setTextColor(resources.getColor(R.color.colorError))
                            editText.background.mutate().setColorFilter( //цвет линии editText
                                resources.getColor(R.color.colorError),
                                PorterDuff.Mode.SRC_ATOP
                            )
                            isCorrectData = false
                            data = null
                        }
                    } else { //если в пользователь ввел какие-нибудь символы кроме цифр
                        editText.setTextColor(resources.getColor(R.color.colorError))
                        editText.background.mutate().setColorFilter( //цвет линии editText
                            resources.getColor(R.color.colorError),
                            PorterDuff.Mode.SRC_ATOP
                        )
                        isCorrectData = false
                        data = null
                    }
                } catch(e: NumberFormatException) { //если произошло переполнение типа Byte (пользователь ввел число более, чем 127)
                    editText.setTextColor(resources.getColor(R.color.colorError))
                    editText.background.mutate().setColorFilter( //цвет линии editText
                        resources.getColor(R.color.colorError),
                        PorterDuff.Mode.SRC_ATOP
                    )
                    isCorrectData = false
                    data = null
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        /*если в editText введена некорректная дата, то при нажатии на кнопку будет вылезать всплывающая подсказка*/
        button.setOnClickListener{
            if (isCorrectData) {
                val intent = Intent(this, AnswerActivity::class.java)
                intent.putExtra("data", data) //передаем data (число) в AnswerActivity
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, R.string.incorrect_data, Toast.LENGTH_SHORT).show() //всплывающая подсказка
            }
        }
    }
}
