package mongodb.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

class QueryProcessor(
  val codeGenerator: CodeGenerator, val logger: KSPLogger
) : SymbolProcessor {
  val annotationName = "org.mongokt.utils.annotations.query"
  override fun process(resolver: Resolver): List<KSAnnotated> {
    resolver.getSymbolsWithAnnotation(annotationName).filterIsInstance<KSClassDeclaration>().forEach(::processClass)
    return emptyList()
  }

  private fun processClass(klass: KSClassDeclaration) {
    klass.validate(logger, annotationName)
    klass.genQueryPaths(codeGenerator, evalAnnotatedClass(klass, annotationName, logger))
  }

}
