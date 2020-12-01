package tz.co.asoft.ticker

import kotlinx.coroutines.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.br
import styled.*

private class Ticker(p: Props) : RComponent<Ticker.Props, Ticker.State>(p), CoroutineScope by CoroutineScope(SupervisorJob()) {

    class Props(val config: TickConfig) : RProps
    class State(var config: TickConfig, var ui: TickerUIState) : RState

    init {
        state = State(p.config, TickerUIState.Ticking(p.config.startAt))
    }

    var tickingJob: Job? = null

    data class TickConfig(val dif: Int, val startAt: Int, val interval: Int)

    sealed class TickerUIState {
        class Ticking(val value: Int) : TickerUIState()
        object Paused : TickerUIState()
        object Setting : TickerUIState()
    }

    private fun startTickingJob(startAt: Int) {
        tickingJob = launch {
            var value = startAt
            while (true) {
                setState {
                    ui = TickerUIState.Ticking(value = value)
                }
                delay(state.config.interval.toLong())
                value += state.config.dif
            }
        }
    }

    override fun componentDidMount() {
        startTickingJob(state.config.startAt)
    }

    private fun RBuilder.Ticking(ticking: TickerUIState.Ticking) = styledDiv {
        styledH2 { +"${ticking.value}" }
        styledButton {
            +"Pause"
            attrs.onClickFunction = {
                tickingJob?.cancel()
                setState {
                    ui = TickerUIState.Paused
                }
            }
        }
        styledButton {
            +"Setting"
            attrs.onClickFunction = {
                tickingJob?.cancel()
                setState {
                    ui = TickerUIState.Setting
                }
            }
        }
    }

    private fun RBuilder.Setting() = styledDiv {
        val config = state.config
        styledInput {
            attrs.value = "${(if (config.dif > 0) "+" else "")}${config.dif}"
        }
        br { }
        styledInput {
            attrs.value = "Starting at: ${config.startAt}"
        }
        br { }
        styledInput {
            attrs.value = "Interval: ${config.interval} ms"
        }
        br { }
        styledButton {
            +"Cancel"
            attrs.onClickFunction = {
                startTickingJob(state.config.startAt)
            }
        }
        styledButton {
            +"Resume"
            attrs.onClickFunction = {
                startTickingJob(state.config.startAt)
            }
        }
    }


    private fun RBuilder.Pause() = styledDiv {
        styledH3 { +"Ticker is Paused" }
        styledButton {
            +"Resume"
            attrs.onClickFunction = {
                startTickingJob(state.config.startAt)
            }
        }
        styledButton {
            +"Setting"
            attrs.onClickFunction = {
                tickingJob?.cancel()
                setState {
                    ui = TickerUIState.Setting
                }
            }
        }
    }

    override fun RBuilder.render(): dynamic = when (val ui = state.ui) {
        is TickerUIState.Ticking -> Ticking(ui)
        TickerUIState.Paused -> Pause()
        TickerUIState.Setting -> Setting()
    }
}

fun RBuilder.Ticker(
        dif: Int = 1,
        startAt: Int = 0,
        interval: Int = 1000
) = child(Ticker::class.js, Ticker.Props(Ticker.TickConfig(dif, startAt, interval))) {}