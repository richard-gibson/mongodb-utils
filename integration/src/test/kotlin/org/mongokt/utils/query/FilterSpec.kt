package org.mongokt.utils.query

import com.mongodb.KotlinCodecProvider
import com.mongodb.client.model.Filters
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.bson.BsonDocument
import org.bson.BsonDocumentWrapper
import org.bson.BsonType
import org.bson.codecs.configuration.CodecRegistries
import org.bson.conversions.Bson
import org.mongokt.utils.domain.ContactInfo
import org.mongokt.utils.domain.Email
import org.mongokt.utils.domain.address
import org.mongokt.utils.domain.emails
import org.mongokt.utils.domain.name
import org.mongokt.utils.domain.number
import org.mongokt.utils.domain.street

class FilterSpec :
  ShouldSpec({
    val streetAddress = ContactInfo.address.street

    context("Filter eq") {
      should("create dot path") {
        streetAddress.name eq "name" bsonShouldMatch Filters.eq("address.street.name", "name")
      }
      should("process array index") {
        ContactInfo.emails[0].address eq "address" bsonShouldMatch Filters.eq("emails.0.address", "address")
      }
      should("handle null values") {
        streetAddress.name eq null bsonShouldMatch Filters.eq("address.street.name", null)
      }
    }

    context("Filter ne") {
      should("create dot path") {
        streetAddress.name ne "name" bsonShouldMatch Filters.ne("address.street.name", "name")
      }
      should("handle null values") {
        streetAddress.number ne null bsonShouldMatch Filters.ne("address.street.number", null)
      }
    }

    context("Filter not") {
      should("render nested eq") {
        not(streetAddress.name eq "name") bsonShouldMatch Filters.not(Filters.eq("address.street.name", "name"))
      }
      should("render nested gt") {
        not(streetAddress.number gt 1) bsonShouldMatch Filters.not(Filters.gt("address.street.number", 1))
      }
      should("render nested regex") {
        not(streetAddress.name regex "^A.*") bsonShouldMatch Filters.not(Filters.regex("address.street.name", "^A.*"))
      }
      should("render nested `in``") {
        val names = listOf("name1", "name2", "name3")
        not(streetAddress.name `in` names) bsonShouldMatch Filters.not(Filters.`in`("address.street.name", names))
      }
      should("render nested and") {
        val names = listOf("name1", "name2", "name3")
        not(
          and(
            streetAddress.name eq "name",
            streetAddress.number gt 1,
            streetAddress.name `in` names,
          ),
        ) bsonShouldMatch
          Filters.not(
            Filters.and(
              Filters.eq("address.street.name", "name"),
              Filters.gt("address.street.number", 1),
              Filters.`in`("address.street.name", names),
            ),
          )
      }
      should("render nested or") {
        val names = listOf("name1", "name2", "name3")
        not(
          or(
            streetAddress.name eq "name",
            streetAddress.number gt 1,
            streetAddress.name `in` names,
          ),
        ) bsonShouldMatch
          Filters.not(
            Filters.or(
              Filters.eq("address.street.name", "name"),
              Filters.gt("address.street.number", 1),
              Filters.`in`("address.street.name", names),
            ),
          )
      }
    }

    context("Filter nor") {
      should("render one nested filter") {
        nor(streetAddress.name eq "name") bsonShouldMatch
          Filters.nor(
            Filters.eq("address.street.name", "name"),
          )
      }
      should("render many nested filters") {
        nor(
          streetAddress.name eq "name",
          streetAddress.number gt 1,
        ) bsonShouldMatch
          Filters.nor(
            Filters.eq("address.street.name", "name"),
            Filters.gt("address.street.number", 1),
          )
      }
    }

    context("Filter gt") {
      should("render") {
        streetAddress.number gt 1 bsonShouldMatch
          Filters.gt("address.street.number", 1)
      }
    }

    context("Filter lt") {
      should("render") {
        streetAddress.number lt 1 bsonShouldMatch
          Filters.lt("address.street.number", 1)
      }
    }

    context("Filter gte") {
      should("render") {
        streetAddress.number gte 1 bsonShouldMatch
          Filters.gte("address.street.number", 1)
      }
    }

    context("Filter lte") {
      should("render") {
        streetAddress.number lte 1 bsonShouldMatch
          Filters.lte("address.street.number", 1)
      }
    }
    context("Filter exists") {
      should("render") {
        streetAddress.number.exists() bsonShouldMatch
          Filters.exists("address.street.number", true)
      }

      should("render false") {
        streetAddress.number exists false bsonShouldMatch
          Filters.exists("address.street.number", false)
      }
    }
    context("Filter or") {
      should("render empty") {
        or() bsonShouldMatch Filters.or()
      }

      should("render many or") {
        val names = listOf("name1", "name2", "name3")
        or(
          streetAddress.name eq "name",
          streetAddress.number gt 1,
          streetAddress.name `in` names,
        ) bsonShouldMatch
          Filters.or(
            Filters.eq("address.street.name", "name"),
            Filters.gt("address.street.number", 1),
            Filters.`in`("address.street.name", names),
          )
      }
    }
    context("Filter and") {
      should("render empty") {
        and() bsonShouldMatch Filters.and()
      }

      should("render many and") {
        val names = listOf("name1", "name2", "name3")
        and(
          streetAddress.name eq "name",
          streetAddress.number gt 1,
          streetAddress.name `in` names,
        ) bsonShouldMatch
          Filters.and(
            Filters.eq("address.street.name", "name"),
            Filters.gt("address.street.number", 1),
            Filters.`in`("address.street.name", names),
          )
      }
      should("render nested and") {
        val names = listOf("name1", "name2", "name3")
        and(
          and(
            streetAddress.name eq "name",
            streetAddress.number gt 1,
          ),
          streetAddress.name `in` names,
        ) bsonShouldMatch
          Filters.and(
            Filters.and(
              Filters.eq("address.street.name", "name"),
              Filters.gt("address.street.number", 1),
            ),
            Filters.`in`("address.street.name", names),
          )
      }
      should("render between exclusive") {
        and(
          streetAddress.number lt 1,
          streetAddress.number gt 3,
        ) bsonShouldMatch
          Filters.and(
            Filters.lt("address.street.number", 1),
            Filters.gt("address.street.number", 3),
          )
      }
      should("render between inclusive") {
        and(
          streetAddress.number lte 1,
          streetAddress.number gte 3,
        ) bsonShouldMatch
          Filters.and(
            Filters.lte("address.street.number", 1),
            Filters.gte("address.street.number", 3),
          )
      }
    }
    context("Filter all") {
      val allEmails =
        listOf(
          Email("address1@test.com", true),
          Email("address2@test.com", false),
        )
      should("render") {
        println((ContactInfo.emails all allEmails).toBsonDoc())
        ContactInfo.emails all allEmails bsonShouldMatch
          Filters.all("emails", allEmails)
      }
      should("render raw doc") {
        val allOperator = "\$all"
        (ContactInfo.emails all allEmails).toBsonDoc() bsonShouldMatch
          BsonDocument.parse(
            """
            {"emails": {"$allOperator": [
            {"address": "address1@test.com", "verified": true}, 
            {"address": "address2@test.com", "verified": false}
            ]}}
            """.trimIndent(),
          )
      }
    }
    context("Filter elemMatch") {
      val bsonDoc =
        mapOf(
          "address" to "address1@test.com",
          "verified" to true,
        ).toBsonDoc()
      should("render") {
        ContactInfo.emails.elemMatch(bsonDoc) bsonShouldMatch
          Filters.elemMatch("emails", bsonDoc)
      }
    }
    context("Filter in") {
      val inVals = listOf(1, 2, 3)
      should("render") {
        streetAddress.number `in` inVals bsonShouldMatch
          Filters.`in`("address.street.number", inVals)
      }
    }
    context("Filter nin") {
      val inVals = listOf(1, 2, 3)
      should("render") {
        streetAddress.number `nin` inVals bsonShouldMatch
          Filters.nin("address.street.number", inVals)
      }
    }
    context("Filter mod") {
      should("render") {
        streetAddress.number.mod(100, 7) bsonShouldMatch
          Filters.mod("address.street.number", 100, 7)
      }
    }
    context("Filter size") {
      should("render") {
        ContactInfo.emails.size(7) bsonShouldMatch
          Filters.size("emails", 7)
      }
    }
    context("Filter type") {
      should("render") {
        val bsonType = BsonType.STRING
        ContactInfo.emails type bsonType bsonShouldMatch
          Filters.type("emails", bsonType)
      }
    }
    context("Filter bitsAllClear") {
      should("render") {
        streetAddress.number bitsAllClear 13L bsonShouldMatch
          Filters.bitsAllClear("address.street.number", 13)
      }
    }
    context("Filter bitsAllSet") {
      should("render") {
        streetAddress.number bitsAllSet 13L bsonShouldMatch
          Filters.bitsAllSet("address.street.number", 13)
      }
    }
    context("Filter bitsAnyClear") {
      should("render") {
        streetAddress.number bitsAnyClear 13L bsonShouldMatch
          Filters.bitsAnyClear("address.street.number", 13)
      }
    }
    context("Filter bitsAnySet") {
      should("render") {
        streetAddress.number bitsAnySet 13L bsonShouldMatch
          Filters.bitsAnySet("address.street.number", 13)
      }
    }
    context("Filter regex ") {
      should("render string regex") {
        val pattern = "acme.*copr"
        streetAddress.name regex pattern bsonShouldMatch
          Filters.regex("address.street.name", pattern)
      }
      should("render regex") {
        val pattern = "acme.*copr".toRegex()
        streetAddress.name regex pattern bsonShouldMatch
          Filters.regex("address.street.name", pattern.toPattern())
      }
      should("render regex with options") {
        val pattern = "acme.*copr"
        val options = "i"
        streetAddress.name.regex(pattern, options) bsonShouldMatch
          Filters.regex("address.street.name", pattern, options)
      }
    }
    context("Filter where") {
      should("render") {
        val javaScriptExpression = """this.address.street.name == "foo""""
        where(javaScriptExpression) bsonShouldMatch Filters.where(javaScriptExpression)
      }
    }
  })

inline infix fun <reified T, reified U : T> T.bsonShouldMatch(expected: U?): T {
  this.toBsonDoc() shouldBe expected.toBsonDoc()
  return this
}

inline fun <reified T> T.toBsonDoc(): BsonDocument {
  val codecRegistries =
    CodecRegistries.fromRegistries(
      CodecRegistries.fromProviders(KotlinCodecProvider()),
      Bson.DEFAULT_CODEC_REGISTRY,
    )
  return BsonDocumentWrapper(this, codecRegistries.get(T::class.java))
}
