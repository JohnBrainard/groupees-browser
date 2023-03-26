package dev.johnbrainard.groupees.groupeesbrowser

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration : WebMvcConfigurer {
	override fun addViewControllers(registry: ViewControllerRegistry) {
		registry.apply {
			addViewController("/").setViewName("redirect:/bundles")
		}
	}
}
