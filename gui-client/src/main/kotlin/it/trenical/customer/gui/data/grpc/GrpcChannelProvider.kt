package it.trenical.customer.gui.data.grpc

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

object GrpcChannelProvider {

    private val channel = ManagedChannelBuilder.forAddress("localhost",6060).usePlaintext().build()

    fun get(): ManagedChannel = channel
}