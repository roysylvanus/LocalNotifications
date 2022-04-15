package com.malikali.schedulenotifications

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import com.malikali.schedulenotifications.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        binding.btnSchedule.setOnClickListener {
            scheduleANotification()
        }
    }

    private fun scheduleANotification() {
        val intent = Intent(applicationContext,NotificationReceiver::class.java)
        val title = binding.edTitle.text.toString()
        val message = binding.edMessage.text.toString()
        intent.putExtra(titleExtra,title)
        intent.putExtra(messageExtra,message)


        val pendingIntent =PendingIntent.getBroadcast(applicationContext, notificationID,intent
            ,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val alarManager =  getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        alarManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
        time,
        pendingIntent)

        showAlert(time,title,message)

    }

    private fun showAlert(time: Long, title: String, message: String) {

        val date  = Date(time)
        val dateFormat = DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date)
            ).setPositiveButton("Okay"){_,_ ->}
            .show()

    }

    private fun getTime(): Long {

        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour

        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year,month,day, hour, minute)

        return calendar.timeInMillis

    }

    private fun createNotificationChannel() {
        val name = "Notification channel"
        val desc = "A description of the channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID,name,importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}