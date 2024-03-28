package mongodb.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.KSTypeParameter
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.Variance
import mongodb.ksp.Errors.invalidClassType

internal fun evalAnnotatedClass(
  klass: KSClassDeclaration,
  annotationName: String,
  logger: KSPLogger
): List<Pair<KSType, String>> {
  return if (klass.isDataClass() || klass.isValue()) {
    klass.getConstructorParams()
  } else {
    logger.error(klass.invalidClassType(annotationName), klass)
    emptyList()
  }
}

internal fun KSClassDeclaration.getConstructorParams(): List<Pair<KSType, String>> =
  primaryConstructor?.parameters?.mapNotNull(KSValueParameter::paramInfo).orEmpty()

internal fun KSValueParameter.paramInfo(): Pair<KSType, String>? =
  name?.asString()?.let { type.resolve() to it }

internal fun KSType.qualifiedString(prefix: String = ""): String = when (declaration) {
  is KSTypeParameter -> {
    val n = declaration.simpleName.asSanitizedString(prefix = prefix)
    if (isMarkedNullable) "$n?" else n
  }

  else -> when (val qname = declaration.qualifiedName?.asSanitizedString(prefix = prefix)) {
    null -> toString()
    else -> {
      val withArgs = when {
        arguments.isEmpty() -> qname
        else -> "$qname<${arguments.joinToString(separator = ", ") { it.qualifiedString() }}>"
      }
      if (isMarkedNullable) "$withArgs?" else withArgs
    }
  }
}


internal fun KSTypeArgument.qualifiedString(): String = when (val ty = type?.resolve()) {
  null -> toString()
  else -> when (variance) {
    Variance.STAR -> "*"
    Variance.INVARIANT -> ty.qualifiedString()
    else -> ty.qualifiedString(prefix = "${variance.label} ")
  }
}
