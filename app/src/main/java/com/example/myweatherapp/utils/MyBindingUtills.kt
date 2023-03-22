package com.example.myweatherapp.utils


import android.widget.ImageView
import androidx.databinding.BindingAdapter

import com.squareup.picasso.Picasso


object MyBindingUtills {

}

@BindingAdapter("myImageUrl")
fun loadImage(view:ImageView, url:String){
    var urls="https://openweathermap.org/img/wn/"+url+"@2x.png"
    Picasso.get().load(urls).into(view);
}