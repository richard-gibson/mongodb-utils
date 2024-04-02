package org.mongokt.utils.query

import kotlin.internal.OnlyInputTypes
import com.mongodb.client.model.Filters
import com.mongodb.client.model.TextSearchOptions
import com.mongodb.client.model.geojson.Geometry
import com.mongodb.client.model.geojson.Point
import org.bson.BsonDocument
import org.bson.BsonType
import org.bson.Document
import org.bson.conversions.Bson
import java.util.regex.Pattern


internal fun Iterable<Bson?>.filterNullOrEmpty(): List<Bson> =
  this.filterNotNull()
    .filterNot { it is Document && it.isEmpty() || it is BsonDocument && it.isEmpty() }

/**
 * Creates a filter that matches all documents where the value of the field name equals the specified value. Note that this doesn't
 * actually generate a `\$eq` operator, as the query language doesn't require it.
 *
 * @param value     the value
 * @tparam TItem  the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/eq \$eq]]
 */
infix fun <@OnlyInputTypes TItem> QueryPath<*, TItem?>.eq(value: TItem?): Bson = Filters.eq(path, value)


/**
 * Creates a filter that matches all documents where the value of the field name equals the specified value. Note that this doesn't
 * actually generate a `\$eq` operator, as the query language doesn't require it.
 *
 * @param value     the value
 * @tparam TItem  the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/eq \$eq]]
 */
infix fun <@OnlyInputTypes TItem> QueryPath<*, Iterable<TItem?>?>.contains(value: TItem?): Bson =
  Filters.eq(path, value)

/**
 * Allows the use of aggregation expressions within the query language.
 *
 * @param expression the aggregation expression
 * @tparam TExpression the expression type
 * @return the filter
 * @since 2.2
 * @note Requires MongoDB 3.6 or greater
 */
fun <TExpression> expr(expression: TExpression): Bson = Filters.expr(expression)


/**
 * Creates a filter that matches all documents that validate against the given JSON schema document.
 *
 * @param schema the JSON schema to validate against
 * @return the filter
 * @since 2.2
 * @note Requires MongoDB 3.6 or greater
 */
fun jsonSchema(schema: Bson): Bson = Filters.jsonSchema(schema)

/**
 * Creates a filter that matches all documents where the value of the field name does not equal the specified value.
 *
 * @param value     the value
 * @tparam TItem  the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/ne \$ne]]
 */
infix fun <@OnlyInputTypes TItem> QueryPath<*, TItem?>.ne(value: TItem?): Bson = Filters.ne(path, value)

/**
 * Creates a filter that matches all documents where the value of the field name does not equal the specified value.
 *
 * A friendly alias for the `neq` method.
 *
 * @param value     the value
 * @tparam TItem  the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/ne \$ne]]
 */
infix fun <@OnlyInputTypes TItem> QueryPath<*, TItem?>.notEqual(value: TItem?): Bson = Filters.ne(path, value)

/**
 * Creates a filter that matches all documents where the value of the given field is greater than the specified value.
 *
 * @param value the value
 * @tparam TItem the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/gt \$gt]]
 */
infix fun <@OnlyInputTypes TItem> QueryPath<*, TItem?>.gt(value: TItem): Bson = Filters.gt(path, value)

/**
 * Creates a filter that matches all documents where the value of the given field is less than the specified value.
 *
 * @param value the value
 * @tparam TItem the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/lt \$lt]]
 */
infix fun <@OnlyInputTypes TItem> QueryPath<*, TItem?>.lt(value: TItem): Bson = Filters.lt(path, value)

/**
 * Creates a filter that matches all documents where the value of the given field is greater than or equal to the specified value.
 *
 * @param value the value
 * @tparam TItem the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/gte \$gte]]
 */
infix fun <@OnlyInputTypes TItem> QueryPath<*, TItem?>.gte(value: TItem): Bson = Filters.gte(path, value)

/**
 * Creates a filter that matches all documents where the value of the given field is less than or equal to the specified value.
 *
 * @param value the value
 * @tparam TItem the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/lte \$lte]]
 */
infix fun <@OnlyInputTypes TItem> QueryPath<*, TItem?>.lte(value: TItem): Bson = Filters.lte(path, value)

/**
 * Creates a filter that matches all documents where the value of a field equals any value in the list of specified values.
 *
 * @param value the value
 * @tparam TItem   the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/in \$in]]
 */
infix fun <TItem> QueryPath<*, TItem>.`in`(value: String): Bson = Filters.`in`(path, value)

