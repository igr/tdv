package dev.oblac.tdv.parser

class ParserException: RuntimeException {
    constructor(message: String, tdiLocation: ThreadDumpLocation) : super(message + "\n" + tdiLocation.toString())
}
