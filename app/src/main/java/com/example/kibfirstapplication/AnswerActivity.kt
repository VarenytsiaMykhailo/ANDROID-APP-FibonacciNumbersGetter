package com.example.kibfirstapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_answer.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class AnswerActivity : AppCompatActivity() {

    private var request: Disposable? =  null//сюда будем складывать результат работы нового потока

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)
        button.setOnClickListener{
            finish()
        }
        val data: Byte = intent.getByteExtra("data", -1) //Вытаскиваем из интента дату (число). если data не передана, положить data дефолтное значение -1, на которое в ответ запишем ошибку
        textView1ShowMessage(data) //выводим сообщение на textView1
        /*реактивное программирование с помощью пакета reactivex*/
        val rxObject = Observable.create<Long> {
            fun getFibNumber(n: Byte): Long{
                if (n.compareTo(0) == 0) //для n = 0 выводим 0
                    return 0
                var a: Long = 1
                var b: Long = 1
                for (i in 3..n) {
                    a = a + b
                    val tmp = a
                    a = b
                    b = tmp
                }
                return b
            }
            val ans: Long = getFibNumber(data)
            it.onNext(ans)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) //вычисления в потоке io, а результат попадает в потом UI
        //запускаем другой поток. Аргументы: в 1ой лямбда функции получаем результат работы другого потока и в этой лямбда-функции можно обработать результат. 2ая лямбда функция вызовится, если в другом потоке произойдет какая-либо ошибка\ексепшен\краш
        request = rxObject.subscribe({
            textView2.text = "${it}" //выводим сообщение на textView2
        }, {
            textView2.text = "Error. Maybe you entered incorrect data. Try again." //выводим сообщение на textView2
        })
    }

    override fun onDestroy() {
        request?.dispose() //очищаем результат работы потока (удалятся ссылки на текущее активити и его контекст, решится проблема утечки памяти)
        super.onDestroy()
    }

    private fun textView1ShowMessage(data: Byte){
        if (data.compareTo(-1) != 0) { //пришло корректное значение
            textView1.text = "The ${data} Fibonacci`s number is:"
        } else {
            textView1.text = "Error. Maybe you entered incorrect data. Try again." //не корректное
        }
    }
}
