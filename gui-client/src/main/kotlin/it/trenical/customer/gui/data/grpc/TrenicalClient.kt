package it.trenical.customer.gui.data.grpc

import io.grpc.ManagedChannel
import it.trenical.common.proto.Empty
import it.trenical.customer.gui.data.mappers.toTripQueryParameters
import it.trenical.customer.gui.data.models.QueryParams
import it.trenical.server.gateway.proto.TrenicalGatewayGrpcKt
import it.trenical.ticketry.proto.PurchaseTicketRequest
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

    fun queryTravel(params: QueryParams) =
        client.queryTravelSolutions(toTripQueryParameters(params))

    suspend fun getLoyaltySubscription() =
        client.getLoyaltySubscriptionInfo(Empty.getDefaultInstance())

    suspend fun subscribeLoyalty() {
        client.subscribeToLoyalty(Empty.getDefaultInstance())
    }

    suspend fun unsubscribeLoyalty() {
        client.unsubscribeToLoyalty(Empty.getDefaultInstance())
    }

    fun listenToPromotions() =
        client.listenToLoyaltyPromotions(Empty.getDefaultInstance())

    suspend fun makePurchase(request: PurchaseTicketRequest) : List<Int>{
        return client.buyTicket(request).ticketIdList
    }

}