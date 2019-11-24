package io.github.frecycle


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import io.github.frecycle.util.UniversalImageLoader


/**
 * A simple [Fragment] subclass.
 */
class EditProfileFragment : Fragment() {
    private lateinit var profilePhoto : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profilePhoto = view.findViewById(R.id.profilePhoto)!!

        setProfileImage()
    }

    private fun setProfileImage(){
        val photoURL = "galeri14.uludagsozluk.com/761/lagertha-lothbrok_1042575_m.png"
        UniversalImageLoader.setImage(photoURL, profilePhoto, null, "https://")
    }



}
