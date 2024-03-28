package mongodb.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.symbol.Nullability
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName
import mongodb.ksp.Errors.invalidClassType
import mongodb.ksp.Errors.noCompanionObject

internal fun KSClassDeclaration.companionObject(): KSClassDeclaration? =
  declarations.filterIsInstance<KSClassDeclaration>().firstOrNull { it.isCompanionObject }

internal fun KSClassDeclaration.hasCompanionObject(): Boolean =
  companionObject() != null
internal fun KSClassDeclaration.isSealed(): Boolean =
  modifiers.contains(Modifier.SEALED)

internal fun KSClassDeclaration.isDataClass(): Boolean =
  classKind == ClassKind.CLASS && modifiers.contains(Modifier.DATA)

internal fun KSClassDeclaration.isValue(): Boolean =
  modifiers.contains(Modifier.VALUE)

internal fun KSClassDeclaration.isValidClassType(): Boolean =
  isSealed() || isDataClass() || isValue()

internal fun KSDeclaration.qualifiedNameOrSimpleName(): String =
  (qualifiedName ?: simpleName).asString()

internal fun KSClassDeclaration.validate(logger: KSPLogger, annotationName: String) =
  if (!isValidClassType())
    logger.error(invalidClassType(annotationName))
  else if(!hasCompanionObject())
    logger.error(noCompanionObject(annotationName))
  else {}

internal fun KSName.asSanitizedString(delimiter: String = ".", prefix: String = "") =
  asString().sanitize(delimiter, prefix)

/**
 * Sanitizes each delimited section if it matches with Kotlin reserved keywords.
 */
internal fun String.sanitize(delimiter: String = ".", prefix: String = "") =
  splitToSequence(delimiter).joinToString(delimiter, prefix) { if (it in KOTLIN_KEYWORDS) "`$it`" else it }

private val KOTLIN_KEYWORDS = setOf(
  // Hard keywords
  "as",
  "break",
  "class",
  "continue",
  "do",
  "else",
  "false",
  "for",
  "fun",
  "if",
  "in",
  "interface",
  "is",
  "null",
  "object",
  "package",
  "return",
  "super",
  "this",
  "throw",
  "true",
  "try",
  "typealias",
  "typeof",
  "val",
  "var",
  "when",
  "while",

  // Soft keywords
  "by",
  "catch",
  "constructor",
  "delegate",
  "dynamic",
  "field",
  "file",
  "finally",
  "get",
  "import",
  "init",
  "param",
  "property",
  "receiver",
  "set",
  "setparam",
  "where",

  // Modifier keywords
  "actual",
  "abstract",
  "annotation",
  "companion",
  "const",
  "crossinline",
  "data",
  "enum",
  "expect",
  "external",
  "final",
  "infix",
  "inline",
  "inner",
  "internal",
  "lateinit",
  "noinline",
  "open",
  "operator",
  "out",
  "override",
  "private",
  "protected",
  "public",
  "reified",
  "sealed",
  "suspend",
  "tailrec",
  "value",
  "vararg",

  // These aren't keywords anymore but still break some code if unescaped.
  // https://youtrack.jetbrains.com/issue/KT-52315
  "header",
  "impl",

  // Other reserved keywords
  "yield",
)

internal object Errors {
  fun KSClassDeclaration.invalidClassType(annotation: String) =
    """
      |${qualifiedNameOrSimpleName()} cannot be annotated with $annotation
      | ^
      |Only data, sealed, and value classes can be annotated with $annotation
    """.trimMargin()

  fun KSClassDeclaration.noCompanionObject(annotation: String) =
    """
      |${qualifiedNameOrSimpleName()} must have a companion object to use $annotation
    """.trimMargin()
}
