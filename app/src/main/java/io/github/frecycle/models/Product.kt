package io.github.frecycle.models

class Product {
    lateinit var product_id : String
    lateinit var category : String
    lateinit var product_name : String
    lateinit var description : String
    lateinit var city : String
    lateinit var date : String
    lateinit var time: String
    lateinit var owner: String




    constructor(){
    }

    constructor(
        product_id: String,
        category: String,
        product_name: String,
        description: String,
        city: String,
        date: String,
        time: String,
        owner: String
    ) {
        this.product_id = product_id
        this.category = category
        this.product_name = product_name
        this.description = description
        this.city = city
        this.date = date
        this.time = time
        this.owner = owner
    }


}