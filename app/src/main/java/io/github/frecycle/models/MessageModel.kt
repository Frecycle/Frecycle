package io.github.frecycle.models

class MessageModel {
    lateinit var product_name : String
    lateinit var user_name: String
    lateinit var profile_photo : String
    lateinit var product_photo : String

    constructor()

    constructor(product_name: String, name: String, profile_photo: String, product_photo: String) {
        this.product_name = product_name
        this.user_name = name
        this.profile_photo = profile_photo
        this.product_photo = product_photo
    }

    override fun toString(): String {
        return "MessageModel(product_name='$product_name', name='$user_name', profile_photo='$profile_photo', product_photo='$product_photo')"
    }


}