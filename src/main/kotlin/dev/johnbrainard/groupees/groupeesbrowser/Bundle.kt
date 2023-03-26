package dev.johnbrainard.groupees.groupeesbrowser

import java.time.OffsetDateTime

data class Bundle(
	val title: String,
	val name: String,
	val endsOn: OffsetDateTime,
	val productDetails: ProductDetails
)

data class ProductDetails(val details: String, val products: Set<Product>)

sealed interface Product {
	val title: String

	class Album(override val title: String) : Product
	class RoyaltyFree(override val title: String) : Product
	class Comics(override val title: String) : Product
	class Ebooks(override val title: String) : Product
	class Game(override val title: String) : Product
}