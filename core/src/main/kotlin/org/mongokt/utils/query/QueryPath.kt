package org.mongokt.utils.query

class QueryPath<T, out R>(
  private val previous: QueryPath<T, *>?,
  private val propertyPath: String,
) {
  internal val path: String
    get() = "${previous?.path?.let { "$it." } ?: ""}$propertyPath"
}

operator fun <R, S, T> QueryPath<R, S>.div(other: QueryPath<S, T>): QueryPath<R, T> =
  QueryPath(this, other.path)

operator fun <R, S> QueryPath<R, List<S>>.get(i: Int): QueryPath<R, S> =
  QueryPath(previous = this, "$i")

operator fun <R, K, V> QueryPath<R, Map<K, V>>.get(k: K): QueryPath<R, V> =
  QueryPath(previous = this, "$k")