/**
 * Creates a filter that matches all documents where the value of a field equals any value in the list of specified values.
 *
 * @param values    the list of values
 * @tparam TItem   the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/in \$in]]
 */
@JvmName("inArray")
infix fun <@OnlyInputTypes TItem> QueryPath<*, Iterable<TItem?>>.`in`(values: Iterable<TItem?>): Bson =
  Filters.`in`(path, values)

/**
 * Creates a filter that matches all documents where the value of a field does not equal any of the specified values or does not exist.
 *
 * @param values    the list of values
 * @tparam TItem   the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/nin \$nin]]
 */
infix fun <@OnlyInputTypes TItem> QueryPath<*, TItem?>.nin(values: Iterable<TItem?>): Bson = Filters.nin(path, values)

/**
 * Creates a filter that matches all documents where the value of a field does not equal any of the specified values or does not exist.
 *
 * @param values    the list of values
 * @tparam TItem   the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/nin \$nin]]
 */
@JvmName("ninArray")
infix fun <@OnlyInputTypes TItem> QueryPath<*, Iterable<TItem?>>.nin(values: Iterable<TItem?>): Bson =
  Filters.nin(path, values)

/**
 * Creates a filter that performs a logical AND of the provided list of filters.  Note that this will only generate a "\$and"
 * operator if absolutely necessary, as the query language implicity ands together all the keys.  In other words, a query expression
 * like:
 *
 * <blockquote><pre>
 * and(eq("x", 1), lt("y", 3))
 * </pre></blockquote>
 *
 * will generate a MongoDB query like:
 *
 * <blockquote><pre>
 * {x : 1, y : {\$lt : 3}}
 * </pre></blockquote>
 *
 * @param filters the list of filters to and together
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/and \$and]]
 */
fun and(filters: Iterable<Bson?>): Bson = Filters.and(filters.filterNullOrEmpty())

/**
 * Creates a filter that performs a logical AND of the provided list of filters.  Note that this will only generate a "\$and"
 * operator if absolutely necessary, as the query language implicity ands together all the keys.  In other words, a query expression
 * like:
 *
 * <blockquote><pre>
 * and(eq("x", 1), lt("y", 3))
 * </pre></blockquote>
 *
 * will generate a MongoDB query like:
 *
 * <blockquote><pre>
 * {x : 1, y : {\$lt : 3}}
 * </pre></blockquote>
 *
 * @param filters the list of filters to and together
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/and \$and]]
 */
fun and(vararg filters: Bson?): Bson = and(filters.toList())

/**
 * Creates a filter that preforms a logical OR of the provided list of filters.
 *
 * @param filters the list of filters to and together
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/or \$or]]
 */
fun or(filters: Iterable<Bson?>): Bson = Filters.or(filters.filterNullOrEmpty())

/**
 * Creates a filter that preforms a logical OR of the provided list of filters.
 *
 * @param filters the list of filters to and together
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/or \$or]]
 */
fun or(vararg filters: Bson?): Bson = or(filters.toList())

/**
 * Creates a filter that matches all documents that do not match the passed in filter.
 * Requires the field name to passed as part of the value passed in and lifts it to create a valid "\$not" query:
 *
 * `not(eq("x", 1))`
 *
 * will generate a MongoDB query like:
 * `{x :\$not: {\$eq : 1}}`
 *
 * @param filter     the value
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/not \$not]]
 */
fun not(filter: Bson): Bson = Filters.not(filter)

/**
 * Creates a filter that performs a logical NOR operation on all the specified filters.
 *
 * @param filters    the list of values
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/nor \$nor]]
 */
fun nor(vararg filters: Bson): Bson = Filters.nor(*filters)

/**
 * Creates a filter that performs a logical NOR operation on all the specified filters.
 *
 * @param filters    the list of values
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/nor \$nor]]
 */
fun nor(filters: Iterable<Bson>): Bson = Filters.nor(filters)

/**
 * Creates a filter that matches all documents that contain the given field.
 *
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/exists \$exists]]
 */
fun <TItem> QueryPath<*, TItem>.exists(): Bson = Filters.exists(path)

/**
 * Creates a filter that matches all documents that either contain or do not contain the given field, depending on the value of the
 * exists parameter.
 *
 * @param exists    true to check for existence, false to check for absence
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/exists \$exists]]
 */
infix fun <TItem> QueryPath<*, TItem>.exists(exists: Boolean): Bson = Filters.exists(path, exists)

/**
 * Creates a filter that matches all documents where the value of the field is of the specified BSON type.
 *
 * @param bsonType      the BSON type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/type \$type]]
 */
