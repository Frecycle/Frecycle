package io.github.frecycle.models

class MessageListModel {
    lateinit var product_name : String
    lateinit var user_name: String
    lateinit var userId: String
    lateinit var productId: String
    lateinit var profile_photo : String
    lateinit var product_photo : String
    var received : Boolean = true

    constructor()
    constructor(
        product_name: String,
        user_name: String,
        userId: String,
        productId: String,
        profile_photo: String,
        product_photo: String,
        received: Boolean
    ) {
        this.product_name = product_name
        this.user_name = user_name
        this.userId = userId
        this.productId = productId
        this.profile_photo = profile_photo
        this.product_photo = product_photo
        this.received = received
    }

    override fun toString(): String {
        return "MessageListModel(product_name='$product_name', user_name='$user_name', userId='$userId', productId='$productId', profile_photo='$profile_photo', product_photo='$product_photo', received=$received)"
    }


}