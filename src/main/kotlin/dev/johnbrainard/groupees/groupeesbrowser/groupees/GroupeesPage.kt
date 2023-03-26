package dev.johnbrainard.groupees.groupeesbrowser.groupees

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URL

const val GROUPEES_URL = "https://groupees.com"

class GroupeesPage private constructor(private val document: Document) {

	fun getBundleElements(): List<BundleElement> {
		return document.select(".default-bundles .default-bundle")
			.map { BundleElement(it) }
	}

	companion object {
		fun open(): GroupeesPage {
			val document = Jsoup.parse(URL(GROUPEES_URL), 10_000)
			return GroupeesPage(document)
		}
	}

	class BundleElement internal constructor(private val element: Element) {
		val name: String
			get() = element.selectFirst("a")?.attr("href")!!

		val title: String
			get() = element.selectFirst(".bundle-info h2")
				?.text()!!

		val endsOn: String
			get() = element.selectFirst(".time")
				?.attr("data-final-time")!!

		val product: ProductElement
			get() = element.selectFirst(".products")
				?.let { ProductElement(it) }!!

	}

	class ProductElement internal constructor(private val element: Element) {
		val details: String
			get() = element.wholeText()

		val album: String?
			get() = element.selectFirst(".g-icon-album")
				?.attr("title")

		val game: String?
			get() = element.selectFirst(".g-icon-game")
				?.attr("title")

		val comics: String?
			get() = element.selectFirst(".g-icon-comics")
				?.attr("title")

		val ebooks: String?
			get() = element.selectFirst(".g-icon-ebooks")
				?.attr("title")

		val royaltyFree: String?
			get() = element.selectFirst(".icon-royalty-free")
				?.attr("title")
	}
}
