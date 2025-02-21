package org.example.project


import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun subscribeToService(apiUrl: String, start: Position, end: Position, intervalMillis: Long = 120_000L): Flow<RouteResponse> = flow {

    val firstRequest = RouteRequest(
        start,
        end
    )
    try{
        val firstResponse: RouteResponse = ApiService.postData(
            url = apiUrl,
            requestBody = firstRequest
        )
        emit(firstResponse)
        println("First response: $firstResponse")
    }catch(e: Exception){
        e.printStackTrace()
    }
    // Delay for the specified interval (default 2 minutes)
    delay(intervalMillis)
    emit(RouteResponse(message = "Weather alert! Rain is expected in the next 40 miles.", isAlert = true))
    delay(intervalMillis)
    emit(RouteResponse())
    emit(RouteResponse(message = "Caution! there is a car accident in 2 miles", isAlert = true))
    delay(intervalMillis)

    // Make a second call (or keep calling in a loop if you want indefinite subscription)
    val secondRequest = RouteRequest(
        start,
        Position(40.6961, -73.9845)
    )
    val secondResponse: RouteResponse = ApiService.postData(
        url = apiUrl,
        requestBody = secondRequest
    )
    secondResponse.isOriginalRoute = false
    emit(secondResponse)
    println("Second response: $secondResponse")
}

