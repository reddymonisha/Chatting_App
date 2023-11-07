package com.example.chattingapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context,val messageList:ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE=1;
    val ITEM_SENT=2;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType==1){
            // inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receiver,parent,false)
            return ReceiverViewHolder(view)
        }
        else{
            // inflate send
            val view: View = LayoutInflater.from(context).inflate(R.layout.send,parent,false)
            return SendViewHolder(view)
        }

    }

    override fun getItemCount(): Int {

        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderid)){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVE
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if(holder.javaClass==SendViewHolder::class.java){
            // do the stuff for send view holder


            val viewHolder = holder as SendViewHolder
            holder.sentmessage.text = currentMessage.message
        }
        else{
            // do stuff fr receive view holder
            val viewHolder = holder as ReceiverViewHolder
            holder.receivemessage.text = currentMessage.message

        }

    }

    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentmessage = itemView.findViewById<TextView>(R.id.txt_send_message)

    }
    class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val receivemessage = itemView.findViewById<TextView>(R.id.txt_receiver_message)

    }

}