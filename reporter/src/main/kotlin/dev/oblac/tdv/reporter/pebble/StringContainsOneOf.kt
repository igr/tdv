package dev.oblac.tdv.reporter.pebble

import io.pebbletemplates.pebble.extension.AbstractExtension
import io.pebbletemplates.pebble.node.expression.BinaryExpression
import io.pebbletemplates.pebble.operator.Associativity
import io.pebbletemplates.pebble.operator.BinaryOperator
import io.pebbletemplates.pebble.operator.BinaryOperatorType
import io.pebbletemplates.pebble.template.EvaluationContextImpl
import io.pebbletemplates.pebble.template.PebbleTemplateImpl

class StringContainsOneOf : AbstractExtension() {
    override fun getBinaryOperators(): MutableList<BinaryOperator> {
        return mutableListOf(
            StringContainsOneOfOperator()
        )
    }

    class StringContainsOneOfOperator : BinaryOperator {
        override fun getPrecedence() = 30

        override fun getSymbol() = "containsOneOf"

        override fun getType(): BinaryOperatorType = BinaryOperatorType.NORMAL

        override fun getAssociativity(): Associativity = Associativity.LEFT

        override fun createInstance(): BinaryExpression<Any> = object : BinaryExpression<Any>() {
            override fun evaluate(self: PebbleTemplateImpl?, context: EvaluationContextImpl?): Any {
                val left = leftExpression.evaluate(self, context) as String
                val right = rightExpression.evaluate(self, context) as List<String>

                return right.any { left.contains(it) }
            }
        }
    }
}
