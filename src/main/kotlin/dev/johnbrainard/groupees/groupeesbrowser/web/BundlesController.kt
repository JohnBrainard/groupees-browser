package dev.johnbrainard.groupees.groupeesbrowser.web

import dev.johnbrainard.groupees.groupeesbrowser.Bundle
import dev.johnbrainard.groupees.groupeesbrowser.Platform
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

@Controller
@RequestMapping("/bundles")
class BundlesController(private val bundleService: BundleService) {

	private val endsOnTooltipFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
	private val endsOnFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

	@GetMapping
	fun listBundles(): ModelAndView {
		val bundles = bundleService.getBundles()
			.sortedBy { it.endsOn }
			.map(::createBundleView)

		return ModelAndView("bundles-list", "bundles", bundles)
	}

	@GetMapping("/{platform}")
	fun listMusicBundles(@PathVariable("platform") platformId: String): ModelAndView {
		val platform = Platform.fromId(platformId)

		val bundles = bundleService.getBundles()
			.filter { bundle -> bundle.platform == platform }
			.sortedBy { it.endsOn }
			.map(::createBundleView)

		return ModelAndView("bundles-list", "bundles", bundles)
	}

	private fun createBundleView(bundle: Bundle): BundleView {
		val expiresInDays = ChronoUnit.DAYS.between(OffsetDateTime.now(), bundle.endsOn)

		return BundleView(
			title = bundle.title,
			details = bundle.details ?: "",
			platform = bundle.platform.label,
			url = "https://groupees.com/${bundle.name}",
			endsOn = bundle.endsOn.format(endsOnFormat),
			endsOnTooltip = bundle.endsOn.toZonedDateTime().format(endsOnTooltipFormat),
			expiresIn = expiresInDays
		)
	}
}

data class BundleView(
	val title: String,
	val details: String,
	val platform: String,
	val url: String,
	val endsOn: String,
	val endsOnTooltip: String,
	val expiresIn: Long
)
