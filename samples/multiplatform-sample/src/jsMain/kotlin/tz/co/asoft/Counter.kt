package tz.co.asoft;

import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.div
import styled.css
import styled.styledButton
import styled.styledSpan

private class Counter(p: Props) : RComponent<Counter.Props, Counter.State>(p) {
    class Props(val start: Int) : RProps
    class State(var value: Int) : RState

    init {
        state = State(p.start)
    }

    override fun RBuilder.render(): dynamic = div {
        styledButton {
            css { padding(horizontal = 4.px) }
            attrs.onClickFunction = { setState { value-- } }
            +"-"
        }
        styledSpan {
            css { padding(horizontal = 4.px) }
            +state.value.toString()
        }
        styledButton {
            css { padding(horizontal = 4.px) }
            attrs.onClickFunction = { setState { value++ } }
            +"+"
        }
    }
}

fun RBuilder.Counter(startAt: Int = 0) = child(Counter::class.js, Counter.Props(startAt)) {}