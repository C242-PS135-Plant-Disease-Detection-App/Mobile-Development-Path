import PlantResponse
import retrofit2.Response
import retrofit2.http.GET

interface PlantApi {
    @GET("scan")
    suspend fun scanPlant(): Response<PlantResponse>
}
