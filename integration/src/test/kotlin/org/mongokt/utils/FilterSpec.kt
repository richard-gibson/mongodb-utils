package org.mongokt.utils

import com.mongodb.client.model.Filters
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.mongokt.utils.domain.ContactInfo
import org.mongokt.utils.domain.address
import org.mongokt.utils.domain.name
import org.mongokt.utils.domain.emails
import org.mongokt.utils.domain.street
import org.mongokt.utils.domain.verified
import org.mongokt.utils.query.get
import org.mongokt.utils.query.eq

class FilterSpec : StringSpec({

  "query path should create filter" {
    ContactInfo.address.street.name eq "name" shouldBe Filters.eq("address.street.name", "name")
  }
  "query path should process array index" {
    ContactInfo.emails[0].verified eq true shouldBe Filters.eq("emails.0.verified", true)
  }

})


