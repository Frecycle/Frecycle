package io.github.frecycle.models

class Chat {
    lateinit var sender: String
    lateinit var receiver: String
    lateinit var message: String
    lateinit var productId: String

    constructor()
    constructor(sender: String, receiver: String, message: String, productId: String) {
        this.sender = sender
        this.receiver = receiver
        this.message = message
        this.productId = productId
    }


}