package data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    var id: Int,
    var name: String,
    var email: String,
    var phone: String,
    var career: String,
    var address: String,
    var birthday: String,
    var facebook: String,
    var instagram: String,
    var twitter: String,
    var linkedin: String,
    var image: String,
    var created_at: String,
    var updated_at: String
) : Parcelable
