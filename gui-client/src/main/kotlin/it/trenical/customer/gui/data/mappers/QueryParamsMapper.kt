package it.trenical.customer.gui.data.mappers

import it.trenical.common.proto.dateRange
import it.trenical.customer.gui.data.models.QueryParams
import it.trenical.ticketry.proto.TripQueryParams
import it.trenical.ticketry.proto.tripQueryParams
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


fun toTripQueryParameters(params: QueryParams): TripQueryParams = tripQueryParams {
    this.date = dateRange {
        val date = Instant.ofEpochMilli(params.date!!).atZone(ZoneId.systemDefault())
            .toLocalDate().atStartOfDay(ZoneId.systemDefault())
        this.from = date.toInstant().epochSecond
        this.to = date.plusDays(1).minusSeconds(1).toInstant().epochSecond
    }
    params.serviceClass?.let { serviceClass = it }
    params.trainType?.let { trainType = it }
    this.departure = params.departure!!
    this.arrival = params.arrival!!
    this.ticketCount = params.count
}