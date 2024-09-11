package adapter

import InterpreterFactory
import controller.LexerVersionController
import interpreter.ErrorHandler
import interpreter.InputAdapter
import interpreter.OutputAdapterJava
import parser.Parser
import token.Token
import variable.VariableMap
import java.io.InputStream

class Adapter {
    fun execute(src: InputStream, version: String, emitter: OutputAdapterJava, handler: ErrorHandler, provider: InputAdapter) {
        try {
            val versionController = LexerVersionController()

            val lexer = versionController.getLexer(version, src)
            val tokens = mutableListOf<Token>()
            var token = lexer.getNextToken()
            while (token != null) {
                tokens.add(token)
                token = lexer.getNextToken()
            }
            println(tokens)
            val parser = Parser(tokens)
            val ast = parser.generateAST()
            val interpreter = InterpreterFactory(version , VariableMap(HashMap()), provider).buildInterpreter()
            val interpretedList = interpreter.interpret(ast)
            for (interpreted in interpretedList.second) {
                emitter.print(interpreted)
            }
        } catch (e: Exception) {
            handler.reportError(e.message)
        } catch (e: Error) {
            handler.reportError(e.message)
        }
    }
}