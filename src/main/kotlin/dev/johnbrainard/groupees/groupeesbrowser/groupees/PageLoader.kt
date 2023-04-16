package dev.johnbrainard.groupees.groupeesbrowser.groupees

import dev.johnbrainard.groupees.groupeesbrowser.web.getLogger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.net.URL

interface PageLoader {
	fun loadPage(page: String): Document
}

@Component
@ConditionalOnProperty(prefix = "groupees", name = ["offline"], matchIfMissing = false)
class OfflinePageLoader : PageLoader {
	private val logger = getLogger()

	override fun loadPage(page: String): Document {
		val url = URL(GROUPEES_URL + page)
		val resourceName = when (page) {
			"", "/" -> "/groupees.html"
			else -> "/bundle.html"
		}

		logger.info("loading page $url, using resource: $resourceName")

		return ClassPathResource(resourceName).inputStream.use {
			Jsoup.parse(it, "UTF-8", url.toString())
		}
	}
}

@Component
@ConditionalOnProperty(prefix = "groupees", name = ["offline"], havingValue = "false", matchIfMissing = true)
class GroupeesPageLoader : PageLoader {
	private val logger = getLogger()

	override fun loadPage(page: String): Document {
		val url = URL(GROUPEES_URL + page)

		// Sleep for a second to avoid hammering Groupees.com
		Thread.sleep(1000)

		logger.info("loading page $url")
		return Jsoup.parse(url, 10_000)
	}
}
