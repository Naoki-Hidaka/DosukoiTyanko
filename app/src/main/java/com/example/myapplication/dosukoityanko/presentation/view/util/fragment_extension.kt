package com.example.myapplication.dosukoityanko.presentation.view.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions

fun Fragment.transitionPage(
    navDirections: NavDirections,
    navOptions: NavOptions = animationFactory()
) {
    findNavController().navigate(navDirections, navOptions)
}

fun animationFactory() = navOptions {
    anim {
         NavigateAnimationType.SLIDE.let {
            enter = it.enterAnimationId
            exit = it.exitAnimationId
            popEnter = it.popEnterAnimationId
            popExit = it.popExitAnimationId
        }
    }
}