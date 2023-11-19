package es.unex.giiis.asee.snapmap_ea01.dummy

import androidx.room.PrimaryKey
import es.unex.giiis.asee.snapmap_ea01.data.model.Comment
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.data.model.UserPhotoLikeRef
import es.unex.giiis.asee.snapmap_ea01.data.model.UserUserFollowRef

val dummyUser: List<User> = listOf(
    User(1, "Daniel", "Hola me llamo Dani", "daniAbad@alumnos.unex.es","12345"),
    User(2, "Fernando", "Soy Fernando y me encanta SnapMap", "Fer@alumnos.unex.es","qwerty"),
    User(3, "Gabri", "Soy nuevo en esta app", "Gabri@alumnos.unex.es","Gabri!?1"),
    User(4, "Sergio", "Buenas a todos, me encantan las fotos", "Sergio@alumnos.unex.es","sergio1234"),
    User(5, "Lucas", "Busco amigos para hacer buenas fotos", "@Lucas@gmail,com","lukitas1!")
)

val dummyPhoto: List<Photo> = listOf(
    Photo(1,"https://images.dog.ceo/breeds/dhole/n02115913_3229.jpg", 1,	39.467082 ,-6.383266),
    Photo(2,"https://images.dog.ceo/breeds/pomeranian/n02112018_6208.jpg", 2,	39.480042 , -6.372755),
    Photo(3,"https://images.dog.ceo/breeds/buhund-norwegian/hakon2.jpg", 3,	39.460681, -6.378834),
    Photo(4,"https://images.dog.ceo/breeds/terrier-lakeland/n02095570_457.jpg", 4,	39.465467, -6.367453),
    Photo(5,"https://images.dog.ceo/breeds/basenji/n02110806_4122.jpg", 5,	39.479299, -6.342499)
)

val dummyComment: List<Comment> = listOf(
    Comment(1, 4, 1, "Sales genial!!"),
    Comment(2, 2, 4, "Me encanta ese paisaje."),
    Comment(3, 3, 2, "Impresionante"),
    Comment(4, 4, 3, "Maravillosa foto :)"),
    Comment(5, 5, 1, "Impresionante")
)

val dummyUserPhotoLike: List<UserPhotoLikeRef> = listOf(
    UserPhotoLikeRef(1,2),
    UserPhotoLikeRef(2,4),
    UserPhotoLikeRef(3,5),
    UserPhotoLikeRef(4,1),
    UserPhotoLikeRef(5,3),
    UserPhotoLikeRef(1,3),
    UserPhotoLikeRef(5,1)
)

val dummyUserUserFollow: List<UserUserFollowRef> = listOf(
    UserUserFollowRef(1,2),
    UserUserFollowRef(1,3),
    UserUserFollowRef(2,1),
    UserUserFollowRef(2,4),
    UserUserFollowRef(2,5),
    UserUserFollowRef(3,1),
    UserUserFollowRef(3,4),
    UserUserFollowRef(4, 2),
    UserUserFollowRef(4,3),
    UserUserFollowRef(4,5),
    UserUserFollowRef(5,4),
    UserUserFollowRef(5,2)
)



