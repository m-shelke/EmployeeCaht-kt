package com.example.employeecahtkt.models

//Model class
class Users {

//  uid Variable for Getting Firebase User id of User
    var uid : String? = null
//  name Variable for Getting User Name
    var name : String? = null
//    phoneNumber Variable for Getting User Phone Number
    var phoneNumber : String? = null
//    profileImage Variable for Getting User's Profile image
    var profileImage : String? = null

//   Empty primary constructor
    constructor(){}

//    Secondary constructor
    constructor(uid:String?, name:String?, phoneNumber:String?,profileImage:String?){
        this.uid = uid
        this.name = name
        this.phoneNumber = phoneNumber
        this.profileImage = profileImage
    }
}