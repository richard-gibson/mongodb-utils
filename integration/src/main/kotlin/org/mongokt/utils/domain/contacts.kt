package org.mongokt.utils.domain

import org.mongokt.utils.annotations.query

@query
data class ContactInfo(
  val name: Name,
  val emails: List<Email>,
  val address: Address,
) {
  companion object
}

@query
data class Name(
  val first: String,
  val last: String,
  val middle: String?,
) {
  companion object
}

@query
data class Email(
  val address: String,
  val verified: Boolean,
) {
  companion object
}

@query
data class Address(
  val street: Street,
  val city: String,
) {
  companion object
}

@query
data class Street(
  val name: String,
  val number: Int?,
) {
  companion object
}
