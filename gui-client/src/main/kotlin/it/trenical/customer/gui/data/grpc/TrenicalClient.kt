package it.trenical.customer.gui.data.grpc

import io.grpc.ManagedChannel
import it.trenical.server.gateway.proto.TrenicalGatewayGrpcKt

class TrenicalClient(channel: ManagedChannel, jwt: String) {
    private val client: TrenicalGatewayGrpcKt.TrenicalGatewayCoroutineStub =
        TrenicalGatewayGrpcKt.TrenicalGatewayCoroutineStub(channel)
            .withCallCredentials(BearerToken(jwt))


}