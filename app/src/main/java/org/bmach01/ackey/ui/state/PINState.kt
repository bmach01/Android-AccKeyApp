package org.bmach01.ackey.ui.state

data class PINState(
    val pin: String = "",
    val pin2: String = "",
    val confirming: Boolean = false,
    val title: String = "Setup PIN",
    val instructions: String = "Lorem ipsum here will be the instruction Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum"
)