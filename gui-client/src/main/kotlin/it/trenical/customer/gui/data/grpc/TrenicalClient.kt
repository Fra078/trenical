package it.trenical.customer.gui.data.grpc

import io.grpc.ManagedChannel
import it.trenical.common.proto.Empty
import it.trenical.server.gateway.proto.TrenicalGatewayGrpcKt
import it.trenical.ticketry.proto.TripQueryParams
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toList

class TrenicalClient(jwt: String) {
    private val client: TrenicalGatewayGrpcKt.TrenicalGatewayCoroutineStub =
        TrenicalGatewayGrpcKt.TrenicalGatewayCoroutineStub(GrpcChannelProvider.get())
            .withCallCredentials(BearerToken(jwt))

    suspend fun getAllStations() : List<String> {
        return client.getAllStations(Empty.getDefaultInstance())
            .map { it.name }
            .toList()
            .sorted()
    }

    suspend fun getAllTrainTypes() : List<String> {
        return client.getAllTrainTypes(Empty.getDefaultInstance())
            .map { it.name }
            .toList()
            .sorted()
    }

    suspend fun getAllServiceClasses() : List<String> {
        return client.getAllServiceClasses(Empty.getDefaultInstance())
            .map { it.name }
            .toList()
            .sorted()
    }

    fun queryTravel(){
        client.queryTravelSolutions(TripQueryParams.newBuilder().build())
    }
}