infix fun <TItem> QueryPath<*, TItem>.type(type: BsonType): Bson = Filters.type(path, type)


/**
 * Creates a filter that matches all documents where the value of a field divided by a divisor has the specified remainder (i.e. perform
 * a modulo operation to select documents).
 *
 * @param divisor   the modulus
 * @param remainder the remainder
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/mod \$mod]]
 */
fun <TItem> QueryPath<*, TItem>.mod(divisor: Long, remainder: Long): Bson = Filters.mod(path, divisor, remainder)

/**
 * Creates a filter that matches all documents where the value of the field matches the given regular expression pattern.
 *
 * @param pattern   the pattern
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/regex \$regex]]
 */
infix fun QueryPath<*, String?>.regex(regex: Pattern): Bson = Filters.regex(path, regex)


/**
 * Creates a filter that matches all documents where the value of the field matches the given regular expression pattern with the given
 * options applied.
 *
 * @param pattern   the pattern
 * @param options   the options
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/regex \$regex]]
 */
infix fun QueryPath<*, String?>.regex(regex: String): Bson = Filters.regex(path, regex)

/**
 * Creates a filter that matches all documents where the value of the field matches the given regular expression pattern.
 *
 * @param regex   the regex
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/regex \$regex]]
 * @since 1.0
 */
infix fun QueryPath<*, String?>.regex(regex: Regex): Bson = Filters.regex(path, regex.toPattern())

/**
 * Creates a filter that matches all documents where the value of the property matches the given regular expression pattern.
 *
 * @param pattern   the pattern
 * @return the filter
 * @mongodb.driver.manual reference/operator/query/regex $regex
 */
@JvmName("regexIterable")
infix fun QueryPath<*, Iterable<String?>>.regex(regex: String): Bson = Filters.regex(path, regex)

/**
 * Creates a filter that matches all documents where the value of the property matches the given regular expression pattern.
 *
 * @param pattern   the pattern
 * @return the filter
 * @mongodb.driver.manual reference/operator/query/regex $regex
 */
@JvmName("regexIterable")
infix fun QueryPath<*, Iterable<String?>>.regex(regex: Pattern): Bson = Filters.regex(path, regex)

/**
 * Creates a filter that matches all documents where the value of the option matches the given regular expression pattern with the given
 * options applied.
 *
 * @param pattern   the pattern
 * @param options   the options
 * @return the filter
 * @mongodb.driver.manual reference/operator/query/regex $regex
 */
@JvmName("regexIterable")
fun QueryPath<*, Iterable<String?>>.regex(pattern: String, options: String): Bson =
  Filters.regex(path, pattern, options)

/**
 * Creates a filter that matches all documents where the value of the property matches the given regular expression pattern.
 *
 * @param regex   the regex
 * @return the filter
 * @mongodb.driver.manual reference/operator/query/regex $regex
 */
@JvmName("regexIterable")
infix fun QueryPath<*, Iterable<String?>>.regex(regex: Regex): Bson = Filters.regex(path, regex.toPattern())

/**
 * Creates a filter that matches all documents matching the given search term using the given language.
 * You may use [[Projections.metaTextScore]] to extract the relevance score assigned to each matched document.
 *
 * `Aggregates.search(SearchOperator, SearchOptions)` / `Aggregates.search(SearchCollector, SearchOptions)`
 * is a more powerful full-text search alternative.
 *
 * @param search   the search term
 * @param textSearchOptions the text search options to use
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/text \$text]]
 * @since 1.1
 */
fun text(search: String, textSearchOptions: TextSearchOptions = TextSearchOptions()): Bson =
  Filters.text(search, textSearchOptions)

/**
 * Creates a filter that matches all documents for which the given expression is true.
 *
 * @param javaScriptExpression the JavaScript expression
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/where \$where]]
 */
fun where(javaScriptExpression: String): Bson = Filters.where(javaScriptExpression)

/**
 * Creates a filter that matches all documents where the value of a field is an array that contains all the specified values.
 *
 * @param values    the list of values
 * @tparam TItem   the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/all \$all]]
 */
infix fun <@OnlyInputTypes TItem> QueryPath<*, Iterable<TItem>?>.all(values: Iterable<TItem>): Bson =
  Filters.all(path, values)

/**
 * Creates a filter that matches all documents where the value of a field is an array that contains all the specified values.
 *
 * @param values    the list of values
 * @tparam TItem   the value type
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/all \$all]]
 */
fun <@OnlyInputTypes TItem> QueryPath<*, Iterable<TItem>?>.all(vararg values: TItem): Bson = Filters.all(path, values)

