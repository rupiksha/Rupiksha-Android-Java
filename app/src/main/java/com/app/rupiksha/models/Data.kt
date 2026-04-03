package com.app.rupiksha.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Text

data class Data(
    @field:Attribute(name = "type", required = false)
    var type: String? = null,

    @field:Text(required = true)
    var value: String? = null
)
