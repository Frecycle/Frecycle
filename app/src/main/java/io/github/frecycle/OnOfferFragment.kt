package io.github.frecycle


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.frecycle.util.OnItemClickListener
import io.github.frecycle.util.RecyclerViewOnOfferAdapter

class OnOfferFragment : Fragment() {
    private lateinit var productMap: LinkedHashMap<String, String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_on_offer, container, false)
        return view
    }

    fun initRecyclerView(view: View, productMap: LinkedHashMap<String,String>){
        this.productMap = productMap
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerViewOnOffer)
        val recyclerViewImagesAdapter = RecyclerViewOnOfferAdapter(view.context, ArrayList(productMap.values))

        recyclerView.layoutManager = GridLayoutManager(view.context,3)
        recyclerView.adapter = recyclerViewImagesAdapter

        recyclerViewImagesAdapter.setOnItemClickListener(object: OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(view.context.applicationContext, ProductActivity::class.java)
                intent.putExtra("productId", productMap.keys.toTypedArray()[position])
                startActivity(intent)
            }
        })
    }
}
