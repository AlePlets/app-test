package com.app.lillup.intro.view_model

import androidx.lifecycle.ViewModel
import com.app.lillup.R

class IntroViewModel:ViewModel() {
    val imageList = listOf(
        R.drawable.intro_one,
        R.drawable.intro_two,
        R.drawable.intro_three,
    )
    val footList = listOf(
        "Navigate with Charity\n Learn More,\nSearch Less with All",
        "Personalized Learning, Tailored for You",
        "Tokenise your Learning, Unlock\n Tailored Opportunities",

    )
    val headers = listOf(
        "",
        "StreamliningYour Learning\nJourney",
        "More than personalized \nlearning path",

    )
}