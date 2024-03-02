package dev.oblac.tdv.reporter

import io.pebbletemplates.pebble.extension.AbstractExtension
import io.pebbletemplates.pebble.extension.Filter
import io.pebbletemplates.pebble.template.EvaluationContext
import io.pebbletemplates.pebble.template.PebbleTemplate


class JsonWriterPebbleExtension : AbstractExtension() {
    override fun getFilters(): MutableMap<String, Filter> {
        return mutableMapOf(
            "json" to JsonFilter()
        )
    }

    class JsonFilter : Filter {
        override fun getArgumentNames(): MutableList<String> {
            return mutableListOf()
        }

        override fun apply(
            input: Any?,
            args: MutableMap<String, Any>,
            self: PebbleTemplate,
            context: EvaluationContext,
            lineNumber: Int,
        ): Any {
            return mapper.writeValueAsString(input)
        }

    }
}
