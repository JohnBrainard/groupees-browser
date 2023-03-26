package dev.johnbrainard.groupees.groupeesbrowser.web

import dev.johnbrainard.groupees.groupeesbrowser.Bundle
import dev.johnbrainard.groupees.groupeesbrowser.Product
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Controller
@RequestMapping("/bundles")
class BundlesController(private val bundleService: BundleService) {

	@GetMapping
	fun listBundles(): ModelAndView {
		val bundles = bundleService.getBundles()
			.sortedBy { it.endsOn }
			.map(::createBundleView)

		return ModelAndView("bundles-list", "bundles", bundles)
	}

	@GetMapping("/{productType}")
	fun listMusicBundles(@PathVariable("productType") productTypeName: String): ModelAndView {

		val productType = when (productTypeName) {
			"music" -> Product.Album::class
			"comics" -> Product.Comics::class
			"ebooks" -> Product.Ebooks::class
			"games" -> Product.Game::class
			else -> throw IllegalArgumentException("$productTypeName isn't valid")
		}

		val bundles = bundleService.getBundles()
			.filter { bundle -> bundle.productDetails.products.any(productType::isInstance) }
			.sortedBy { it.endsOn }
			.map(::createBundleView)

		return ModelAndView("bundles-list", "bundles", bundles)
	}

	private fun createBundleView(bundle: Bundle): BundleView {
		val expiresInDays = ChronoUnit.DAYS.between(OffsetDateTime.now(), bundle.endsOn)

		val productView = bundle.productDetails.products.map { product ->
			when (product) {
				is Product.Album -> ProductView("&#127911;", product.title)
				is Product.Comics -> ProductView("&#128172;", product.title)
				is Product.Ebooks -> ProductView("&#128366;", product.title)
				is Product.Game -> ProductView("&#127918;", product.title)
				is Product.RoyaltyFree -> ProductView("&#128275;", "Royalty Free")
			}
		}

		return BundleView(
			title = bundle.title,
			details = bundle.productDetails.details,
			products = productView,
			url = "https://groupees.com/${bundle.name}",
			endsOn = bundle.endsOn.format(DateTimeFormatter.ISO_LOCAL_DATE),
			expiresIn = expiresInDays
		)
	}
}

data class BundleView(
	val title: String,
	val details: String,
	val products: List<ProductView>,
	val url: String,
	val endsOn: String,
	val expiresIn: Long
)

data class ProductView(
	val icon: String,
	val title: String
)
