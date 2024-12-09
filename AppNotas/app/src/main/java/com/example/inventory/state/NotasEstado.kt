package com.example.inventory.state

data class NotasEstado(val nombre: String="",
                       val fecha: String="",
                       val descripcion: String="",
                       val tipo: String="",
                       val mostrarAlerta: Boolean=false,


                       val id:Long=0)
