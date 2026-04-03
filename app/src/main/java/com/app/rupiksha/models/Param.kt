package com.app.rupiksha.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "Param")
data class Param(
    @field:Attribute(name = "name", required = false)
    var name: String? = null,

    @field:Attribute(name = "value", required = false)
    var value: String? = null
)
