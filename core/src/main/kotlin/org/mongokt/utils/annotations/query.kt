package org.mongokt.utils.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
/**
 * Empty arrays means "Everything that matches annotated class"
 */
annotation class query