/**
 * Creates a filter that matches all documents containing a field that is an array where at least one member of the array matches the
 * given filter.
 *
 * @param filter    the filter to apply to each element
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/elemMatch \$elemMatch]]
 */
infix fun <TItem> QueryPath<*, Iterable<TItem>?>.elemMatch(filter: Bson): Bson = Filters.elemMatch(path, filter)


/**
 * Creates a filter that matches all documents where the value of a field is an array of the specified size.
 *
 * @param size      the size of the array
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/size \$size]]
 */
infix fun <TItem> QueryPath<*, TItem>.size(size: Int): Bson = Filters.size(path, size)

/**
 * Creates a filter that matches all documents where all of the bit positions are clear in the field.
 *
 * @note Requires MongoDB 3.2 or greater
 * @param bitmask   the bitmask
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/bitsAllClear \$bitsAllClear]]
 * @since 1.1
 */
infix fun <TItem> QueryPath<*, TItem>.bitsAllClear(bitmask: Long): Bson = Filters.bitsAllClear(path, bitmask)

/**
 * Creates a filter that matches all documents where all of the bit positions are set in the field.
 *
 * @note Requires MongoDB 3.2 or greater
 * @param bitmask   the bitmask
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/bitsAllSet \$bitsAllSet]]
 * @since 1.1
 */
infix fun <TItem> QueryPath<*, TItem>.bitsAllSet(bitmask: Long): Bson = Filters.bitsAllSet(path, bitmask)

/**
 * Creates a filter that matches all documents where any of the bit positions are clear in the field.
 *
 * @note Requires MongoDB 3.2 or greater
 * @param bitmask   the bitmask
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/bitsAnyClear \$bitsAnyClear]]
 * @since 1.1
 */
infix fun <TItem> QueryPath<*, TItem>.bitsAnyClear(bitmask: Long): Bson = Filters.bitsAnyClear(path, bitmask)

/**
 * Creates a filter that matches all documents where any of the bit positions are set in the field.
 *
 * @note Requires MongoDB 3.2 or greater
 * @param bitmask   the bitmask
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/bitsAnySet \$bitsAnySet]]
 * @since 1.1
 */
infix fun <TItem> QueryPath<*, TItem>.bitsAnySet(bitmask: Long): Bson = Filters.bitsAnySet(path, bitmask)

/**
 * Creates a filter that matches all documents containing a field with geospatial data that exists entirely within the specified shape.
 *
 * @param geometry the bounding GeoJSON geometry object
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/geoWithin/ \$geoWithin]]
 */
infix fun <TItem> QueryPath<*, TItem>.geoWithin(geometry: Geometry): Bson = Filters.geoWithin(path, geometry)

/**
 * Creates a filter that matches all documents containing a field with geospatial data that exists entirely within the specified shape.
 *
 * @param geometry the bounding GeoJSON geometry object
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/geoWithin/ \$geoWithin]]
 */
infix fun <TItem> QueryPath<*, TItem>.geoWithin(geometry: Bson): Bson = Filters.geoWithin(path, geometry)

/**
 * Creates a filter that matches all documents containing a field with grid coordinates data that exist entirely within the specified
 * box.
 *
 * @param lowerLeftX  the lower left x coordinate of the box
 * @param lowerLeftY  the lower left y coordinate of the box
 * @param upperRightX the upper left x coordinate of the box
 * @param upperRightY the upper left y coordinate of the box
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/geoWithin/ \$geoWithin]]
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/box/#op._S_box \$box]]
 */
fun <TItem> QueryPath<*, TItem>.geoWithinBox(
  lowerLeftX: Double,
  lowerLeftY: Double,
  upperRightX: Double,
  upperRightY: Double
): Bson =
  Filters.geoWithinBox(path, lowerLeftX, lowerLeftY, upperRightX, upperRightY)

/**
 * Creates a filter that matches all documents containing a field with grid coordinates data that exist entirely within the specified
 * polygon.
 *
 * @param points    a Seq of pairs of x, y coordinates.  Any extra dimensions are ignored
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/geoWithin/ \$geoWithin]]
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/polygon/#op._S_polygon \$polygon]]
 */
fun <TItem> QueryPath<*, TItem>.geoWithinPolygon(points: List<List<Double>>): Bson =
  Filters.geoWithinPolygon(path, points)

/**
 * Creates a filter that matches all documents containing a field with grid coordinates data that exist entirely within the specified
 * circle.
 *
 * @param x         the x coordinate of the circle
 * @param y         the y coordinate of the circle
 * @param radius    the radius of the circle, as measured in the units used by the coordinate system
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/geoWithin/ \$geoWithin]]
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/center/#op._S_center \$center]]
 */
