package data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int,
    val email: String,
    val password: String,
    val name: String?,
    val phone: String?,
    val address: String?,
    val career: String?,
    val birthday: String?,
    val facebook: String?,
    val instagram: String?,
    val twitter: String?,
    val linkedin: String?,
    val image: String?,
    var isSelected: Boolean
) : Parcelable{

        override fun hashCode(): Int {
            var result = id.hashCode()
            if(image.isNullOrEmpty()){
                result = 31 * result + image.hashCode()
            }
            return result
        }
    }


