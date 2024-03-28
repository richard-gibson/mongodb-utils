package org.mongokt.utils.query
import kotlin.internal.OnlyInputTypes
import com.mongodb.client.model.Filters
import org.bson.conversions.Bson

infix fun <@OnlyInputTypes T> QueryPath<*, T?>.eq(value: T): Bson = Filters.eq(path, value)

infix fun <@OnlyInputTypes T> QueryPath<*, Iterable<T?>?>.contains(value: T?): Bson = Filters.eq(path, value)

infix fun <@OnlyInputTypes T> QueryPath<*, T?>.ne(value: T?): Bson = Filters.ne(path, value)
infix fun <@OnlyInputTypes T> QueryPath<*, T?>.lt(value: T): Bson = Filters.lt(path, value)
infix fun <@OnlyInputTypes T> QueryPath<*, T?>.gt(value: T): Bson = Filters.gt(path, value)
infix fun <@OnlyInputTypes T> QueryPath<*, T?>.gte(value: T): Bson = Filters.gte(path, value)
infix fun <@OnlyInputTypes T> QueryPath<*, T?>.`in`(values: Iterable<T?>): Bson = Filters.`in`(path, values)