fun <TItem> QueryPath<*, TItem>.geoWithinCenter(x: Double, y: Double, radius: Double): Bson =
  Filters.geoWithinCenter(path, x, y, radius)

/**
 * Creates a filter that matches all documents containing a field with geospatial data (GeoJSON or legacy coordinate pairs) that exist
 * entirely within the specified circle, using spherical geometry.  If using longitude and latitude, specify longitude first.
 *
 * @param x         the x coordinate of the circle
 * @param y         the y coordinate of the circle
 * @param radius    the radius of the circle, in radians
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/geoWithin/ \$geoWithin]]
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/centerSphere/#op._S_centerSphere \$centerSphere]]
 */
fun <TItem> QueryPath<*, TItem>.geoWithinCenterSphere(x: Double, y: Double, radius: Double): Bson =
  Filters.geoWithinCenterSphere(path, x, y, radius)

/**
 * Creates a filter that matches all documents containing a field with geospatial data that intersects with the specified shape.
 *
 * @param geometry the bounding GeoJSON geometry object
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/geoIntersects/ \$geoIntersects]]
 */
infix fun <TItem> QueryPath<*, TItem>.geoIntersects(geometry: Geometry): Bson = Filters.geoIntersects(path, geometry)


/**
 * Creates a filter that matches all documents containing a field with geospatial data that intersects with the specified shape.
 *
 * @param geometry the bounding GeoJSON geometry object
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/geoIntersects/ \$geoIntersects]]
 */
infix fun <TItem> QueryPath<*, TItem>.geoIntersects(geometry: Bson): Bson = Filters.geoIntersects(path, geometry)

/**
 * Creates a filter that matches all documents containing a field with geospatial data that is near the specified GeoJSON point.
 *
 * @param geometry the bounding GeoJSON geometry object
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/near/ \$near]]
 */
fun <TItem> QueryPath<*, TItem>.near(geometry: Point, maxDistance: Double? = null, minDistance: Double? = null): Bson =
  Filters.near(path, geometry, maxDistance, minDistance)

/**
 * Creates a filter that matches all documents containing a field with geospatial data that is near the specified GeoJSON point.
 *
 * @param geometry the bounding GeoJSON geometry object
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/near/ \$near]]
 */
fun <TItem> QueryPath<*, TItem>.near(geometry: Bson, maxDistance: Double? = null, minDistance: Double? = null): Bson =
  Filters.near(path, geometry, maxDistance, minDistance)


/**
 * Creates a filter that matches all documents containing a field with geospatial data that is near the specified point.
 *
 * @param x the x coordinate
 * @param y the y coordinate
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/near/ \$near]]
 */
fun <TItem> QueryPath<*, TItem>.near(
  x: Double,
  y: Double,
  maxDistance: Double? = null,
  minDistance: Double? = null
): Bson =
  Filters.near(path, x, y, maxDistance, minDistance)

/**
 * Creates a filter that matches all documents containing a field with geospatial data that is near the specified GeoJSON point using
 * spherical geometry.
 *
 * @param geometry the bounding GeoJSON geometry object
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/near/ \$near]]
 */
fun <TItem> QueryPath<*, TItem>.nearSphere(
  geometry: Bson,
  maxDistance: Double? = null,
  minDistance: Double? = null
): Bson =
  Filters.nearSphere(path, geometry, maxDistance, minDistance)

/**
 * Creates a filter that matches all documents containing a field with geospatial data that is near the specified GeoJSON point using
 * spherical geometry.
 *
 * @param geometry the bounding GeoJSON geometry object
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/near/ \$near]]
 */
fun <TItem> QueryPath<*, TItem>.nearSphere(
  geometry: Point,
  maxDistance: Double? = null,
  minDistance: Double? = null
): Bson =
  Filters.nearSphere(path, geometry, maxDistance, minDistance)

/**
 * Creates a filter that matches all documents containing a field with geospatial data that is near the specified point using
 * spherical geometry.
 *
 * @param x the x coordinate
 * @param y the y coordinate
 * @return the filter
 * @see [[https://www.mongodb.com/docs/manual/reference/operator/query/near/ \$near]]
 */
fun <TItem> QueryPath<*, TItem>.nearSphere(
  x: Double,
  y: Double,
  maxDistance: Double? = null,
  minDistance: Double? = null
): Bson =
  Filters.nearSphere(path, x, y, maxDistance, minDistance)
