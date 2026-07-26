package com.synapse.mobile.features.nlp.parser

class MultiCommandParser {

    private val separators = listOf(
        " and then ",
        " then ",
        " afterwards ",
        " after that ",
        " next ",
        " and ",
        ",",
        ";"
    )

    fun parse(input: String): List<String> {

        if (input.isBlank()) {
            return emptyList()
        }

        var commands = listOf(input.trim())

        for (separator in separators) {

            commands = commands.flatMap { command ->

                command.split(
                    separator,
                    ignoreCase = true
                )

            }

        }

        return commands
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }

}