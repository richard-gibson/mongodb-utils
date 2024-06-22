package mongodb.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import org.mongokt.utils.query.QueryPath

// public inline val Street.Companion.name: MongoPath<Street, String> get() = MongoPath(null, "name")
fun ClassName.companionMongoPath(
  propertyType: KSType,
  propertyName: String,
): PropertySpec {
  val queryPathClass = QueryPath::class.asClassName()
  val companion = nestedClass("Companion")
  return PropertySpec
    .builder(
      propertyName,
      queryPathClass.parameterizedBy(this, propertyType.toTypeName()),
    ).receiver(companion)
    .getter(
      FunSpec
        .getterBuilder()
        .addModifiers(KModifier.INLINE)
        .addStatement("return %T(null, %S)", QueryPath::class, propertyName)
        .build(),
    ).build()
}

// public inline val <P> QueryPath<P, Street>.name: QueryPath<P, String> get() = div(Street.name)
fun ClassName.propertyPath(
  propertyType: KSType,
  propertyName: String,
): PropertySpec {
  val genParam = TypeVariableName("P")
  val queryPathClass = QueryPath::class.asClassName()
  val mongoPathSelect = MemberName(queryPathClass.packageName, "div")
  return PropertySpec
    .builder(
      propertyName,
      queryPathClass.parameterizedBy(genParam, propertyType.toTypeName()),
    ).addTypeVariable(genParam)
    .receiver(queryPathClass.parameterizedBy(genParam, this))
    .getter(
      FunSpec
        .getterBuilder()
        .addModifiers(KModifier.INLINE)
        .addStatement("return %M(%T.%L)", mongoPathSelect, this, propertyName)
        .build(),
    ).build()
}

inline fun <reified T> arrayOfNotNull(element: T?) = listOfNotNull(element).toTypedArray()

fun KSClassDeclaration.genQueryPaths(
  codeGenerator: CodeGenerator,
  classProps: List<Pair<KSType, String>>,
) {
  val fileSpec =
    FileSpec.builder(
      packageName.asSanitizedString(),
      "${simpleName.asSanitizedString()}__MongoPaths",
    )
  val kpClass = asType(emptyList()).toClassName()
  val companionMongoPaths =
    classProps.map { (propertyType, propertyName) ->
      kpClass.companionMongoPath(propertyType, propertyName)
    }
  val propertyMongoPaths =
    classProps.map { (propertyType, propertyName) ->
      kpClass.propertyPath(propertyType, propertyName)
    }
  (companionMongoPaths + propertyMongoPaths).forEach(fileSpec::addProperty)
  fileSpec.build().writeTo(
    codeGenerator,
    Dependencies(true, *arrayOfNotNull(containingFile)),
  )
}
