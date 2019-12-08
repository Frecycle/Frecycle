package io.github.frecycle.models

class User {
    lateinit var user_id : String
    lateinit var name: String
    lateinit var email: String
    var phone: Long = 0
    var rank : Float = 0.0f
    var voter : Int = 0
    lateinit var profile_photo : String
    lateinit var city : String

    constructor(){}
    constructor(
        user_id: String,
        name: String,
        email: String,
        phone: Long,
        rank: Float,
        voter: Int,
        profile_photo: String,
        city: String
    ) {
        this.user_id = user_id
        this.name = name
        this.email = email
        this.phone = phone
        this.rank = rank
        this.voter = voter
        this.profile_photo = profile_photo
        this.city = city
    }

    override fun toString(): String {
        return "User(user_id='$user_id', name='$name', email='$email', phone=$phone, rank=$rank, voter=$voter, profile_photo='$profile_photo', city='$city')"
    }


}