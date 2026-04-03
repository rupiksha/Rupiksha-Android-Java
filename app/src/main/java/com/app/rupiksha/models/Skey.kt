package com.app.rupiksha.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Text

data class Skey(
    @field:Attribute(name = "ci", required = false)
    var ci: String? = null,

    @field:Text(required = true)
    var value: String? = null
)
