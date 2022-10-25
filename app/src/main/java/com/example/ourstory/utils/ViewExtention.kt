package com.example.ourstory.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ourstory.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun ImageView.setImage(url: String) =
    Glide.with(this)
        .load(url)
        .into(this)

fun EditText.textTrim() = this.text.toString().trim()

@SuppressLint("SimpleDateFormat", "SetTextI18n")
fun TextView.convertDate(date: String, context: Context) {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val pasTime = dateFormat.parse(date)
    val nowTime = Date()

    val dateDiff = nowTime.time - (pasTime?.time ?: 0)

    val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
    val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
    val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
    val day = TimeUnit.MILLISECONDS.toDays(dateDiff)

    if (second < 60) this.text = "$second ${context.getString(R.string.date_second)}"
    else if (minute < 60) this.text = "$minute ${context.getString(R.string.date_minute)}"
    else if (hour < 24) this.text = "$hour ${context.getString(R.string.date_hour)}"
    else if (day >= 7) {
        if (day > 360) this.text = "${day / 360} ${context.getString(R.string.date_year)}"
        else if (day > 30) this.text = "${day / 30} ${context.getString(R.string.date_month)}"
        else this.text = "${day / 7} ${context.getString(R.string.date_week)}"
    } else this.text = "$day ${context.getString(R.string.date_day)}"
}

fun View.snackBar(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()
}
