package com.example.employeecahtkt.models

import com.google.firebase.Timestamp

class MessageModel {

    var messageId:String? = null
    var message:String? = null
    var senderId:String? = null
    var imageUrl:String? = null
    var timeStamp:Long? = 0

    constructor()

    constructor(messageId: String?,senderId:String?,timestamp:Long){
        this.messageId = messageId
        this.senderId = senderId
        this.timeStamp = timeStamp
    }

}