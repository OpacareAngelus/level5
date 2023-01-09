package activity.mainActivity.remote

import io.reactivex.Single
import activity.mainActivity.remote.requests.AddContactRequest
import activity.mainActivity.remote.requests.EditProfileRequest
import activity.mainActivity.remote.requests.LoginRequest
import activity.mainActivity.remote.requests.RegisterUserRequest
import activity.mainActivity.remote.responses.AuthorizeResponse
import activity.mainActivity.remote.responses.ContactsResponse
import activity.mainActivity.remote.responses.ProfileResponse
import activity.mainActivity.remote.responses.UsersResponse
import retrofit2.http.*


interface RequestAPI {

    @POST("./users")
    fun registerUser(@Body request: RegisterUserRequest): Single<AuthorizeResponse>

    @POST("./login")
    fun login(@Body request: LoginRequest): Single<AuthorizeResponse>

    @GET("users/{userId}")
    fun getProfile(
        @Path("userId") userId: String,
        @Header("Authorization") accessToken: String?
    ): Single<ProfileResponse>

    @PUT("users/{userId}")
    fun editProfile(
        @Path("userId") userId: String,
        @Header("Authorization") accessToken: String?,
        @Body request: EditProfileRequest
    ): Single<ProfileResponse>

    @GET("./users")
    fun getUsers(
        @Header("Authorization") accessToken: String?,
    ): Single<UsersResponse>

    @PUT("users/{userId}/contacts")
    fun addContact(
        @Path("userId") userId: String,
        @Header("Authorization") accessToken: String,
        @Body request: AddContactRequest
    ): Single<ContactsResponse>

    @DELETE("users/{userId}/contacts/{deletingUser}")
    fun deleteContact(
        @Path("userId") userId: String,
        @Path("deletingUser") deletingUser: String,
        @Header("Authorization") accessToken: String?
    ): Single<ContactsResponse>

    @GET("users/{userId}/contacts")
    fun getContacts(
        @Path("userId") userId: String,
        @Header("Authorization") accessToken: String?
    ): Single<ContactsResponse>
}