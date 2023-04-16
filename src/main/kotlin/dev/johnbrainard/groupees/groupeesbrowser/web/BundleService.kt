package dev.johnbrainard.groupees.groupeesbrowser.web

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import dev.johnbrainard.groupees.groupeesbrowser.Bundle
import dev.johnbrainard.groupees.groupeesbrowser.Platform
import dev.johnbrainard.groupees.groupeesbrowser.groupees.GroupeesPage
import dev.johnbrainard.groupees.groupeesbrowser.groupees.PageLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

inline fun <reified T : Any> T.getLogger(): Logger = LoggerFactory.getLogger(T::class.java)

private const val BUNDLE_CACHE_KEY: String = "bundles"

@Service
class BundleService(private val pageLoader: PageLoader) {
	private val logger = getLogger()

	private val bundleCache: LoadingCache<String, List<Bundle>>

	init {
		bundleCache = CacheBuilder.newBuilder()
			.maximumSize(1)
			.expireAfterWrite(12, TimeUnit.HOURS)
			.build(object : CacheLoader<String, List<Bundle>>() {
				override fun load(key: String): List<Bundle> {
					return fetchBundles()
				}
			})
	}

	fun getBundles(): List<Bundle> {
		return bundleCache.get(BUNDLE_CACHE_KEY)
	}

	fun fetchBundles(): List<Bundle> {
		logger.info("fetching bundles from groupees.com...")
		val bundlePage = GroupeesPage.open(pageLoader)

		val bundlesElements = bundlePage.getBundles()
		return bundlesElements.map { bundleElement ->
			val endsOn = bundleElement.bundle.endsOn.let {
				Instant.ofEpochSecond(it.toLong())
					.atZone(ZoneOffset.systemDefault())
					.toOffsetDateTime()
			} ?: OffsetDateTime.MIN

			Bundle(
				bundleElement.title,
				name = bundleElement.name,
				endsOn = endsOn,
				platform = bundleElement.platform?.let { resolvePlatform(it) } ?: Platform.OTHER,
				details = bundleElement.productDetails
			)
		}.also {
			logger.info("found ${it.size} bundles")
		}
	}

	private fun resolvePlatform(platform: String): Platform = when {
		platform.contains("steam", ignoreCase = true) -> Platform.STEAM
		platform.contains("itch.io", ignoreCase = true) -> Platform.ITCH_IO
		platform.contains("pdf", ignoreCase = true) -> Platform.PDF
		platform.contains("bandcamp", ignoreCase = true) -> Platform.BANDCAMP
		else -> Platform.OTHER
	}
}
