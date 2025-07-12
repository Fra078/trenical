package it.trenical.customer.gui.data.models

data class QueryParams(
    val departure: String? = null,
    val arrival: String? = null,
    val date: Long? = null,
    val trainType: String? = null,
    val serviceClass: String? = null,
    val count: Int = 1
){
    val isValid = departure != null && arrival != null && date != null
}