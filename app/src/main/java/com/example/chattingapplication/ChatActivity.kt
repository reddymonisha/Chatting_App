package com.example.chattingapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.hardware.Sensor
import android.hardware.SensorManager
import android.content.Context
import android.graphics.Color
import kotlin.random.Random


class ChatActivity : AppCompatActivity() {
    private lateinit var ChatRecyclerView: RecyclerView
    private lateinit var messagebox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    var receiverRoom: String? = null
    var senderRoom: String? = null

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometerSensor: Sensor
    private val shakeThreshold = 800 // Adjust the threshold as needed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        ChatRecyclerView = findViewById<RecyclerView>(R.id.chatRecyclerview)
        messagebox = findViewById<EditText>(R.id.messagebox)
        sendButton = findViewById(R.id.sendbutton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        ChatRecyclerView.layoutManager = LinearLayoutManager(this)
        ChatRecyclerView.adapter = messageAdapter

        // Initialize the sensor manager and accelerometer sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        // Set up the ShakeDetector
        val shakeDetector = ShakeDetector(shakeThreshold) {
            changeBackgroundColor()
        }

        sensorManager.registerListener(shakeDetector, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)

        // Logic for adding data to recyclerview
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })

        // Adding the message to the database
        sendButton.setOnClickListener {
            val message = messagebox.text.toString()
            val messageObject = Message(message, senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messagebox.setText("")
        }
    }

    private fun changeBackgroundColor() {
        // Implement your background color change logic here
        // For example, you can change the background color of ChatRecyclerView.

        val colors = intArrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,Color.MAGENTA)
        val randomColor = colors[Random.nextInt(colors.size)]
        ChatRecyclerView.setBackgroundColor(randomColor)
    }
}
