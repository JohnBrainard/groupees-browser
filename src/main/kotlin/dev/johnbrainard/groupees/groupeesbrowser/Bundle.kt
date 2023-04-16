package dev.johnbrainard.groupees.groupeesbrowser

import java.time.OffsetDateTime

data class Bundle(
	val title: String,
	val name: String,
	val endsOn: OffsetDateTime,
	val platform: Platform,
	val details: String?
)

enum class Platform(val label: String, val id: String) {
	STEAM("Steam", "steam"),
	ITCH_IO("Itch.io", "itchio"),
	BANDCAMP("Bandcamp", "bandcamp"),
	PDF("PDF", "pdf"),
	OTHER("Other", "other");

	companion object {
		fun fromId(id: String) = values().first { it.id == id }
	}
}
