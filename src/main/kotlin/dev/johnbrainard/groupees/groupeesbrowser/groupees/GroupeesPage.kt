package dev.johnbrainard.groupees.groupeesbrowser.groupees

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

const val GROUPEES_URL = "https://groupees.com"

class GroupeesPage private constructor(
	private val document: Document,
	private val pageLoader: PageLoader
) {

	fun getBundles(): List<BundleTile> {
		return document.select(".default-bundles .default-bundle")
			.map { BundleTile(it) }
	}

	companion object {
		fun open(pageLoader: PageLoader): GroupeesPage {
			val document = pageLoader.loadPage("")
			return GroupeesPage(document, pageLoader)
		}
	}

	inner class BundleTile internal constructor(private val element: Element) {
		val name: String
			get() = element.selectFirst("a")?.attr("href")!!

		val title: String
			get() = element.selectFirst(".bundle-info h2")
				?.text()!!

		val platform: String?
			get() = element.selectFirst(".platforms-list img")
				?.attr("title")

		val productDetails: String?
			get() = element.selectFirst(".products")?.text()

		val bundle: BundlePage
			get() = BundlePage(pageLoader.loadPage(name))
	}

	inner class BundlePage internal constructor(private val document: Document) {
		val endsOn: String = document.selectFirst(".time")
			?.attr("data-final-time")!!
	}
}
