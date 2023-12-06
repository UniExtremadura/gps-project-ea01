package es.unex.giiis.asee.snapmap_ea01.view.home

import es.unex.giiis.asee.snapmap_ea01.data.model.User

interface UserProvider {
    fun getUser(): User
}