package dev.johnbrainard.groupees.groupeesbrowser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class GroupeesBrowserApplication

fun main(args: Array<String>) {
	runApplication<GroupeesBrowserApplication>(*args)
}
